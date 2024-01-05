package com.example.feedmicroservice.Repositories;

import com.example.feedmicroservice.Models.HelpPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HelpRepository  extends JpaRepository<HelpPost, Long> {
}
