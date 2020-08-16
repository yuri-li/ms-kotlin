---
typora-root-url: assets
---

# 1 hello world

![image-20200727122625469](/image-20200727122625469.png)

# 2 Queries 

场景： 使用GraphQL模拟实现`SQL经典练习题（老师，学生，成绩）`

## 2.1 database

### 2.1.1 创建一个空的database

```
# 1 下载镜像
[root@JD3 ~]# docker image pull postgres:13-alpine

# 2 毕竟是数据库嘛，先挂个volume
[root@JD3 ~]# docker volume create db

# 3 启动容器
[root@JD3 ~]# docker container run -d -p 5432:5432 --name postgres -v db:/var/lib/postgresql/data  -e POSTGRES_PASSWORD=123456 postgres:13-alpine

# 4 进入容器
[root@JD3 ~]# docker container exec -it postgres /bin/bash
bash-5.0# 

# 5 访问数据库
bash-5.0# psql -U postgres
psql (13beta3)
Type "help" for help.

# 6 清理环境
postgres=# DROP DATABASE IF EXISTS graphql;
DROP DATABASE
postgres=# \l
                                 List of databases
   Name    |  Owner   | Encoding |  Collate   |   Ctype    |   Access privileges   
-----------+----------+----------+------------+------------+-----------------------
 postgres  | postgres | UTF8     | en_US.utf8 | en_US.utf8 | 
 template0 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +
           |          |          |            |            | postgres=CTc/postgres
 template1 | postgres | UTF8     | en_US.utf8 | en_US.utf8 | =c/postgres          +
           |          |          |            |            | postgres=CTc/postgres
(3 rows)

# 7 创建database
postgres=# CREATE DATABASE graphql;
CREATE DATABASE

# 8 使用最新创建的database
postgres=# \connect graphql
You are now connected to database "graphql" as user "postgres".

# 9 创建schema（user是关键字，所以，用双引号括起来）
graphql=# CREATE SCHEMA "user";
CREATE SCHEMA
```

### 2.1.2 微服务与数据库

![image-20200817023148338](/image-20200817023148338.png)



![image-20200817023718478](/image-20200817023718478.png)



# 3 Mutations



