package com.sensei.backend.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sensei.backend.dto.ParentUserDTO;
import com.sensei.backend.dto.ParentUserUpdateDTO;
import com.sensei.backend.entity.ParentUser;

@Mapper(componentModel = "spring", uses = {ChildUserMapper.class}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ParentUserMapper {

    ParentUserDTO toDto(ParentUser entity);

    @Mapping(target = "childLock", ignore = true)
    @Mapping(target = "isQuizCompleted", ignore = true)
    ParentUser toEntity(ParentUserDTO dto);

    @Mapping(target = "childLock", ignore = true)
    @Mapping(target = "isQuizCompleted", ignore = true)
    void updateEntityFromDto(ParentUserDTO dto, @MappingTarget ParentUser entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "childLock", ignore = true)
    @Mapping(target = "isQuizCompleted", ignore = true)
    void updateEntityFromUpdateDto(ParentUserUpdateDTO dto, @MappingTarget ParentUser entity);

    @AfterMapping
    default void mapIsChildLockSet(ParentUser entity, @MappingTarget ParentUserDTO dto) {
        if (entity != null && dto != null) {
            dto.setIsChildLockSet(entity.getChildLock() != null && !entity.getChildLock().trim().isEmpty());
        }
    }
}
