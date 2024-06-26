package com.studyhole.app.data;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthPackage {
    private String authToken;
    private String refreshToken;
    private String username;
    private Instant expiresAt;
}
