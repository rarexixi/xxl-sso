package org.xi.sso.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Redis client base on jedis 根据继承类的不同,
 * jedis实例方式不用:JedisSimpleFactry/JedisPoolFactry/ShardedJedisPoolFactry
 *
 * @author xuxueli 2015-7-10 18:34:07
 * <p>
 * # for redis (sharded.jedis.address=host01:port,host02:port)
 * sharded.jedis.address=127.0.0.1:6379,127.0.0.1:6379,127.0.0.1:6379
 */
public class JedisUtil {

    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    // 默认过期时间,单位/秒
    private static final int DEFAULT_EXPIRE_TIME = 2 * 60 * 60;

    /**
     * 方式 01: Redis单节点 + Jedis单例 : Redis单节点压力过重, Jedis单例存在并发瓶颈，不可用于线上
     * new Jedis("127.0.0.1", 6379).get("cache_key");
     * <p>
     * 方式 02: Redis单节点 + JedisPool单节点连接池
     * Redis单节点压力过重，负载和容灾比较差
     * new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 10000).getResource().get("cache_key");
     * <p>
     * 方式 03: Redis集群(通过client端集群,一致性哈希方式实现) + Jedis多节点连接池，
     * Redis集群，负载和容灾较好, ShardedJedisPool一致性哈希分片，读写均匀，动态扩充
     * new ShardedJedisPool(new JedisPoolConfig(), new LinkedList<JedisShardInfo>());
     */
    private static ShardedJedisPool shardedJedisPool;

    public static void init(String address) {

        List<JedisShardInfo> jedisShardInfos = new LinkedList<>();

        String[] addressArr = address.split(",");
        for (int i = 0; i < addressArr.length; i++) {
            String[] addressInfo = addressArr[i].split(":");
            String host = addressInfo[0];
            int port = Integer.valueOf(addressInfo[1]);
            JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port, 10000);
            jedisShardInfos.add(jedisShardInfo);
        }
        JedisPoolConfig config = getJedisPoolConfig();
        shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);

        if (shardedJedisPool == null) {
            throw new NullPointerException("JedisUtil.ShardedJedisPool is null.");
        }
    }

    private static JedisPoolConfig getJedisPoolConfig() {

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(50);
        config.setMinIdle(8);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        config.setTestWhileIdle(true);
        config.setTimeBetweenEvictionRunsMillis(30000);
        config.setNumTestsPerEvictionRun(10);
        config.setMinEvictableIdleTimeMillis(60000);
        return config;
    }

    // region jedis utils

    /*
     * 存储简单的字符串或者是Object 因为jedis没有分装直接存储Object的方法，所以在存储对象需斟酌下
     * 存储对象的字段是不是非常多而且是不是每个字段都用到，如果是的话那建议直接存储对象，
     * 否则建议用集合的方式存储，因为redis可以针对集合进行日常的操作很方便而且还可以节省空间
     */

    /**
     * Set String (默认存活时间, 2H)
     *
     * @param key
     * @param value
     * @return
     */
    public static String setStringValue(String key, String value) {
        return setStringValue(key, value, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Set String
     *
     * @param key
     * @param value
     * @param seconds 存活时间,单位/秒
     * @return
     */
    public static String setStringValue(String key, String value, int seconds) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.setex(key, seconds, value);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return null;
    }

    /**
     * Set Object
     *
     * @param key
     * @param obj
     * @param seconds 存活时间,单位/秒
     */
    public static String setObjectValue(String key, Object obj, int seconds) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.setex(key.getBytes(), seconds, serialize(obj));
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return null;
    }

    /**
     * Set Object (默认存活时间, 2H)
     *
     * @param key
     * @param obj
     * @return
     */
    public static String setObjectValue(String key, Object obj) {
        return setObjectValue(key, obj, DEFAULT_EXPIRE_TIME);
    }

    /**
     * Get String
     *
     * @param key
     * @return
     */
    public static String getStringValue(String key) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.get(key);
        } catch (Exception e) {
            logger.info("", e);
        }
        return null;
    }

    /**
     * Get Object
     *
     * @param key
     * @return
     */
    public static Object getObjectValue(String key) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            byte[] bytes = client.get(key.getBytes());
            if (bytes != null && bytes.length > 0) {
                return deserialize(bytes);
            }
        } catch (Exception e) {
            logger.info("", e);
        }
        return null;
    }

    /**
     * Delete
     *
     * @param key
     * @return Integer reply, specifically:
     * an integer greater than 0 if one or more keys were removed
     * 0 if none of the specified key existed
     */
    public static Long del(String key) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.del(key);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return null;
    }

    /**
     * incrBy value值加i
     *
     * @param key
     * @param i
     * @return new value after incr
     */
    public static Long incrBy(String key, int i) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.incrBy(key, i);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return null;
    }

    /**
     * exists
     *
     * @param key
     * @return Boolean reply, true if the key exists, otherwise false
     */
    public static boolean exists(String key) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.exists(key);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return false;
    }

    /**
     * expire 重置存活时间
     *
     * @param key
     * @param seconds 存活时间,单位/秒
     * @return Integer reply, specifically:
     * 1: the timeout was set.
     * 0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
     */
    public static Long expire(String key, int seconds) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.expire(key, seconds);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return null;
    }

    /**
     * expireAt 设置存活截止时间
     *
     * @param key
     * @param unixTime 存活截止时间戳
     * @return
     */
    public static Long expireAt(String key, long unixTime) {

        try (ShardedJedis client = shardedJedisPool.getResource()) {
            return client.expireAt(key, unixTime);
        } catch (Exception e) {
            logger.info("{}", e);
        }
        return null;
    }

    // endregion

    // region serialize and deserialize

    /**
     * object -> byte[]
     *
     * @param object
     * @return
     */
    private static byte[] serialize(Object object) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {

            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            logger.error("{}", e);
        }
        return null;
    }

    /**
     * byte[] -> Object
     *
     * @param bytes
     * @return
     */
    private static Object deserialize(byte[] bytes) {

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {

            return objectInputStream.readObject();
        } catch (Exception e) {
            logger.error("{}", e);
        }
        return null;
    }

    // endregion
}
