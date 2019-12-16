## ElasticSearch 简介
- 基于 Apache Lucene 构建的开源搜索引擎
- 采用 Java 编写，提供简单易用很高的 RESTFul API
- 轻松的横向发展，可支持 PB 级的结构化或非结构化数据处理
---
## 应用场景
- 海量数据分析引擎
- 站内搜索引擎
- 数据仓库
---
## 安装 
- [单实例安装](https://www.cnblogs.com/nicknailo/p/9014952.html)
- Head 插件安装
- 分布式安装
---
## 配置修改
- 跨域：
    - http.cors.enabled: true
    - http.cors.allow-origin: "*"
- 集群：
    - 主：
        - cluster.name: wali
        - node.name: master
        - node.master: true
        - network.host: 127.0.0.1
    - 从：
        - cluster.name: wali
        - node.name: slave1
        - network.host: 127.0.0.1
        - http.port: 9201
        - discovery.zen.ping.unicast.hosts: ["127.0.0.1"]
---
## 基础概念
- 集群和节点
- 索引：含有相同属性的文档集合 (数据库)
- 类型：索引可以定义一个或多个类型，通常定义拥有相同字段的文档为一个类型，文档必须属于一个类型 (表)
- 文档：可以被索引的基本数据单位 (行记录)
- 分片：shard，每个索引都有多个分片，每个分片是一个 Lucene 索引，创建索引时默认创建5个分片
- 备份：replica，拷贝一份分片就完成了分片的备份，创建索引时默认创1个备份
---
## 基本用法
- API 基本格式：http://<ip>:<port>/<索引>/<类型>/<文档id>
---
>### 创建索引
>- 非结构化创建："mappings": {}
>- 结构化创建
>```
>PUT 127.0.0.1:9200/people
>   {
>  	    "settings": {
>  	    	"number_of_shards": 3,
>  	    	"number_of_replicas": 1
>  	    },
>  	    "mappings": {
>  	    	"man": {
>  	    		"properties": {
>  	    			"name": {
>  	    				"type": "text"
>  	    			},
>  	    			"country": {
>  	    				"type": "keyword"
>  	    			},
>  	    			"age": {
>  	    				"type": "integer"
>  	    			},
>  	    			"date": {
>  	    				"type": "date",
>  	    				"format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
>  	    			}
>  	    		}
>  	    	},
>  	    	"woman": {}
>  	    }
>   }
>```
>---
>### 插入             
>- 指定文档 id 插入：PUT 127.0.0.1:9200/people/man/1
>- 自动产生文档 id 插入
>```
>PUT 127.0.0.1:9200/people/man
>   {
>       "name": "瓦力",
>       "country": "china",
>       "age": 30,
>       "date": "1987-03-07"
>   }
>POST 127.0.0.1:9200/people/man
>   {
>       "name": "超重瓦力",
>       "country": "china",
>       "age": 40,
>       "date": "1977-03-07"
>   }
>```
>---
>### 修改
>- 直接修改文档
>```
>POST 127.0.0.1:9200/people/man/1/_update
>   {
> 	    "doc": {
> 	    	"name": "谁是瓦力"
> 	    }
>   }
>```
>- 脚本修改文档
>```
>POST 127.0.0.1:9200/people/man/1/_update
>   {
>   	"script": {
>   		"lang": "painless",
>   		"inline": "ctx._source.age += 10"
>   	}
>   }
>POST 127.0.0.1:9200/people/man/1/_update
>   {
>   	"script": {
>   		"lang": "painless",
>   		"inline": "ctx._source.age = params.age",
>   		"params": {
>   			"age": 100
>   		}
>   	}
>   }
>```
>---
>### 删除
>- 删除文档：DELETE 127.0.0.1:9200/people/man/1/
>- 删除索引：DELETE 127.0.0.1:9200/people
>---
>### 查询
>- 简单查询：GET 127.0.0.1:9200/book/novel/1
>- 条件查询：POST 127.0.0.1:9200/book/_search
>```
>查询所有：
>   {
>   	"query": {
>   		"match_all": {}
>   	},
>   	"from": 1,
>   	"size": 1
>   }
>关键词查询：
>   {
>       "query": {
>           "match": {
>               "title": "ElasticSearch"
>           }
>       },
>       "sort": [
>           {
>               "publish_date": {
>                   "order": "desc"
>               }
>           }
>       ]
>   }
>```
>- 聚合查询
>```
>   {
>   	"aggs": {
>   		"group_by_word_count": {
>   			"terms": {
>   				"field": "word_count"
>   			}
>   		},
>   		"group_by_publish_date": {
>   			"terms": {
>   				"field": "publish_date"
>   			}
>   		}
>   	}
>   }
>
>   {
>   	"aggs": {
>   		"grades_word_count": {
>   			"stats": {
>   				"field": "word_count"
>   			}
>   		}
>   	}
>   }
>```
>---
## 高级查询
>### 子条件查询
>特定字段查询所指特定值
>- Query context: 在查询过程中，除了判断文档是否满足查询条件外，ES 还会计算一个 _score 来标识匹配的程度，旨在判断目标文档和查询条件匹配得有多好
>   - 全文本查询：针对文本类型数据
>   ```
>   模糊匹配：
>       {
>       	"query": {
>       		"match": {
>       			"author": "瓦力"
>       		}
>       	}
>       }
>   词语匹配：
>       {
>       	"query": {
>       		"match_phrase": {
>       			"title": "ElasticSearch入门"
>       		}
>       	}
>       }
>   多字段匹配：
>       {
>           "query": {
>               "multi_match": {
>                   "query": "瓦力",
>                   "fields": ["author", "title"]
>               }
>           }
>       }
>   语法查询：
>       {
>           "query": {
>           	"query_string": {
>           		"query": "(ElasticSearch AND 大法) OR Python"
>           	}
>           }
>       }
>       
>       {
>           "query": {
>           	"query_string": {
>           		"query": "瓦力 OR ElasticSearch",
>           		"fields": ["author", "title"]
>           	}
>           }
>       }
>   ```
>   - 字段级别查询：针对结构化数据，如数字、日期等
>   ```
>       {
>           "query": {
>       		"term": {
>       			"word_count": 1000
>       		}
>           }
>       }
>   
>       {
>           "query": {
>       		"range": {
>       			"publish_date": {
>       				"gt": "2017-01-01",
>       				"lte": "now"
>       			}
>       		}
>           }
>       }
>   ```
>- Filter context: 在查询过程中，值判断该文档是否满足条件，只有 Yes 或者 No
>```
>   {
>       "query": {
>   		"bool": {
>   			"filter": {
>   				"term": {
>   					"word_count": 1000
>   				}
>   			}
>   		}
>       }
>   }
>```
>### 复合条件查询
>以一定的逻辑组合子条件查询
>- 固定分数查询
>```
>   {
>       "query": {
>   		"constant_score": {
>   			"filter": {
>   				"match": {
>   					"title": "ElasticSearch"
>   				}
>   			},
>   			"boost": 2
>   		}
>       }
>   }
>```
>- 布尔查询
>```
>   {
>       "query": {
>   		"bool": {
>   			"should": [
>   				{
>   					"match": {
>   						"author": "瓦力"
>   					}
>   				},
>   				{
>   					"match": {
>   						"title": "ElasticSearch"
>   					}
>   				}
>   			]
>   		}
>       }
>   }
>
>   {
>       "query": {
>           "bool": {
>               "must": [
>                   {
>                       "match": {
>                           "author": "瓦力"
>                       }
>                   },
>                   {
>                       "match": {
>                           "title": "ElasticSearch"
>                       }
>                   }
>               ],
>               "filter": [
>                   {
>                       "term": {
>                           "word_count": 1000
>                       }
>                   }
>               ]
>           }
>       }
>   }
>   
>   {
>       "query": {
>           "bool": {
>               "must_not": {
>                   "match": {
>                       "author": "瓦力"
>                   }
>               }
>           }
>       }
>   }
>```
>- more...
>---
## 实战
mvn spring-boot:run