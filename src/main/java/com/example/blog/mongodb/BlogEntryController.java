package com.example.blog.mongodb;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/api")
public class BlogEntryController {

    @Autowired
    private BlogEntryService blogEntryService;

    // Create a new blog entry
    @PostMapping("/blog")
    public BlogEntry createBlogEntry(@RequestParam String title,
                                     @RequestParam String body, @RequestParam String author) {
        return blogEntryService.createBlogEntry(new ObjectId(), title, body, author);
    }

    // Get all blog entries
    @GetMapping("/blog")
    public List<BlogEntry> getAllBlogEntries() {
        return blogEntryService.getAllBlogEntries();
    }

    // Get blog entries by blogId
    @GetMapping("/blog/{blogId}")
    public List<BlogEntry> getBlogEntriesByBlogId(@PathVariable String blogId) {
        return blogEntryService.getBlogEntriesByBlogId(new ObjectId(blogId));
    }

    @DeleteMapping("/blog/{blogId}")
    public void deleteBlogEntriesByBlogId(@PathVariable String blogId) {
        blogEntryService.deleteBlogEntriesByBlogId(new ObjectId(blogId));
    }

}