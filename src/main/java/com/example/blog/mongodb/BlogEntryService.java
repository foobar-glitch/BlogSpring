package com.example.blog.mongodb;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogEntryService {

    @Autowired
    private BlogEntryRepository blogEntryRepository;

    // Create a new blog entry
    public BlogEntry createBlogEntry(ObjectId blogId, String title, String body, String author) {
        BlogEntry blogEntry = new BlogEntry(
                blogId,
                LocalDateTime.now(),  // current timestamp
                title,
                body,
                author
        );
        return blogEntryRepository.save(blogEntry);
    }

    // Get all blog entries
    public List<BlogEntry> getAllBlogEntries() {
        return blogEntryRepository.findAll();
    }

    // Get blog entries by blogId
    public List<BlogEntry> getBlogEntriesByBlogId(ObjectId blogId) {
        return blogEntryRepository.findByBlogId(blogId);
    }

    public void deleteBlogEntriesByBlogId(ObjectId blogId) {
        blogEntryRepository.deleteByBlogId(blogId);
    }

}
