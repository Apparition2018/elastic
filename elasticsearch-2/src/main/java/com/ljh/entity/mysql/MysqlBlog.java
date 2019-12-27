package com.ljh.entity.mysql;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * MysqlBlog
 * 
 * CREATE TABLE `t_blog` (
 *   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
 *   `title` varchar(60) DEFAULT NULL COMMENT '博客标题',
 *   `author` varchar(60) DEFAULT NULL COMMENT '博客作者',
 *   `content` mediumtext COMMENT '博客内容',
 *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 *   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;
 *
 * @author Arsenal
 * created on 2019/12/27 12:54
 */
@Entity
@Table(name = "t_blog")
@Data
public class MysqlBlog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String author;
    @Column(columnDefinition = "mediumtext")
    private String content;
    private Date createTime;
    private Date updateTime;
    
}
