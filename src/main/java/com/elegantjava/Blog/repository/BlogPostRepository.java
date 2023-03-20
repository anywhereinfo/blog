package com.elegantjava.Blog.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.elegantjava.Blog.model.BlogPost;

public interface BlogPostRepository extends ElasticsearchRepository<BlogPost, String> {
    List<BlogPost> findByTitleContaining(String title);
    List<BlogPost> findByTagsIn(List<String> tags);
    @Query("{ \"match\": { \"body\": { \"query\": \"?0\", \"operator\": \"and\" } } }")
    List<BlogPost> findByBodyContainingIgnoreCase(String query);

}
