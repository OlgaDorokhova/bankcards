package com.example.bankcards.service;

import com.example.bankcards.model.Card;
import com.example.bankcards.model.Token;
import com.example.bankcards.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    User getUserByIdWithCards(Long id);
    User getUserByUsername(String username);
    Page<User> getAllUsers();
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
    User addCardToUser(Long userId, Card card);
    User setUserToken(Long userId);
    List<Card> getUserCards(Long userId);
    Token getUserToken(Long userId);
    User deactivateUser(Long userId);
}
