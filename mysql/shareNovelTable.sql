-- MySQL dump 10.13  Distrib 9.1.0, for Linux (x86_64)
--
-- Host: localhost    Database: shareNovel
-- ------------------------------------------------------
-- Server version	9.1.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Admin`
--

DROP TABLE IF EXISTS `Admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Admin` (
  `aid` int NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `aname` varchar(255) DEFAULT NULL COMMENT '管理员名',
  `pwd` varchar(255) DEFAULT NULL COMMENT '管理员密码',
  PRIMARY KEY (`aid`),
  UNIQUE KEY `aname` (`aname`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Collection`
--

DROP TABLE IF EXISTS `Collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Collection` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `uid` int NOT NULL COMMENT '用户id',
  `nid` int NOT NULL COMMENT '小说id',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `nid` (`nid`),
  CONSTRAINT `Collection_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`),
  CONSTRAINT `Collection_ibfk_2` FOREIGN KEY (`nid`) REFERENCES `Novel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Comments`
--

DROP TABLE IF EXISTS `Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Comments` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nid` int NOT NULL COMMENT '小说id',
  `uid` int NOT NULL COMMENT '用户id',
  `content` text NOT NULL COMMENT '评论内容',
  `state` int NOT NULL DEFAULT '0' COMMENT '0为正常状态，1为被举报，-1为被封禁',
  PRIMARY KEY (`id`),
  KEY `nid` (`nid`),
  KEY `uid` (`uid`),
  CONSTRAINT `Comments_ibfk_1` FOREIGN KEY (`nid`) REFERENCES `Novel` (`id`),
  CONSTRAINT `Comments_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CommentsLike`
--

DROP TABLE IF EXISTS `CommentsLike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CommentsLike` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cid` int NOT NULL COMMENT '评论id',
  `uid` int NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  KEY `cid` (`cid`),
  KEY `uid` (`uid`),
  CONSTRAINT `CommentsLike_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `Comments` (`id`),
  CONSTRAINT `CommentsLike_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论点赞表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Label`
--

DROP TABLE IF EXISTS `Label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Label` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `name` varchar(255) DEFAULT NULL COMMENT '标签名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Novel`
--

DROP TABLE IF EXISTS `Novel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Novel` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '小说id',
  `title` varchar(255) DEFAULT NULL COMMENT '小说标题',
  `content` text COMMENT '小说正文',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `uid` int NOT NULL COMMENT '上传用户id',
  `views` int DEFAULT '0' COMMENT '浏览量',
  `state` int(10) unsigned zerofill NOT NULL DEFAULT '0000000000' COMMENT '封禁状态（0表示审核中，1表示审核通过，2表示被举报下架）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`),
  KEY `user_create_novel` (`uid`),
  CONSTRAINT `user_create_novel` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NovelLabel`
--

DROP TABLE IF EXISTS `NovelLabel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `NovelLabel` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nid` int NOT NULL COMMENT '小说id',
  `lid` int NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `uname` varchar(255) DEFAULT NULL COMMENT '用户名',
  `pwd` varchar(255) DEFAULT NULL COMMENT '用户密码',
  `banned` int NOT NULL DEFAULT '0' COMMENT '封禁状态（0表示正常，1表示封禁）',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-05  5:29:28
