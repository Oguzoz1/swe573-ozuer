package com.studyhole.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.studyhole.app.data.CommentPackage;
import com.studyhole.app.mapper.CommentMapper;
import com.studyhole.app.model.Comment;
import com.studyhole.app.model.User;
import com.studyhole.app.model.Post.Post;
import com.studyhole.app.repository.CommentRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    
    //Services
    private final StudyholeService studyholeService;

    @Transactional
    public void save(CommentPackage commentPackage) {
        User user = studyholeService.getCurrentUser();
        Post post = studyholeService.getPostById(commentPackage.getPostId());
        Comment comment = commentMapper.mapCommentClass(commentPackage, user, post);
        
        commentRepository.save(comment);
    }

    @Transactional
    public List<CommentPackage> getAllCommentsForPost(Long postId){
        Post post = studyholeService.getPostById(postId);
        var comments = commentRepository.findByPost(post)
        .stream().map(commentMapper::mapCommentPackage).collect(toList());

        return comments;
    }

    @Transactional
    public List<CommentPackage> getAllCommentsForUser(String username){
        User user = studyholeService.getUserbyUsername(username);
        var comments = commentRepository.findByUser(user)
        .stream().map(commentMapper::mapCommentPackage)
        .collect(toList());

        return comments;
    }
    
    @Transactional
    public Comment getCommentById(Long id){
        Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("COMMENT NOT FOUND"));

        return comment;
    }
}
