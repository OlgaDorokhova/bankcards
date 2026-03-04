package com.example.bankcards.service.impl;

import com.example.bankcards.Entity.CardEntity;
import com.example.bankcards.Entity.TokenEntity;
import com.example.bankcards.Entity.UserEntity;
import com.example.bankcards.exception.TokenNotFoundException;
import com.example.bankcards.exception.UserExistsException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mapper.CardEntityMapper;
import com.example.bankcards.mapper.TokenEntityMapper;
import com.example.bankcards.mapper.UserEntityMapper;
import com.example.bankcards.model.Card;
import com.example.bankcards.model.Token;
import com.example.bankcards.model.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TokenRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtTokenUtil;
import com.example.bankcards.service.CardService;
import com.example.bankcards.service.UserService;
import com.example.bankcards.util.enums.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final TokenRepository tokenRepository;
    private final CardService cardService;
    private final UserEntityMapper userEntityMapper;
    private final TokenEntityMapper tokenEntityMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final CardEntityMapper cardEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserExistsException("Username already exists");
        }
        UserEntity userEntity = userEntityMapper.map(user);


        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(jwtTokenUtil.generateToken(user));
        TokenEntity saved = tokenRepository.save(tokenEntity);

        userEntity.setToken(saved.getUser().getToken());
        // Устанавливаем связи для карт
        if (userEntity.getCards() != null) {
            userEntity.getCards().forEach(card -> card.setUser(userEntity));
        }

        // Устанавливаем связь для токена
        if (userEntity.getToken() != null) {
            userEntity.getToken().setUser(userEntity);
        }

        return userEntityMapper.map(userRepository.save(userEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .map(userEntityMapper::map)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserByIdWithCards(Long id) {
        return userRepository.findByIdWithCards(id)
                .map(userEntityMapper::map)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }


    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .map(userEntityMapper :: map)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return user;
    }

    @Override
    public Page<User> getAllUsers() {

        return null;
    }


    //TODO
    @Override
    public User updateUser(Long id, User user) {
//        User updatedUser = userRepository.findById(id)
//                .map(existingEntity -> {
//                    // Сохраняем существующие связи
//                    List<CardEntity> existingCards = existingEntity.getCards();
//                    TokenEntity existingToken = existingEntity.getToken();
//                    // Обновляем базовые поля и связи
//                    userEntityMapper.updateEntityFromDto(userDto, existingEntity);
//
//                    // Восстанавливаем/обновляем связи
//                    if (userDto.getCards() != null) {
//                        // Обновляем связи для новых карт
//                        existingEntity.getCards().forEach(card -> card.setUser(existingEntity));
//                    } else {
//                        // Сохраняем существующие карты
//                        existingEntity.setCards(existingCards);
//                    }
//
//                    if (userDto.getToken() != null) {
//                        // Обновляем связь для токена
//                        existingEntity.getToken().setUser(existingEntity);
//                    } else {
//                        // Сохраняем существующий токен
//                        existingEntity.setToken(existingToken);
//                    }
//
//                    UserEntity updatedEntity = userRepository.save(existingEntity);
//                    return userEntityMapper.map(updatedEntity);
//                })
//                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return new User();
    }

    @Override
    public void deleteUser(Long id) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {throw new UserNotFoundException("User not found with id: " + id);}
    }

    @Override
    public User addCardToUser(Long userId, Card card) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardNumber(card.getCardNumber());
        cardEntity.setCardHolderName(card.getCardHolderName());
        cardEntity.setExpiryDate(card.getExpiryDate());
        cardEntity.setStatus(card.getStatus());
        cardEntity.setBalance(card.getBalance());

        userEntity.addCard(cardEntity);

        UserEntity savedUser = userRepository.save(userEntity);
        return userEntityMapper.map(savedUser);    }

    @Override
    @Transactional(readOnly = true)
    public List<Card> getUserCards(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return cardEntityMapper.map(userEntity.getCards());
    }

    @Override
    public User setUserToken(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        tokenRepository.findByUserId(userId).ifPresent(tokenRepository::delete);

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(jwtTokenUtil.generateToken(userEntityMapper.map(userEntity)));
        return userEntityMapper.map(userEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Token getUserToken(Long userId) {
        Token token = tokenRepository.findByUserId(userId)
                .map(tokenEntityMapper :: map)
                .orElseThrow(() -> new TokenNotFoundException("Token not found with id: " + userId));
        return token;
    }

    // BUSINESS METHODS
    @Override
    public User deactivateUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setEnabled(false);

        // Деактивируем все карты пользователя
        user.getCards().forEach(card -> card.setStatus(CardStatus.INACTIVE));

        UserEntity savedUser = userRepository.save(user);
        return userEntityMapper.map(savedUser);
    }

    // BULK OPERATIONS
    @Transactional(readOnly = true)
    public Page<User> getUsersWithCardsByStatus(CardStatus status, Pageable pageable) {
        Page<UserEntity> entityPage = userRepository.findUsersWithCardsByStatus(status, pageable);
        return userEntityMapper.toUserPage(entityPage);
    }
}
