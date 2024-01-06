package com.example.feedmicroservice.Services;


import com.example.feedmicroservice.DTO.CommentDTO;
import com.example.feedmicroservice.DTO.GuidelineDTO;
import com.example.feedmicroservice.DTO.HelpPostWithUserDTO;
import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.Feign.UserClient;
import com.example.feedmicroservice.Models.GuidelinePost;
import com.example.feedmicroservice.Models.HelpPost;
import com.example.feedmicroservice.Repositories.GuidelineRepository;
import com.example.feedmicroservice.Threads.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GuidelineService {

    private final GuidelineRepository guidelineRepository;
    private final UserClient userClient;
    private final MinioService minioService;


    public GuidelineService(GuidelineRepository guidelineRepository, UserClient userClient, MinioService minioService) {
        this.guidelineRepository = guidelineRepository;
        this.userClient = userClient;
        this.minioService = minioService;
    }

    public GuidelinePost createGuidelinePost(GuidelinePost guidelinePost , MultipartFile file) {
        guidelinePost.setUserId(UserContextHolder.getUserInfo().getId());
        guidelinePost.setMedia(minioService.uploadImage(file));
        System.out.println(guidelinePost.getMedia());
        return guidelineRepository.save(guidelinePost);
    }

    public void deleteGuidelineById(Long postId){
        guidelineRepository.deleteById(postId);
    }

    public GuidelineDTO getGuidelinePostById(Long postId) {
        Optional<GuidelinePost> guidelinePost = guidelineRepository.findById(postId);

        UserDTO author = userClient.getUserInfo(guidelinePost.get().getUserId());

        return new GuidelineDTO(
                guidelinePost.get().getId(),
                guidelinePost.get().getTitle(),
                guidelinePost.get().getBody(),
                author,
                guidelinePost.get().getTags(),
                guidelinePost.get().getMedia()
        );
    }

    public List<GuidelineDTO> getAllGuidelinePosts() {
        List<GuidelinePost> guidelinePosts = guidelineRepository.findAll();
        return guidelinePosts.stream()
                .map(this::mapGuidelinePostToDTO)
                .collect(Collectors.toList());
    }

    private GuidelineDTO mapGuidelinePostToDTO(GuidelinePost guidelinePost) {
        UserDTO author = userClient.getUserInfo(guidelinePost.getUserId());

        return new GuidelineDTO(
                guidelinePost.getId(),
                guidelinePost.getTitle(),
                guidelinePost.getBody(),
                author,
                guidelinePost.getTags(),
                guidelinePost.getMedia()
        );
    }





}
