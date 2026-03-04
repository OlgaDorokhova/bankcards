package com.example.bankcards.mapper;

import com.example.bankcards.Entity.CardEntity;
import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.model.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface CardEntityMapper {
    Card map(CardEntity cardEntity);
    List<Card> map(List<CardEntity> cardEntities);
    CardEntity map(Card card);
    // Update entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromModel(Card card, @MappingTarget CardEntity entity);
}
