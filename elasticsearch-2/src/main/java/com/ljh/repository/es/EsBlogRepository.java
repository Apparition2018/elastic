package com.ljh.repository.es;

import com.ljh.entity.es.EsBlog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * EsBlogRepository
 *
 * @author Arsenal
 * created on 2019/12/27 13:59
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, Integer> {
}
