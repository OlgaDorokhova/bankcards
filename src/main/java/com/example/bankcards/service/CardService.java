package com.example.bankcards.service;

import com.example.bankcards.model.Card;

import java.util.List;

public interface CardService {
    List<Card> getAllClientCards(String cardHolderName);
    Card save(Card card);
    void delete(String cardNumber);
    Card getCard(String cardNumber);
    Card updateCard(String cardNumber);
}
