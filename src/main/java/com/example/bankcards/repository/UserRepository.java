package com.example.bankcards.repository;

import com.example.bankcards.Entity.UserEntity;
import com.example.bankcards.model.User;
import com.example.bankcards.util.enums.CardStatus;
import com.example.bankcards.util.enums.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    // Проверка существования пользователя по username
    boolean existsByUsername(String username);

    // Поиск пользователей по роли
    List<UserEntity> findByRole(Roles role);

    // Поиск активных/неактивных пользователей
    Page<UserEntity> findByEnabled(boolean enabled, Pageable pageable);

    // Поиск по username с игнорированием регистра
    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    // Кастомные запросы с JOIN FETCH для избежания N+1 проблемы
    @Query("SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.cards WHERE u.id = :id")
    Optional<UserEntity> findByIdWithCards(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM UserEntity u LEFT JOIN FETCH u.cards WHERE u.username = :username")
    Optional<UserEntity> findByUsernameWithCards(@Param("username") String username);

    // Кастомный запрос для поиска пользователей с картами определенного статуса
    @Query("SELECT u FROM UserEntity u JOIN u.cards c WHERE c.status = :status")
    Page<UserEntity> findUsersWithCardsByStatus(@Param("status") CardStatus status, Pageable pageable);

    // Поиск пользователя по токену
    @Query("SELECT u FROM UserEntity u JOIN u.token t WHERE t.token = :token")
    Optional<UserEntity> findByTokenValue(@Param("token") String token);
}
