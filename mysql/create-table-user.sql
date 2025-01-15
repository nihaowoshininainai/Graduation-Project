CREATE TABLE User(  
    uid int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    create_time DATETIME COMMENT '注册时间',
    uname VARCHAR(255) UNIQUE COMMENT '用户名',
    pwd VARCHAR(255) COMMENT '用户密码'
) COMMENT '用户表';