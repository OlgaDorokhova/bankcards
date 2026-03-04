package com.example.bankcards.controller.responses;

import com.example.bankcards.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication response")
public class AuthenticationResponse {
    @Schema(description = "User token", example = "12345")
    private String token;
    @Schema(description = "User with login", example = "User user")
    private User user;
}
