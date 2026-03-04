package com.example.bankcards.mapper;

import com.example.bankcards.Entity.CardEntity;
import com.example.bankcards.Entity.TokenEntity;
import com.example.bankcards.Entity.UserEntity;
import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.model.Card;
import com.example.bankcards.model.Token;
import com.example.bankcards.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = MapStructConfig.class, uses = {CardEntityMapper.class, TokenEntityMapper.class})
public interface UserEntityMapper {
    @Mapping(source = "cards", target = "cards")
    @Mapping(source = "token", target = "token")
    User map(UserEntity userEntity);

    @Mapping(source = "cards", target = "cards")
    @Mapping(source = "token", target = "token")
    UserEntity map(User user);
    List<User> map(List<UserEntity> userEntities);

    default Page<User> toUserPage(Page<UserEntity> entityPage) {
        List<User> users = map(entityPage.getContent());
        return new PageImpl<>(users, entityPage.getPageable(), entityPage.getTotalElements());
    }

    @Named("mapCards")
    default List<CardEntity> mapCards(List<Card> cards) {
        if (cards == null) {
            return null;
        }
        return cards.stream()
                .map(this::mapCardToEntity)
                .collect(Collectors.toList());
    }

    @Named("mapCardsToDto")
    default List<Card> mapCardsToModel(List<CardEntity> cards) {
        if (cards == null) {
            return null;
        }
        return cards.stream()
                .map(this::mapCardToModel)
                .collect(Collectors.toList());
    }

    // Вспомогательные методы для маппинга отдельных сущностей
    default CardEntity mapCardToEntity(Card card) {
        if (card == null) {
            return null;
        }
        CardEntity entity = new CardEntity();
        entity.setId(card.getId());
        entity.setCardNumber(card.getCardNumber());
        entity.setCardHolderName(card.getCardHolderName());
        entity.setExpiryDate(card.getExpiryDate());
        entity.setStatus(card.getStatus());
        entity.setBalance(card.getBalance());
        return entity;
    }

    default Card mapCardToModel(CardEntity entity) {
        if (entity == null) {
            return null;
        }
        Card dto = new Card();
        dto.setId(entity.getId());
        dto.setCardNumber(entity.getCardNumber());
        dto.setCardHolderName(entity.getCardHolderName());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setStatus(entity.getStatus());
        dto.setBalance(entity.getBalance());
        return dto;
    }

    default TokenEntity mapTokenToEntity(Token token) {
        if (token == null) {
            return null;
        }
        TokenEntity entity = new TokenEntity();
        entity.setId(token.getId());
        entity.setToken(token.getToken());
        return entity;
    }

    default Token mapTokenToModel(TokenEntity entity) {
        if (entity == null) {
            return null;
        }
        Token dto = new Token();
        dto.setId(entity.getId());
        dto.setToken(entity.getToken());
        return dto;
    }

}
