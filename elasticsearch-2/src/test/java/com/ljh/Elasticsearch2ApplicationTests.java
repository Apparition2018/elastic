package com.ljh;

import com.ljh.entity.es.EsBlog;
import com.ljh.repository.es.EsBlogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;

@SpringBootTest
class Elasticsearch2ApplicationTests {
    
    private final EsBlogRepository esBlogRepository;

    @Autowired
    Elasticsearch2ApplicationTests(EsBlogRepository esBlogRepository) {
        this.esBlogRepository = esBlogRepository;
    }

    @Test
    public void contextLoads() {
    }
    
    @Test
    public void test() {
        Iterable<EsBlog> all = esBlogRepository.findAll();
        Iterator<EsBlog> iterator = all.iterator();
        EsBlog next = iterator.next();
        System.out.println("next = " + next);
    } 

}
