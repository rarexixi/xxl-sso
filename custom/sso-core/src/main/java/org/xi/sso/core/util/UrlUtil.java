package org.xi.sso.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UrlUtil {

    /**
     * 拼接url
     *
     * @param url
     * @param parameters
     * @return
     */
    public static String getUrl(String url, Map<String, String> parameters) {

        if (StringUtils.isBlank(url)) return "";

        if (parameters != null && parameters.size() > 0) {
            List<String> parameterList = new ArrayList<>();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                parameterList.add(entry.getKey() + "=" + entry.getValue());
            }
            url += (url.contains("?") ? "&" : "?") + String.join("&", parameterList);
        }
        return url;
    }

    public static String encode(String s) {
        return encode(s, "utf-8");
    }

    public static String encode(String s, String encoding) {

        if (StringUtils.isBlank(s)) return "";
        if (StringUtils.isBlank(encoding)) encoding = "utf-8";
        try {
            return URLEncoder.encode(s, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String decode(String s) {
        return decode(s, "utf-8");
    }

    public static String decode(String s, String encoding) {

        if (StringUtils.isBlank(s)) return "";
        if (StringUtils.isBlank(encoding)) encoding = "utf-8";
        try {
            return URLDecoder.decode(s, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
