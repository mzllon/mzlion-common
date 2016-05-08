DROP TABLE IF EXISTS user_info;
CREATE TABLE user_info (
  id        INT PRIMARY KEY AUTO_INCREMENT,
  user_name VARCHAR(32)  NOT NULL,
  user_pwd  VARCHAR(32)  NOT NULL,
  real_name VARCHAR(20)  NULL,
  nick_name VARCHAR(20)  NOT NULL,
  gender    CHAR(1)      NULL,
  birth     CHAR(8)      NULL,
  hobby     VARCHAR(128) NULL,
  avatar    VARCHAR(64)  NULL,
  UNIQUE KEY UK_1 (user_name)
) ENGINE =InnoDB DEFAULT CHARSET = utf8;

-- COMMENT ON TABLE user_info IS '用户信息表'
-- COMMENT ON COLUMN user_info.id IS '主键';
-- COMMENT ON COLUMN user_info.user_name IS '用户名';
-- COMMENT ON COLUMN user_info.user_pwd IS '用户密码';
-- COMMENT ON COLUMN user_info.real_name IS '用户真实姓名';
-- COMMENT ON COLUMN user_info.nick_name IS '用户昵称';
-- COMMENT ON COLUMN user_info.gender IS '用户性别';
-- COMMENT ON COLUMN user_info.birth IS '出生日期';
-- COMMENT ON COLUMN user_info.hobby IS '爱好';
-- COMMENT ON COLUMN user_info.avatar IS '用户头像';


