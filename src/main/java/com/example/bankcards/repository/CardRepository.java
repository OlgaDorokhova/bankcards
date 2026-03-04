package com.example.bankcards.repository;

import com.example.bankcards.Entity.CardEntity;
import com.example.bankcards.util.enums.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    // Поиск всех карт пользователя
    List<CardEntity> findByUserId(Long userId);

    // Найти все карты пользователя по статусу
    Page<CardEntity> findAllByUserIdAndStatus(Long userId, CardStatus status, Pageable pageable);

    // Найти карту по номеру (для переводов)
    Optional<CardEntity> findByCardNumber(String encryptedCardNumber);

    // Найти все карты (для ADMIN)
    Page<CardEntity> findAll(Pageable pageable);

    // Проверить принадлежит ли карта пользователю
    boolean existsByIdAndUserId(Long cardId, Long userId);

    // Проверка существования карты по номеру
    boolean existsByCardNumber(String cardNumber);

    // Поиск просроченных карт
    Page<CardEntity> findByExpiryDateBefore(LocalDate date, Pageable pageable);

    // Поиск карт с балансом больше указанного
    Page<CardEntity> findByBalanceGreaterThan(BigDecimal balance, Pageable pageable);

    // Поиск карт по диапазону баланса
    Page<CardEntity> findByBalanceBetween(BigDecimal minBalance, BigDecimal maxBalance, Pageable pageable);

    // Кастомный запрос для поиска карт с балансом меньше указанного у конкретного пользователя
    @Query("SELECT c FROM CardEntity c WHERE c.user.id = :userId AND c.balance < :balance")
    List<CardEntity> findUserCardsWithBalanceLessThan(@Param("userId") Long userId,
                                                      @Param("balance") BigDecimal balance);

    // Найти карту по ID с пользователем (для проверки владения)
    @Query("SELECT c FROM CardEntity c JOIN FETCH c.cardHolderName WHERE c.id = :cardId")
    Optional<CardEntity> findByIdWithUser(@Param("cardId") Long cardId);
}
