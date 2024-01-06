package com.example.feedmicroservice.Repositories;

import com.example.feedmicroservice.Models.GuidelinePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuidelineRepository extends JpaRepository<GuidelinePost, Long> {
}
