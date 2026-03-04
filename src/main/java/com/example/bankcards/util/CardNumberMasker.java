package com.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class CardNumberMasker {
    private static final String MASK_PREFIX = "**** **** **** ";
    private static final int VISIBLE_DIGITS = 4;

    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return "";
        }

        String cleanNumber = cardNumber.trim().replaceAll("\\s", "");

        // Простая проверка, что номер состоит из цифр (для открытого номера)
        if (!cleanNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Card number must contain only digits");
        }

        // Стандартные номера карт обычно от 13 до 19 цифр
        if (cleanNumber.length() < 13 || cleanNumber.length() > 19) {
            throw new IllegalArgumentException("Invalid card number length");
        }

        String lastFourDigits = cleanNumber.substring(cleanNumber.length() - VISIBLE_DIGITS);
        return MASK_PREFIX + lastFourDigits;
    }
}
