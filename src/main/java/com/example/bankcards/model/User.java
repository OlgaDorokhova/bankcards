package com.example.bankcards.model;

import com.example.bankcards.util.enums.Roles;
import lombok.Data;

import java.util.List;

@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private String token;
    private Roles role;
    private List<Card> cards;
    private boolean enabled = true;

}
