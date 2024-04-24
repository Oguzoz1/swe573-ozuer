package com.studyhole.app.data;

import java.time.Instant;
import java.util.Set;

import com.studyhole.app.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommunityPackage {
    private Long communityId;
    private String name;
    private String description;
    private Integer numberOfPosts;
    private Instant createdDate;
    private Set<User> ownerUsers;
}
