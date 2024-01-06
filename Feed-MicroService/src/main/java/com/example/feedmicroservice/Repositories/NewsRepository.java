package com.example.feedmicroservice.Repositories;

import com.example.feedmicroservice.Models.NewsPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsPost,Long> {
}
