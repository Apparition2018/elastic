package com.ljh.entity.es;

import lombok.Data;
import org.elasticsearch.client.ml.job.config.DataDescription;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * EsBlog
 *
 * @author Arsenal
 * created on 2019/12/27 13:42
 */
@Data
@Document(indexName = "blog", type = "doc", useServerConfiguration = true, createIndex = false)
public class EsBlog {

    @Id
    private Integer id;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String author;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Date, format = DateFormat.custom, 
            pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")     
    private Date createTime;
    @Field(type = FieldType.Date, format = DateFormat.custom, 
            pattern = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis") 
    private Date updateTime;
}
