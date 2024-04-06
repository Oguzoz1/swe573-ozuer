package com.studyhole.demo.model.Vote;

import lombok.*;

import com.studyhole.demo.model.User;
import com.studyhole.demo.model.Post.Post;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long voteId;
    private VoteType voteType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", referencedColumnName = "postId")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;
}