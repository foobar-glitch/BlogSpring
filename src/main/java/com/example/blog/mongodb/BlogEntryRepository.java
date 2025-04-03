package com.example.blog.mongodb;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BlogEntryRepository extends MongoRepository<BlogEntry, String> {

    // Custom query to find entries by blogId
    List<BlogEntry> findByBlogId(ObjectId blogId);

    void deleteByBlogId(ObjectId blogId);
}
