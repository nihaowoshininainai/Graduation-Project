# Graduation-Project(毕业设计)
## 本地环境配置
### Mysql服务部署

使用docker命令：sudo docker run --restart=always -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:latest

用户名：root

密码：123456

### Redis服务部署

使用docker命令:sudo docker run --restart=always -p 6379:6379 --name myredis -d redis:latest --requirepass 123456

用户名：无

密码:123456

## Mysql基本配置

数据库名：shareNoval


