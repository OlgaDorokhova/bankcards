package com.example.bankcards.dto;

import com.example.bankcards.model.Card;
import com.example.bankcards.util.enums.Roles;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String username;
    private String password;
    private String token;
    private Roles role;
    private List<Card> cards;
    private boolean enabled = true;
}
