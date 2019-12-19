package com.ljh;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Application
 *
 * @author NL-PC001
 * created on 2019/12/16 14:54
 */
@SpringBootApplication
@RestController
public class Application {

    private final TransportClient client;

    @Autowired
    public Application(TransportClient client) {
        this.client = client;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/get/book/novel")
    public ResponseEntity<Map<String, Object>> get(@RequestParam(name = "id", defaultValue = "") String id) {
        if (id.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        GetResponse result = this.client.prepareGet("book", "novel", id).get();
        if (!result.isExists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.getSource(), HttpStatus.OK);
    }

    @PostMapping("add/book/novel")
    public ResponseEntity<String> add(
            @RequestParam(name = "title") String title,
            @RequestParam(name = "author") String author,
            @RequestParam(name = "word_count") String wordCount,
            @RequestParam(name = "publish_date") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date publishDate) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("title", title)
                    .field("author", author)
                    .field("word_count", wordCount)
                    .field("publish_date", publishDate.getTime())
                    .endObject();
            IndexResponse result = this.client.prepareIndex("book", "novel")
                    .setSource(builder)
                    .get();
            return new ResponseEntity<>(result.getId(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("delete/book/novel")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestParam(name = "id") String id) {
        DeleteResponse result = this.client.prepareDelete("book", "novel", id).get();
        return new ResponseEntity<>(result.getResult().toString(), HttpStatus.OK);
    }

    /**
     * PUT localhost:8080/update/book/novel
     * {
     * "author": "瓦力",
     * "id": 1
     * }
     */
    @PutMapping("update/book/novel")
    @ResponseBody
    public ResponseEntity<String> update(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author) {
        UpdateRequest updateRequest = new UpdateRequest("book", "novel", id);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
            if (title != null) {
                builder.field("title", title);
            }
            if (author != null) {
                builder.field("author", author);
            }
            builder.endObject();
            updateRequest.doc(builder);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            UpdateResponse result = this.client.update(updateRequest).get();
            return new ResponseEntity<>(result.getResult().toString(), HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("query/book/novel")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> query(
            @RequestParam(name = "author", required = false) String author,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "gt_word_count", defaultValue = "0") int gtWordCount,
            @RequestParam(name = "lt_word_count", required = false) Integer ltWordCount) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (author != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("author", author));
        }
        if (title != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("title", title));
        }

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("word_count").from(gtWordCount);
        if (ltWordCount != null && ltWordCount > 0) {
            rangeQueryBuilder.to(ltWordCount);
        }

        boolQueryBuilder.filter(rangeQueryBuilder);

        SearchRequestBuilder searchRequestBuilder = this.client.prepareSearch("book")
                .setTypes("novel")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(10);
        System.out.println("searchRequestBuilder = " + searchRequestBuilder);

        SearchResponse searchResponse = searchRequestBuilder.get();
        List<Map<String, Object>> result = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            result.add(hit.getSource());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
