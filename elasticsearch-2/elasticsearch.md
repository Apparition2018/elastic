## 课程概要
![SpringBoot+ElasticSearch](https://img.mukewang.com/5df9c97f0001083f19201080.jpg)

---
## ElasticSearch vs MySQL
|Mysql|ES|
|:---:|:---:|
|Database|Index|
|Table|Type|
|Row|Document|
|Column|Field|
|Schema|Mapping|
MySQL: select * from user.user_info where name = '张三';  
ES: GET /user/user_info/_search?q=name:张三
---
## Postman 与 ElasticSearch 交互
```
创建索引                PUT     localhost:9200/person
查看索引                GET     localhost:9200/_all
删除索引                DELETE  localhost:9200/person
新建数据                PUT     localhost:9200/person/_doc/1
                                {
                                	"first_name": "John",
                                	"last_name": "Smith",
                                	"age": 25,
                                	"about": "I love to go rock climbing",
                                	"interests": ["sports", "music"]
                                }
                        PUT     localhost:9200/person/_doc/2
                                {
                                	"first_name": "Eric",
                                	"last_name": "Smith",
                                	"age": 23,
                                	"about": "I love to basketball",
                                	"interests": ["sports", "reading"]
                                }
搜索数据                GET     localhost:9200/person/_doc/1
                       GET     localhost:9200/person/_doc/_search?q=first_name:john
```
---
## Kibana 与 ElasticSearch 交互
```
should                  POST    /person/_search
                                {
                                  "query": {
                                    "bool": {
                                      "should": [
                                        {
                                          "match": {
                                            "last_name": "Smith"
                                          }
                                        }
                                      ]
                                    }
                                  }
                                }
must                    POST    /person/_search
                                {
                                  "query": {
                                    "bool": {
                                      "must": [
                                        {
                                          "match": {
                                            "last_name": "Smith"
                                          }
                                        },
                                        {
                                          "match": {
                                            "about": "basketball"
                                          }
                                        }
                                      ]
                                    }
                                  }
                                }
```
---

