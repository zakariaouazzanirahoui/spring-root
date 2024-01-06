package com.example.feedmicroservice.Services;

import com.example.feedmicroservice.DTO.CommentDTO;
import com.example.feedmicroservice.DTO.HelpPostWithUserDTO;
import com.example.feedmicroservice.DTO.UserDTO;
import com.example.feedmicroservice.DTO.VolunteeringPostWithUserDTO;
import com.example.feedmicroservice.Feign.UserClient;
import com.example.feedmicroservice.Models.Comment;
import com.example.feedmicroservice.Models.HelpPost;
import com.example.feedmicroservice.Models.VolunteeringPost;
import com.example.feedmicroservice.Repositories.HelpRepository;
import com.example.feedmicroservice.Repositories.VolunteeringRepository;
import com.example.feedmicroservice.Threads.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.stream.Collectors;



import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
public class VolunteeringService {


        private final UserClient userClient;
        private final VolunteeringRepository volunteeringRepository;
        private final MinioService minioService;
        public VolunteeringService(UserClient userClient,VolunteeringRepository volunteeringRepository, MinioService minioService ){
            this.volunteeringRepository = volunteeringRepository;
            this.userClient = userClient;
            this.minioService = minioService ;
        }

        public Optional<VolunteeringPost> findById(Long posId){
            return volunteeringRepository.findById(posId);
        }
        public List<VolunteeringPostWithUserDTO> getAllVolunteeringPosts() {
            List<VolunteeringPost> volunteeringPosts  = volunteeringRepository.findAll();
            return volunteeringPosts.stream()
                    .map(this::mapVolunteeringPostToDTO)
                    .collect(Collectors.toList());
        }

        private VolunteeringPostWithUserDTO mapVolunteeringPostToDTO(VolunteeringPost volunteeringPost) {
            UserDTO author = userClient.getUserInfo(volunteeringPost.getUserId());
            List<CommentDTO> commentsWithUser = getCommentsWithUser(volunteeringPost.getComments(), volunteeringPost.getId());

            return new VolunteeringPostWithUserDTO(
                    volunteeringPost.getId(),
                    volunteeringPost.getTitle(),
                    volunteeringPost.getBody(),
                    volunteeringPost.isConsumed(),
                    author,
                    commentsWithUser,
                    volunteeringPost.getMedia()
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

        public VolunteeringPost createVolunteeringPost(VolunteeringPost volunteeringPost, MultipartFile file) {

            volunteeringPost.setMedia(minioService.uploadImage(file));
            volunteeringPost.setUserId(UserContextHolder.getUserInfo().getId());
            return volunteeringRepository.save(volunteeringPost);
        }

        public VolunteeringPostWithUserDTO getVolunteeringPostById(Long postId) {
            Optional<VolunteeringPost> volunteeringPost = volunteeringRepository.findById(postId);

            UserDTO author = userClient.getUserInfo(volunteeringPost.get().getUserId());
            List<CommentDTO> commentsWithUser = getCommentsWithUser(volunteeringPost.get().getComments(), volunteeringPost.get().getId());

            return new VolunteeringPostWithUserDTO(
                    volunteeringPost.get().getId(),
                    volunteeringPost.get().getTitle(),
                    volunteeringPost.get().getBody(),
                    volunteeringPost.get().isConsumed(),
                    author,
                    commentsWithUser,
                    volunteeringPost.get().getMedia()
            );
        }

        public void deleteVolunteeringById(Long postId){
            volunteeringRepository.deleteById(postId);
        }



}
