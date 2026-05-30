CREATE DATABASE IF NOT EXISTS shareNovel DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE shareNovel;

CREATE TABLE IF NOT EXISTS `Admin` (
    `aid` INT AUTO_INCREMENT PRIMARY KEY,
    `aname` VARCHAR(50) NOT NULL UNIQUE,
    `pwd` VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS `User` (
    `uid` INT AUTO_INCREMENT PRIMARY KEY,
    `uname` VARCHAR(50) NOT NULL UNIQUE,
    `pwd` VARCHAR(255) NOT NULL,
    `createTime` VARCHAR(30),
    `banned` INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `Novel` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `content` LONGTEXT,
    `createTime` VARCHAR(30),
    `uid` INT NOT NULL,
    `views` INT DEFAULT 0,
    `state` INT DEFAULT 0,
    INDEX `idx_uid` (`uid`),
    INDEX `idx_state` (`state`)
);

CREATE TABLE IF NOT EXISTS `Comments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `nid` INT NOT NULL,
    `uid` INT NOT NULL,
    `content` TEXT,
    `state` INT DEFAULT 0,
    INDEX `idx_nid` (`nid`),
    INDEX `idx_uid` (`uid`)
);

CREATE TABLE IF NOT EXISTS `Collection` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `uid` INT NOT NULL,
    `nid` INT NOT NULL,
    `createTime` VARCHAR(30),
    INDEX `idx_uid` (`uid`),
    UNIQUE INDEX `uk_uid_nid` (`uid`, `nid`)
);

CREATE TABLE IF NOT EXISTS `CommentsLike` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `cid` INT NOT NULL,
    `uid` INT NOT NULL,
    `createTime` VARCHAR(30),
    INDEX `idx_cid` (`cid`),
    UNIQUE INDEX `uk_cid_uid` (`cid`, `uid`)
);

CREATE TABLE IF NOT EXISTS `Label` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS `NovelLabel` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `nid` INT NOT NULL,
    `lid` INT NOT NULL,
    INDEX `idx_nid` (`nid`),
    INDEX `idx_lid` (`lid`),
    UNIQUE INDEX `uk_nid_lid` (`nid`, `lid`)
);

CREATE TABLE IF NOT EXISTS `AdminToNovel` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `aid` INT NOT NULL,
    `nid` INT NOT NULL,
    `createTime` VARCHAR(30),
    `record` VARCHAR(500),
    INDEX `idx_aid` (`aid`),
    INDEX `idx_nid` (`nid`)
);

CREATE TABLE IF NOT EXISTS `AdminToComments` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `aid` INT NOT NULL,
    `cid` INT NOT NULL,
    `createTime` VARCHAR(30),
    `record` VARCHAR(500),
    INDEX `idx_aid` (`aid`),
    INDEX `idx_cid` (`cid`)
);

CREATE TABLE IF NOT EXISTS `AdminToUser` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `aid` INT NOT NULL,
    `uid` INT NOT NULL,
    `createTime` VARCHAR(30),
    `record` VARCHAR(500),
    INDEX `idx_aid` (`aid`),
    INDEX `idx_uid` (`uid`)
);
