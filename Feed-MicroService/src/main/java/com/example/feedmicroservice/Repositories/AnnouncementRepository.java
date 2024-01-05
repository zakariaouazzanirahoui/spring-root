package com.example.feedmicroservice.Repositories;

import com.example.feedmicroservice.Models.AnnouncementPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementPost, Long> {
}
