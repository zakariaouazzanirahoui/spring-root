package com.example.feedmicroservice.Services;

import com.example.feedmicroservice.DTO.*;
import com.example.feedmicroservice.Feign.UserClient;
import com.example.feedmicroservice.Models.Comment;
import com.example.feedmicroservice.Models.HelpPost;
import com.example.feedmicroservice.Repositories.HelpRepository;
import com.example.feedmicroservice.Threads.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HelpService {

    private final  UserClient userClient;
    private final HelpRepository helpRepository;
    private final MinioService minioService;

    public HelpService(UserClient userClient,HelpRepository helpRepository ,MinioService minioService ){
        this.helpRepository = helpRepository;
        this.userClient = userClient;
        this.minioService = minioService ;
    }


    public List<HelpPostWithUserDTO> getAllHelpPosts() {
        List<HelpPost> helpPosts = helpRepository.findAll();
        return helpPosts.stream()
                .map(this::mapHelpPostToDTO)
                .collect(Collectors.toList());
    }

    private HelpPostWithUserDTO mapHelpPostToDTO(HelpPost helpPost) {
        UserDTO author = userClient.getUserInfo(helpPost.getUserId());
        List<CommentDTO> commentsWithUser = getCommentsWithUser(helpPost.getComments(), helpPost.getId());

        return new HelpPostWithUserDTO(
                helpPost.getId(),
                helpPost.getTitle(),
                helpPost.getBody(),
                helpPost.isStillNeeded(),
                author,
                commentsWithUser,
                helpPost.getMedia()
        );
    }

    private List<CommentDTO> getCommentsWithUser(List<Comment> comments, Long postId) {
        Set<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());

        Map<Long, UserDTO> userMap = userClient.getUsersInfo(userIds);

        return comments.stream()
                .map(comment -> {
                    UserDTO commentAuthor = userMap.get(comment.getUserId());
                    return new CommentDTO(comment.getId(), comment.getText(), commentAuthor, postId, comment.getUserId());
                })
                .collect(Collectors.toList());
    }

    public HelpPost createHelpPost(HelpPost helpPost , MultipartFile file) {
        helpPost.setUserId(UserContextHolder.getUserInfo().getId());
        helpPost.setMedia(minioService.uploadImage(file));
        System.out.println(helpPost.getMedia());
        return helpRepository.save(helpPost);
    }

    public HelpPostWithUserDTO getHelpPostById(Long postId) {
        Optional<HelpPost> helpPost = helpRepository.findById(postId);

        UserDTO author = userClient.getUserInfo(helpPost.get().getUserId());
        List<CommentDTO> commentsWithUser = getCommentsWithUser(helpPost.get().getComments(), helpPost.get().getId());

         return new HelpPostWithUserDTO(
                helpPost.get().getId(),
                helpPost.get().getTitle(),
                helpPost.get().getBody(),
                helpPost.get().isStillNeeded(),
                author,
                commentsWithUser, helpPost.get().getMedia()
        );
    }

   public void deleteHelpById(Long postId){
           helpRepository.deleteById(postId);
   }


}
