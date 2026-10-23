package com.sensei.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sensei.backend.dto.ParentUserDTO;
import com.sensei.backend.dto.ParentUserUpdateDTO;
import com.sensei.backend.entity.ParentUser;

@Mapper(componentModel = "spring", uses = {ChildUserMapper.class}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ParentUserMapper {
    ParentUserDTO toDto(ParentUser entity);
    ParentUser toEntity(ParentUserDTO dto);
    void updateEntityFromDto(ParentUserDTO dto, @MappingTarget ParentUser entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdateDto(ParentUserUpdateDTO dto, @MappingTarget ParentUser entity);
}
