package edu.pidev.backend.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AuthenticationResponse {
    private String authToken;
    private String refreshToken;
    private LocalDate expiresIn ;
}
