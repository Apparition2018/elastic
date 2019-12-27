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
```
```
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
## MySQL 与 ES 数据同步
- 全量：第一次同步的数据
- 增量：之后新增/修改/删除的数据  
>### 开源中间件
>- MySQL binlog 
>    - [alibaba/canal](https://github.com/alibaba/canal)
>    - [go-mysql-elasticsearch](https://github.com/siddontang/go-mysql-elasticsearch)
>- logstash (ES 官方)
>   - 配置 mysql.conf
>   ```
>   input {
>    	jdbc {
>    		jdbc_driver_library => "D:\\apache-maven-3.5.4\\repository\\mysql\\mysql-connector-java\\5.1.47\\mysql-connector-java-5.1.47.jar"
>    		jdbc_driver_class => "com.mysql.jdbc.Driver"
>    		jdbc_connection_string => "jdbc:mysql://127.0.0.1:3306/es"
>    		jdbc_user => "root"
>    		jdbc_password => "root"
>    		# 定时任务，多久执行一次查询，默认一分钟，如果想要没有延迟，可以使用 schedule => "* * * * * *"
>    		schedule => "* * * * *"
>    		# 清空上次的 sql_last_value 记录
>    		clean_up => true
>    		# 你要执行的语句
>    		statement => "SELECT * FROM t_blog WHERE update_time > :sql_last_value AND update_time < NOW() ORDER BY update_time desc"
>    	}
>    }
>    
>    output {
>    	elasticsearch {
>    		hosts => ["127.0.0.1:9200"]
>    		index => "blog"
>    		document_id => "%{id}"
>    	}
>    }
>   ```
>   - 执行脚本：.../bin: logstash -f ../config/mysql.conf
>---
## ES 分词器
1. standard: 支持中文，采用单子切分
2. simple: 通过非字母字符切分文本，将词汇单元统一为小写形式，会去除掉数字类型字符
3. whitespace: 不支持中文，仅仅去除空格
4. language: 不支持中文
```
英文分词                 POST    _analyze
                        {
                          "analyzer": "standard",
                          "text": "hello world"
                        }
结果                      {
                          "tokens": [
                            {
                              "token": "hello",
                              "start_offset": 0,
                              "end_offset": 5,
                              "type": "<ALPHANUM>",
                              "position": 0
                            },
                            {
                              "token": "world",
                              "start_offset": 6,
                              "end_offset": 11,
                              "type": "<ALPHANUM>",
                              "position": 1
                            }
                          ]
                        }
```
```
中文分词                POST    _analyze
                        {
                          "analyzer": "standard",
                          "text": "我是好人"
                        }
结果                      {
                          "tokens": [
                            {
                              "token": "中",
                              "start_offset": 0,
                              "end_offset": 1,
                              "type": "<IDEOGRAPHIC>",
                              "position": 0
                            },
                            {
                              "token": "国",
                              "start_offset": 1,
                              "end_offset": 2,
                              "type": "<IDEOGRAPHIC>",
                              "position": 1
                            },
                            {
                              "token": "人",
                              "start_offset": 2,
                              "end_offset": 3,
                              "type": "<IDEOGRAPHIC>",
                              "position": 2
                            }
                          ]
                        }
```
>### [elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik)
>目前 ES 开源社区对于中文分词支持最好的插件  
>拷贝和解压 release 下的文件到 elasticsearch 插件目录, 如: plugins/ik， 重启 elasticsearch
>1. ik_smart
>   ```
>   分词                  POST    _analyze
>                           {
>                             "analyzer": "ik_smart",
>                             "text": "我是好人"
>                           }
>   结果                      {
>                             "tokens": [
>                               {
>                                 "token": "中国人",
>                                 "start_offset": 0,
>                                 "end_offset": 3,
>                                 "type": "CN_WORD",
>                                 "position": 0
>                               }
>                             ]
>                           }                   
>   ```
>2. ik_max_word
>   ```
>   分词                  POST    _analyze
>                           POST _analyze
>                           {
>                             "analyzer": "ik_max_word",
>                             "text": "中国人"
>                           }
>   结果                      {
>                             "tokens": [
>                               {
>                                 "token": "中国人",
>                                 "start_offset": 0,
>                                 "end_offset": 3,
>                                 "type": "CN_WORD",
>                                 "position": 0
>                               },
>                               {
>                                 "token": "中国",
>                                 "start_offset": 0,
>                                 "end_offset": 2,
>                                 "type": "CN_WORD",
>                                 "position": 1
>                               },
>                               {
>                                 "token": "国人",
>                                 "start_offset": 1,
>                                 "end_offset": 3,
>                                 "type": "CN_WORD",
>                                 "position": 2
>                               }
>                             ]
>                           }
>   ```
>- 自定义词典：在 config/IKAnalyzer.cfg.xml 中配置自定义扩展文件 <entry key="ext_dict">ext_dict.dic</entry>
>---