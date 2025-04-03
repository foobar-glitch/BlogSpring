package com.example.blog.mongodb;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "entries")
public class BlogEntry {
    @Id
    private ObjectId id;
    private ObjectId blogId;
    private LocalDateTime createdAt;
    private String title;
    private String body;
    private String author;

    // Default constructor
    public BlogEntry() {}

    // Constructor with fields
    public BlogEntry(ObjectId blogId, LocalDateTime createdAt, String title, String body, String author) {
        this.id = new ObjectId();
        this.blogId = blogId;
        this.createdAt = createdAt;
        this.title = title;
        this.body = body;
        this.author = author;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getBlogId() {
        return blogId.toString();
    }

    public void setBlogId(ObjectId blogId) {
        this.blogId = blogId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }




}
