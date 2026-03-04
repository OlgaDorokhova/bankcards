package com.example.bankcards.service.impl;

import com.example.bankcards.Entity.CardEntity;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.model.Card;
import com.example.bankcards.model.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.CardService;
import com.example.bankcards.util.enums.CardStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    @Service
    @Transactional
    @RequiredArgsConstructor
    public class CardService {

        private final CardRepository cardRepository;
        private final UserRepository userRepository;
        private final EncryptionService encryptionService;
        private final CardMapper cardMapper;

        // CREATE - Создание новой карты (ADMIN)
        public CardResponse createCard(CreateCardRequest request, Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

            // Генерация номера карты (в реальном проекте - интеграция с платежной системой)
            String cardNumber = generateCardNumber();
            String encryptedCardNumber = encryptionService.encrypt(cardNumber);
            String maskedCardNumber = maskCardNumber(cardNumber);

            Card card = Card.builder()
                    .encryptedCardNumber(encryptedCardNumber)
                    .maskedNumber(maskedCardNumber)
                    .cardHolderName(request.cardHolderName())
                    .expiryDate(request.expiryDate())
                    .status(CardStatus.ACTIVE)
                    .balance(request.initialBalance())
                    .user(user)
                    .build();

            Card savedCard = cardRepository.save(card);
            return cardMapper.toResponse(savedCard);
        }

        // READ - Получить карту по ID
        @Transactional(readOnly = true)
        public Card getCardById(Long cardId) {
            CardEntity cardEntity = cardRepository.findById(cardId)
                    .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));
            return cardMapper.toResponse(card);
        }

        // READ - Получить карту с проверкой владения
        @Transactional(readOnly = true)
        public CardResponse getUserCardById(Long cardId, Long userId) {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

            if (!card.getUser().getId().equals(userId)) {
                throw new AccessDeniedException("Access denied to card: " + cardId);
            }

            return cardMapper.toResponse(card);
        }

        // READ - Получить ВСЕ карты пользователя (с пагинацией)
        @Transactional(readOnly = true)
        public Page<CardResponse> getUserCards(Long userId, Pageable pageable) {
            Page<Card> cards = cardRepository.findAllByUserId(userId, pageable);
            return cards.map(cardMapper::toResponse);
        }

        // READ - Получить ВСЕ карты в системе (ADMIN)
        @Transactional(readOnly = true)
        public Page<CardResponse> getAllCards(Pageable pageable) {
            Page<Card> cards = cardRepository.findAll(pageable);
            return cards.map(cardMapper::toResponse);
        }

        // READ - Получить карты по статусу
        @Transactional(readOnly = true)
        public Page<CardResponse> getUserCardsByStatus(Long userId, CardStatus status, Pageable pageable) {
            Page<Card> cards = cardRepository.findAllByUserIdAndStatus(userId, status, pageable);
            return cards.map(cardMapper::toResponse);
        }

        // UPDATE - Обновить карту
        public CardResponse updateCard(Long cardId, UpdateCardRequest request) {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

            if (request.cardHolderName() != null) {
                card.setCardHolderName(request.cardHolderName());
            }
            if (request.expiryDate() != null) {
                card.setExpiryDate(request.expiryDate());
            }
            if (request.status() != null) {
                card.setStatus(request.status());
            }

            Card updatedCard = cardRepository.save(card);
            return cardMapper.toResponse(updatedCard);
        }

        // UPDATE - Обновить баланс карты
        public void updateBalance(Long cardId, BigDecimal newBalance) {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));
            card.setBalance(newBalance);
            cardRepository.save(card);
        }

        // DELETE - Удалить карту (ADMIN)
        public void deleteCard(Long cardId) {
            if (!cardRepository.existsById(cardId)) {
                throw new CardNotFoundException("Card not found with id: " + cardId);
            }
            cardRepository.deleteById(cardId);
        }

        // Блокировка карты пользователем
        public CardResponse blockCard(Long cardId, Long userId) {
            Card card = cardRepository.findByIdWithUser(cardId)
                    .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

            if (!card.getUser().getId().equals(userId)) {
                throw new AccessDeniedException("Access denied to card: " + cardId);
            }

            if (card.getStatus() != CardStatus.ACTIVE) {
                throw new CardOperationException("Only active cards can be blocked");
            }

            card.setStatus(CardStatus.BLOCKED);
            Card updatedCard = cardRepository.save(card);
            return cardMapper.toResponse(updatedCard);
        }

        // Активация карты (ADMIN)
        public CardResponse activateCard(Long cardId) {
            Card card = cardRepository.findById(cardId)
                    .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardId));

            if (card.getStatus() != CardStatus.BLOCKED) {
                throw new CardOperationException("Only blocked cards can be activated");
            }

            card.setStatus(CardStatus.ACTIVE);
            Card updatedCard = cardRepository.save(card);
            return cardMapper.toResponse(updatedCard);
        }

        // Вспомогательные методы
        private String generateCardNumber() {
            // Генерация 16-значного номера карты
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                sb.append(random.nextInt(10));
                if ((i + 1) % 4 == 0 && i != 15) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        }

        private String maskCardNumber(String cardNumber) {
            // Маскировка: **** **** **** 1234
            if (cardNumber == null || cardNumber.length() < 4) {
                return "****";
            }
            String lastFour = cardNumber.substring(cardNumber.length() - 4);
            return "**** **** **** " + lastFour;
        }

        // Поиск карты по зашифрованному номеру (для переводов)
        @Transactional(readOnly = true)
        public Card findCardByEncryptedNumber(String encryptedCardNumber) {
            return cardRepository.findByEncryptedCardNumber(encryptedCardNumber)
                    .orElseThrow(() -> new CardNotFoundException("Card not found"));
        }

        // Проверка принадлежности карты пользователю
        public boolean isCardBelongsToUser(Long cardId, Long userId) {
            return cardRepository.existsByIdAndUserId(cardId, userId);
        }
    }
}
