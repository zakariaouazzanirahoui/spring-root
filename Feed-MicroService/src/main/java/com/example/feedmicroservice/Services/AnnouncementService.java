package com.example.feedmicroservice.Services;


import com.example.feedmicroservice.DTO.AnnouncementWithUserDTO;
import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.Feign.UserClient;
import com.example.feedmicroservice.Models.AnnouncementPost;
import com.example.feedmicroservice.Repositories.AnnouncementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private UserClient userClient;

    public List<AnnouncementWithUserDTO> getAllAnnouncementPosts() {
        List<AnnouncementPost> announcementPosts = announcementRepository.findAll();

        List<AnnouncementWithUserDTO> result = new ArrayList<>();

        for (AnnouncementPost announcementPost : announcementPosts) {
            UserDTO author = userClient.getUserInfo(announcementPost.getUserId());
            AnnouncementWithUserDTO dto = new AnnouncementWithUserDTO(announcementPost.getId(),
                    announcementPost.getTitle(),
                    announcementPost.getBody(),
                    author);
            result.add(dto);
        }

        return result;
    }

    public Optional<AnnouncementPost> getAnnouncementById(Long id) {
        return announcementRepository.findById(id);
    }

    public AnnouncementPost createAnnouncement(AnnouncementPost announcement) {
        return announcementRepository.save(announcement);
    }

    public AnnouncementPost updateAnnouncement(Long id, AnnouncementPost updatedAnnouncement) {
        if (announcementRepository.existsById(id)) {
            updatedAnnouncement.setId(id);
            return announcementRepository.save(updatedAnnouncement);
        }
        return null; // or throw an exception indicating resource not found
    }

    public void deleteAnnouncement(Long id) {
        announcementRepository.deleteById(id);
    }
}
