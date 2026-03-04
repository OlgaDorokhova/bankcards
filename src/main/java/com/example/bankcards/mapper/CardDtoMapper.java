package com.example.bankcards.mapper;

import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.dto.CardDto;
import com.example.bankcards.model.Card;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapStructConfig.class)
public interface CardDtoMapper {
    CardDto map(Card card);
    List<CardDto> map(List<Card> cards);
    Card map(CardDto cardDto);
}
