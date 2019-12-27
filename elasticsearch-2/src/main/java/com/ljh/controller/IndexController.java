package com.ljh.controller;

import com.ljh.entity.mysql.MysqlBlog;
import com.ljh.repository.mysql.MysqlBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * IndexController
 *
 * @author Arsenal
 * created on 2019/12/27 13:03
 */
@Controller
public class IndexController {
    
    private final MysqlBlogRepository mysqlBlogRepository;

    @Autowired
    public IndexController(MysqlBlogRepository mysqlBlogRepository) {
        this.mysqlBlogRepository = mysqlBlogRepository;
    }

    @RequestMapping("/")
    public String index() {
        List<MysqlBlog> all = mysqlBlogRepository.findAll();
        System.out.println(all.size());
        return "index.html";
    }
}
