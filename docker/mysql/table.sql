/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.11.10-MariaDB, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: shareNovel
-- ------------------------------------------------------
-- Server version	9.2.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Admin`
--
USE shareNovel;

DROP TABLE IF EXISTS `Admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Admin` (
  `aid` int NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `aname` varchar(255) DEFAULT NULL COMMENT '管理员名',
  `pwd` varchar(255) DEFAULT NULL COMMENT '管理员密码',
  PRIMARY KEY (`aid`),
  UNIQUE KEY `aname` (`aname`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';

INSERT INTO `Admin` VALUES
(1,'admin','1111');
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Collection`
--

DROP TABLE IF EXISTS `Collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Collection` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `uid` int NOT NULL COMMENT '用户id',
  `nid` int NOT NULL COMMENT '小说id',
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `nid` (`nid`),
  CONSTRAINT `Collection_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`),
  CONSTRAINT `Collection_ibfk_2` FOREIGN KEY (`nid`) REFERENCES `Novel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Comments`
--

DROP TABLE IF EXISTS `Comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `CommentsLike`
--

DROP TABLE IF EXISTS `CommentsLike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Label` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `name` varchar(255) DEFAULT NULL COMMENT '标签名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Novel`
--

DROP TABLE IF EXISTS `Novel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Novel` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '小说id',
  `title` varchar(255) DEFAULT NULL COMMENT '小说标题',
  `content` text COMMENT '小说正文',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `uid` int NOT NULL COMMENT '上传用户id',
  `views` int DEFAULT '0' COMMENT '浏览量',
  `state` int NOT NULL DEFAULT '0' COMMENT '封禁状态（0审核中，-1审核不通过，1审核通过，2被举报需要管理员审核，3管理员核实违规下架处理）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `title` (`title`),
  KEY `user_create_novel` (`uid`),
  CONSTRAINT `user_create_novel` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `NovelLabel`
--

DROP TABLE IF EXISTS `NovelLabel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `NovelLabel` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `nid` int NOT NULL COMMENT '小说id',
  `lid` int NOT NULL COMMENT '标签id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `uid` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `uname` varchar(255) DEFAULT NULL COMMENT '用户名',
  `pwd` varchar(255) DEFAULT NULL COMMENT '用户密码',
  `banned` int NOT NULL DEFAULT '0' COMMENT '封禁状态（0表示正常，1表示封禁）',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'shareNovel'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-14 14:35:42
