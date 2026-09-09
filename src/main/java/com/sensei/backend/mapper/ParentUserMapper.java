package com.sensei.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.sensei.backend.dto.ParentUserDTO;
import com.sensei.backend.entity.ParentUser;

@Mapper(componentModel = "spring", uses = {ChildUserMapper.class}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ParentUserMapper {
    ParentUserDTO toDto(ParentUser entity);
    ParentUser toEntity(ParentUserDTO dto);
    void updateEntityFromDto(ParentUserDTO dto, @MappingTarget ParentUser entity);
}
