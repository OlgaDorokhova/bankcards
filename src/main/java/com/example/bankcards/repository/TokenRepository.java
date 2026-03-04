package com.example.bankcards.repository;

import com.example.bankcards.Entity.TokenEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<TokenEntity, Long> {

        // Поиск по значению токена
        Optional<TokenEntity> findByToken(String token);

        // Поиск токена по ID пользователя
        Optional<TokenEntity> findByUserId(Long userId);

        // Проверка существования токена
        boolean existsByToken(String token);

        // Удаление токена по значению
        void deleteByToken(String token);

        // Удаление токена по ID пользователя
        @Modifying
        @Query("DELETE FROM TokenEntity t WHERE t.user.id = :userId")
        void deleteByUserId(@Param("userId") Long userId);

        // Проверка существования токена для пользователя
        boolean existsByUserId(Long userId);
    }

