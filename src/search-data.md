```
PUT 127.0.0.1:9200/book
{
    "mappings": {
        "novel": {
            "properties": {
                "word_count": {
                    "type": "integer"
                },
                "author": {
                    "type": "keyword"
                },
                "title": {
                    "type": "text"
                },
                "publish_date": {
                    "type": "date",
                    "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
                }
            }
        }
    }
}
```
---
```
PUT 127.0.0.1:9200/book/novel/1
{
	"author": "张三",
	"title": "移魂大法",
	"word_count": 1000,
	"publish_date": "2000-10-01"
}
PUT 127.0.0.1:9200/book/novel/2
{
	"author": "李三",
	"title": "Java入门",
	"word_count": 2000,
	"publish_date": "2010-10-01"
}
PUT 127.0.0.1:9200/book/novel/3
{
	"author": "张四",
	"title": "Python入门",
	"word_count": 2000,
	"publish_date": "2005-10-01"
}
PUT 127.0.0.1:9200/book/novel/4
{
	"author": "李四",
	"title": "ElasticSearch大法好",
	"word_count": 1000,
	"publish_date": "2017-08-01"
}
PUT 127.0.0.1:9200/book/novel/5
{
	"author": "王五",
	"title": "菜谱",
	"word_count": 5000,
	"publish_date": "2002-10-01"
}
PUT 127.0.0.1:9200/book/novel/6
{
	"author": "赵六",
	"title": "剑谱",
	"word_count": 10000,
	"publish_date": "1997-01-01"
}
PUT 127.0.0.1:9200/book/novel/7
{
	"author": "张三丰",
	"title": "太极拳",
	"word_count": 1000,
	"publish_date": "1997-01-01"
}
PUT 127.0.0.1:9200/book/novel/8
{
	"author": "瓦力",
	"title": "ElasticSearch入门",
	"word_count": 3000,
	"publish_date": "2017-08-20"
}
PUT 127.0.0.1:9200/book/novel/9
{
	"author": "很胖的瓦力",
	"title": "ElasticSearch精通",
	"word_count": 3000,
	"publish_date": "2017-08-15"
}
PUT 127.0.0.1:9200/book/novel/10
{
	"author": "牛魔王",
	"title": "芭蕉扇",
	"word_count": 1000,
	"publish_date": "2000-10-01"
}
PUT 127.0.0.1:9200/book/novel/11
{
	"author": "孙悟空",
	"title": "七十二变",
	"word_count": 1000,
	"publish_date": "2000-10-01"
}
PUT 127.0.0.1:9200/book/novel/12
{
	"author": "瓦力",
	"title": "瓦力教我们学ElasticSearch",
	"word_count": 1000,
	"publish_date": "2017-08-01"
}
```