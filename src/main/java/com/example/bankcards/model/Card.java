package com.example.bankcards.model;

import com.example.bankcards.util.enums.CardStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Card {
        private Long id;
        private String cardNumber;
        private String cardHolderName;
        private LocalDate expiryDate;
        private CardStatus status;
        private BigDecimal balance;
}
