package com.example.feedmicroservice.Services;


import com.example.feedmicroservice.DTO.GuidelineDTO;
import com.example.feedmicroservice.DTO.NewsDTO;
import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.Feign.UserClient;
import com.example.feedmicroservice.Models.NewsPost;
import com.example.feedmicroservice.Repositories.NewsRepository;
import com.example.feedmicroservice.Threads.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final UserClient userClient;
    private final MinioService minioService;

    public NewsService( NewsRepository newsRepository, UserClient userClient, MinioService minioService) {
        this.newsRepository = newsRepository;
        this.userClient = userClient;
        this.minioService = minioService;
    }


    public NewsPost createNewsPost(NewsPost newsPost , MultipartFile file) {
        newsPost.setUserId(UserContextHolder.getUserInfo().getId());
        newsPost.setMedia(minioService.uploadImage(file));
        System.out.println(newsPost.getMedia());
        return newsRepository.save(newsPost);
    }

    public void deleteNewsById(Long postId){
        newsRepository.deleteById(postId);
    }

    public NewsDTO getNewsPostById(Long postId) {
        Optional<NewsPost> newsPost = newsRepository.findById(postId);

        UserDTO author = userClient.getUserInfo(newsPost.get().getUserId());

        return new NewsDTO(
                newsPost.get().getId(),
                newsPost.get().getTitle(),
                newsPost.get().getBody(),
                author,
                newsPost.get().getTags(),
                newsPost.get().getMedia()
        );
    }

    public List<NewsDTO> getAllNewsPosts() {
        List<NewsPost> newsPosts = newsRepository.findAll();
        return newsPosts.stream()
                .map(this::mapNewsPostToDTO)
                .collect(Collectors.toList());
    }

    private NewsDTO mapNewsPostToDTO(NewsPost newsPost) {
        UserDTO author = userClient.getUserInfo(newsPost.getUserId());

        return new NewsDTO(
                newsPost.getId(),
                newsPost.getTitle(),
                newsPost.getBody(),
                author,
                newsPost.getTags(),
                newsPost.getMedia()
        );
    }





}
