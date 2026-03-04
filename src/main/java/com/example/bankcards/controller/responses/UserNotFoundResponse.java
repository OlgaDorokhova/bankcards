package com.example.bankcards.controller.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "User not found")
public class UserNotFoundResponse {
    @Schema(description = "User not found", example = "Not found")
    private String message;
}
