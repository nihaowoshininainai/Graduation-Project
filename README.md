# Graduation-Project(毕业设计)

## 本地环境配置

### Mysql服务部署

- 使用docker命令：sudo docker run --restart=always -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:latest
- 用户名：root
- 密码：123456

#### Mysql基本配置

- - 建表命令：
    CREATE TABLE User(
    uid int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
    create_time DATETIME COMMENT '注册时间',
    uname VARCHAR(255) UNIQUE COMMENT '用户名',
    pwd VARCHAR(255) COMMENT '用户密码'
    ) COMMENT '用户表';
- - 数据库名：shareNovel
- - 表：
- - - 用户表(User)：![用户表](./img/241219_20h09m09s_screenshot.png "用户表")

### Redis服务部署

使用docker命令:sudo docker run --restart=always -p 6379:6379 --name myredis -d redis:latest --requirepass 123456

用户名：无

密码:123456

## api文档

- 基础路径:/api

- - 注册：
- - - 路径：/register
- - - 方法：post
- - - 参数：
- - - - uname:string 用户名
- - - - pwd:string 用户密码
- - - 返回值：json字符串
- - - - state 状态值（1为成功，0为失败）
- - - - message 说明信息
- - - - data token令牌/null

- - 登陆：
- - - 路径：/login
- - - 方法：post
- - - 参数：
- - - - uname:string 用户名
- - - - pwd:string 用户密码
- - - 返回值：json字符串
- - - - state 状态值（1为成功，0为失败）
- - - - message 说明信息
- - - - data token令牌/null

- - 修改密码
- - - 路径：/resetPwd
- - - 方法：post
- - - 参数：
- - - - newPwd:string 新密码
- - - - oldPwd:string 旧密码
- - - 返回值：json字符串
- - - - state 状态值（1为成功，0为失败）
- - - - message 说明信息
- - - - data token令牌/null

- - 注销功能
- - - 路径：/deleteUser
- - - 方法：post
- - - 参数：
- - - - pwd:string 密码
- - - 返回值：json字符串
- - - - state 状态值（1为成功，0为失败）
- - - - message 说明信息
- - - - data token令牌/null