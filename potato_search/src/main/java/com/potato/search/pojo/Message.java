package com.potato.search.pojo;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.Date;

/**
 * 资讯实体类
 */
@Document(type = "message",indexName = "potato_message") //索引库，一行记录对应一个文档
public class Message implements Serializable {

    @Id
    private String id;//ID

    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word") //这一列的值索引，2用这种分词器存，3用这个查
    private String title;//标题
    @Field(index = true,analyzer="ik_max_word",searchAnalyzer="ik_max_word") //这一列的值索引
    private String content;//资讯正文

    private String state;//审核状态

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

//是否索引，看该field是否能被搜索；
//是否分词，看搜索的时候是整体匹配还是单词匹配
//是否存储，是否在页面上显示