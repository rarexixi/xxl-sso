CREATE database if NOT EXISTS `sso`;

CREATE DATABASE `sso`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

use `sso`;

CREATE TABLE `user`
(
  `id`          INT          NOT NULL AUTO_INCREMENT
  COMMENT '用户ID',
  `username`    VARCHAR(50)  NOT NULL
  COMMENT '用户名',
  `email`       VARCHAR(200) NOT NULL
  COMMENT '邮箱',
  `password`    VARCHAR(200) NOT NULL
  COMMENT '密码',
  `nick_name`   VARCHAR(50)  NOT NULL DEFAULT ''
  COMMENT '昵称',
  `is_deleted`  TINYINT      NOT NULL DEFAULT 0
  COMMENT '是否删除',
  `create_time` TIMESTAMP    NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time` TIMESTAMP    NOT NULL DEFAULT current_timestamp
  ON UPDATE current_timestamp
  COMMENT '更新时间',

  PRIMARY KEY (`id`),
  UNIQUE (`username`),
  UNIQUE (`email`),
  INDEX (`nick_name`),
  INDEX (`create_time`),
  INDEX (`update_time`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_unicode_ci
  COMMENT ='用户';

INSERT INTO user (username, email, password, nick_name) VALUES
  ('root', 'root@example.com', '123456', 'root'),
  ('admin1', 'admin1@example.com', '123456', 'admin1'),
  ('admin2', 'admin2@example.com', '123456', 'admin2'),
  ('admin3', 'admin3@example.com', '123456', 'admin3'),
  ('admin4', 'admin4@example.com', '123456', 'admin4'),
  ('admin5', 'admin5@example.com', '123456', 'admin5'),
  ('user1', 'user1@example.com', '123456', 'user1'),
  ('user2', 'user2@example.com', '123456', 'user2'),
  ('user3', 'user3@example.com', '123456', 'user3'),
  ('user4', 'user4@example.com', '123456', 'user4'),
  ('user5', 'user5@example.com', '123456', 'user5'),
  ('user6', 'user6@example.com', '123456', 'user6'),
  ('user7', 'user7@example.com', '123456', 'user7'),
  ('user8', 'user8@example.com', '123456', 'user8'),
  ('user9', 'user9@example.com', '123456', 'user9'),
  ('user10', 'user10@example.com', '123456', 'user10'),
  ('user11', 'user11@example.com', '123456', 'user11'),
  ('user12', 'user12@example.com', '123456', 'user12'),
  ('user13', 'user13@example.com', '123456', 'user13'),
  ('user14', 'user14@example.com', '123456', 'user14'),
  ('user15', 'user15@example.com', '123456', 'user15'),
  ('user16', 'user16@example.com', '123456', 'user16'),
  ('user17', 'user17@example.com', '123456', 'user17'),
  ('user18', 'user18@example.com', '123456', 'user18'),
  ('user19', 'user19@example.com', '123456', 'user19'),
  ('user20', 'user20@example.com', '123456', 'user20'),
  ('user21', 'user21@example.com', '123456', 'user21'),
  ('user22', 'user22@example.com', '123456', 'user22'),
  ('user23', 'user23@example.com', '123456', 'user23'),
  ('user24', 'user24@example.com', '123456', 'user24'),
  ('user25', 'user25@example.com', '123456', 'user25'),
  ('user26', 'user26@example.com', '123456', 'user26'),
  ('user27', 'user27@example.com', '123456', 'user27'),
  ('user28', 'user28@example.com', '123456', 'user28'),
  ('user29', 'user29@example.com', '123456', 'user29'),
  ('user30', 'user30@example.com', '123456', 'user30');

COMMIT;
