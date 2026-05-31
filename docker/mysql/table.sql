CREATE DATABASE IF NOT EXISTS shareNovel DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shareNovel;

CREATE TABLE IF NOT EXISTS `Admin` (
    `aid` INT NOT NULL AUTO_INCREMENT COMMENT '管理员id',
    `aname` VARCHAR(255) DEFAULT NULL COMMENT '管理员名',
    `pwd` VARCHAR(255) DEFAULT NULL COMMENT '管理员密码',
    PRIMARY KEY (`aid`),
    UNIQUE KEY `aname` (`aname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理员表';

CREATE TABLE IF NOT EXISTS `User` (
    `uid` INT NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `create_time` DATETIME DEFAULT NULL COMMENT '注册时间',
    `uname` VARCHAR(255) DEFAULT NULL COMMENT '用户名',
    `pwd` VARCHAR(255) DEFAULT NULL COMMENT '用户密码',
    `banned` INT NOT NULL DEFAULT 0 COMMENT '封禁状态（0正常，1封禁）',
    PRIMARY KEY (`uid`),
    UNIQUE KEY `uname` (`uname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `Novel` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '小说id',
    `title` VARCHAR(255) DEFAULT NULL COMMENT '小说标题',
    `content` LONGTEXT COMMENT '小说正文',
    `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
    `uid` INT NOT NULL COMMENT '上传用户id',
    `views` INT DEFAULT 0 COMMENT '浏览量',
    `state` INT NOT NULL DEFAULT 0 COMMENT '状态（0审核中，-1不通过，1通过，2被举报，3违规下架）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `title` (`title`),
    KEY `user_create_novel` (`uid`),
    CONSTRAINT `user_create_novel` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说表';

CREATE TABLE IF NOT EXISTS `Comments` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `nid` INT NOT NULL COMMENT '小说id',
    `uid` INT NOT NULL COMMENT '用户id',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `state` INT NOT NULL DEFAULT 0 COMMENT '0正常，1被举报，-1封禁',
    PRIMARY KEY (`id`),
    KEY `nid` (`nid`),
    KEY `uid` (`uid`),
    CONSTRAINT `Comments_ibfk_1` FOREIGN KEY (`nid`) REFERENCES `Novel` (`id`),
    CONSTRAINT `Comments_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论表';

CREATE TABLE IF NOT EXISTS `Collection` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '收藏id',
    `uid` INT NOT NULL COMMENT '用户id',
    `nid` INT NOT NULL COMMENT '小说id',
    `create_time` DATETIME DEFAULT NULL COMMENT '收藏时间',
    PRIMARY KEY (`id`),
    KEY `uid` (`uid`),
    KEY `nid` (`nid`),
    CONSTRAINT `Collection_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`),
    CONSTRAINT `Collection_ibfk_2` FOREIGN KEY (`nid`) REFERENCES `Novel` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='收藏表';

CREATE TABLE IF NOT EXISTS `CommentsLike` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `cid` INT NOT NULL COMMENT '评论id',
    `uid` INT NOT NULL COMMENT '用户id',
    `create_time` DATETIME DEFAULT NULL COMMENT '点赞时间',
    PRIMARY KEY (`id`),
    KEY `cid` (`cid`),
    KEY `uid` (`uid`),
    CONSTRAINT `CommentsLike_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `Comments` (`id`),
    CONSTRAINT `CommentsLike_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `User` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论点赞表';

CREATE TABLE IF NOT EXISTS `Label` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT '标签id',
    `name` VARCHAR(255) DEFAULT NULL COMMENT '标签名',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='标签表';

INSERT INTO `Label` (`id`, `name`) VALUES
(1, '言情小说'), (2, '科幻小说'), (3, '现实主义小说'), (4, '浪漫主义小说'),
(5, '武侠小说'), (6, '惊悚小说'), (7, '悬疑小说'), (8, '奇幻小说'),
(9, '历史小说'), (10, '儿童文学'), (11, '其他')
ON DUPLICATE KEY UPDATE `name`=`name`;

CREATE TABLE IF NOT EXISTS `NovelLabel` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `nid` INT NOT NULL COMMENT '小说id',
    `lid` INT NOT NULL COMMENT '标签id',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小说标签表';

CREATE TABLE IF NOT EXISTS `AdminToNovel` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `aid` INT NOT NULL COMMENT '管理员id',
    `nid` INT NOT NULL COMMENT '小说id',
    `create_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `record` VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    PRIMARY KEY (`id`),
    KEY `idx_aid` (`aid`),
    KEY `idx_nid` (`nid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='审核记录表';

CREATE TABLE IF NOT EXISTS `AdminToComments` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `aid` INT NOT NULL COMMENT '管理员id',
    `cid` INT NOT NULL COMMENT '评论id',
    `create_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `record` VARCHAR(500) DEFAULT NULL COMMENT '审核意见',
    PRIMARY KEY (`id`),
    KEY `idx_aid` (`aid`),
    KEY `idx_cid` (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='评论审核记录表';

CREATE TABLE IF NOT EXISTS `AdminToUser` (
    `id` INT NOT NULL AUTO_INCREMENT COMMENT 'id',
    `aid` INT NOT NULL COMMENT '管理员id',
    `uid` INT NOT NULL COMMENT '用户id',
    `create_time` DATETIME DEFAULT NULL COMMENT '操作时间',
    `record` VARCHAR(500) DEFAULT NULL COMMENT '操作记录',
    PRIMARY KEY (`id`),
    KEY `idx_aid` (`aid`),
    KEY `idx_uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户操作记录表';
