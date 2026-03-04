package com.example.bankcards.controller.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Authentication error response")
public class AuthenticationErrorResponse {
    @Schema(description = "User token", example = "12345")
    private String token;
}
