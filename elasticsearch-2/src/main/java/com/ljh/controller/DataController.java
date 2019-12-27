package com.ljh.controller;

import com.ljh.entity.es.EsBlog;
import com.ljh.entity.mysql.MysqlBlog;
import com.ljh.repository.es.EsBlogRepository;
import com.ljh.repository.mysql.MysqlBlogRepository;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * DataController
 *
 * @author Arsenal
 * created on 2019/12/27 14:04
 */
@RestController
public class DataController {

    private final MysqlBlogRepository mysqlBlogRepository;

    private final EsBlogRepository esBlogRepository;

    @Autowired
    public DataController(MysqlBlogRepository mysqlBlogRepository, EsBlogRepository esBlogRepository) {
        this.mysqlBlogRepository = mysqlBlogRepository;
        this.esBlogRepository = esBlogRepository;
    }

    @GetMapping("/blogs")
    public Object blog() {
        return mysqlBlogRepository.queryAll();
    }
    
    @PostMapping("/search")
    public Object search(@RequestBody Param param) {
        HashMap<String, Object> map = new HashMap<>();
        StopWatch watch = new StopWatch();
        watch.start();
        String type = param.getType();
        if ("mysql".equalsIgnoreCase(type)) {
            List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryBlogs(param.getKeyword());
            map.put("list", mysqlBlogs);
        } else if ("es".equalsIgnoreCase(type)) {
            //  POST /blog/_search
            //  {
            //    "query": {
            //      "bool": {
            //        "should": [
            //          {
            //            "match_phrase": {
            //              "title": "springboot"
            //            }
            //          },
            //          {
            //            "match_phrase": {
            //              "content": "springboot"
            //            } 
            //          }
            //        ]
            //      }
            //    }
            //  }
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.should(QueryBuilders.matchPhraseQuery("title", param.getKeyword()));
            builder.should(QueryBuilders.matchPhraseQuery("content", param.getKeyword()));
            String builderString = builder.toString();
            System.out.println("builderString = " + builderString);
            Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
            List<EsBlog> content = search.getContent();
            map.put("list", content);
        } else {
            return "I don't understand";
        }
        watch.stop();
        long totalTimeMillis = watch.getTotalTimeMillis();
        map.put("duration", totalTimeMillis);
        return map;
    }
    
    @GetMapping("/blog/{id}")
    public Object blog(@PathVariable("id") Integer id) {
        Optional<MysqlBlog> byId = mysqlBlogRepository.findById(id);
        return byId.orElse(null);
    }

    @Data
    public static class Param {
        // mysql, es
        private String type;
        private String keyword;
    }
}
