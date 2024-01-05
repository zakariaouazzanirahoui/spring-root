package com.example.feedmicroservice.Repositories;
import com.example.feedmicroservice.Models.HelpPost;
import com.example.feedmicroservice.Models.VolunteeringPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteeringRepository  extends JpaRepository<VolunteeringPost, Long> {
}
