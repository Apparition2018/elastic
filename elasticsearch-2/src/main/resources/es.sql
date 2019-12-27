/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50727
 Source Host           : localhost:3306
 Source Schema         : es

 Target Server Type    : MySQL
 Target Server Version : 50727
 File Encoding         : 65001

 Date: 27/12/2019 15:28:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_blog
-- ----------------------------
DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE `t_blog`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `title` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '博客标题',
  `author` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '博客作者',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '博客内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_blog
-- ----------------------------
INSERT INTO `t_blog` VALUES (1, 'Springboot 为什么这么容易', 'bywind', '没错 Springboot 就是这么简单', '2019-12-19 11:56:26', '2019-12-19 11:56:26');
INSERT INTO `t_blog` VALUES (2, 'ElasticSearch 从精通到入门', 'bywind', 'ElasticSearch 从精通到入门', '2019-12-19 11:56:26', '2019-12-19 11:56:26');
INSERT INTO `t_blog` VALUES (3, 'Springboot 中 Redis 的配置', 'bywind', 'Spring Boot 整合 Redis', '2019-12-19 11:56:26', '2019-12-19 11:56:26');
INSERT INTO `t_blog` VALUES (4, 'Springboot 优化技巧', 'bywind', '这两天启动项目太慢了', '2019-12-19 11:56:26', '2019-12-19 11:56:26');
INSERT INTO `t_blog` VALUES (5, 'Springboot 消息队列', 'bywind', 'RabbitMQ 在 Spring Boot 中的使用', '2019-12-19 11:56:26', '2019-12-19 11:56:26');
INSERT INTO `t_blog` VALUES (6, 'Docket Compose + Springboot', 'bywind', '我知道大家...', '2019-12-19 11:56:26', '2019-12-19 11:56:26');

SET FOREIGN_KEY_CHECKS = 1;
