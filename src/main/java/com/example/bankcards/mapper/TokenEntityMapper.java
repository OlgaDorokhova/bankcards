package com.example.bankcards.mapper;

import com.example.bankcards.Entity.TokenEntity;
import com.example.bankcards.config.MapStructConfig;
import com.example.bankcards.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface TokenEntityMapper {
    Token map(TokenEntity tokenEntity);
    @Mapping(target = "user", ignore = true) // user устанавливается через хелпер-методы
    TokenEntity map(Token token);

    // Update entity from DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromModel(Token token, @MappingTarget TokenEntity entity);
}
