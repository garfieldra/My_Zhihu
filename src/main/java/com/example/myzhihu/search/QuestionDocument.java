package com.example.myzhihu.search;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "questions")
public class QuestionDocument {

    @Id
    private Long id;

//    @Field(type = FieldType.Text) // 暂时使用默认分析器
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;

//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
//   @Field(type = FieldType.Text)
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;

//    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
//    @Field(type = FieldType.Text)
   @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String username;

    @Field(type = FieldType.Date)
    private Long createdAt;

    public QuestionDocument() {}

    public QuestionDocument(Long id, String title, String content, String username, Long createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

}
