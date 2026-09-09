package com.sensei.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.entity.ChildUser;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ChildUserMapper {
    ChildUserDTO toDto(ChildUser entity);
    ChildUser toEntity(ChildUserDTO dto);
    void updateEntityFromDto(ChildUserDTO dto, @MappingTarget ChildUser entity);
}
