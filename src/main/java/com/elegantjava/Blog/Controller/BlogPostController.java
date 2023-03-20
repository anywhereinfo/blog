package com.elegantjava.Blog.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elegantjava.Blog.model.BlogPost;
import com.elegantjava.Blog.repository.BlogPostRepository;

@RestController
@RequestMapping("/blog")
public class BlogPostController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @PostMapping
    public ResponseEntity<BlogPost> createBlogPost(@RequestBody BlogPost blogPost) {
    	System.out.println("-----------------------");
    	System.out.println(blogPost);
        BlogPost savedBlogPost = blogPostRepository.save(blogPost);
        return new ResponseEntity<>(savedBlogPost, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getBlogPostById(@PathVariable String id) {
        Optional<BlogPost> optionalBlogPost = blogPostRepository.findById(id);
        if (optionalBlogPost.isPresent()) {
            BlogPost blogPost = optionalBlogPost.get();
            return new ResponseEntity<>(blogPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPost> updateBlogPost(@PathVariable String id, @RequestBody BlogPost blogPost) {
        Optional<BlogPost> optionalBlogPost = blogPostRepository.findById(id);
        if (optionalBlogPost.isPresent()) {
            BlogPost existingBlogPost = optionalBlogPost.get();
            existingBlogPost.setTitle(blogPost.getTitle());
            existingBlogPost.setAuthor(blogPost.getAuthor());
            existingBlogPost.setContent(blogPost.getContent());
            existingBlogPost.setPublishDate(blogPost.getPublishDate());
            existingBlogPost.setTags(blogPost.getTags());

            BlogPost savedBlogPost = blogPostRepository.save(existingBlogPost);
            return new ResponseEntity<>(savedBlogPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<BlogPost>> searchBlogPosts(@RequestParam(required = false) String title,
                                                           @RequestParam(required = false) List<String> tags,
                                                           @RequestParam(required = false) String body) {
        List<BlogPost> blogPosts;
        if (title != null && !title.isEmpty()) {
            blogPosts = blogPostRepository.findByTitleContaining(title);
        } else if (tags != null && !tags.isEmpty()) {
            blogPosts = blogPostRepository.findByTagsIn(tags);
        } else if (body != null && !body.isEmpty()) {
        	blogPosts = blogPostRepository.findByBodyContainingIgnoreCase(body);
        } else {
            blogPosts = new ArrayList<>();
        }
        return new ResponseEntity<>(blogPosts, HttpStatus.OK);
    }
}
