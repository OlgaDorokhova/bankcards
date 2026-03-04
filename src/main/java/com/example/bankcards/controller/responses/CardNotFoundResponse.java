package com.example.bankcards.controller.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Card not found response")
public class CardNotFoundResponse {
    @Schema(description = "Card not found", example = "Not found")
    private String message;
}
