package com.example.bankcards.dto;

import com.example.bankcards.util.enums.CardStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardDto {
    private Long id;
    private String maskedCardNumber;
    private String cardHolderName;
    private String expiryDate;
    private CardStatus status;
    private BigDecimal balance;
}
