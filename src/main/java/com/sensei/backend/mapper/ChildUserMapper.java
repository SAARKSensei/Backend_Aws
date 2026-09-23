package com.sensei.backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.entity.ChildUser;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ChildUserMapper {
    @Mapping(source = "parentUser.parentId", target = "parentId")
    ChildUserDTO toDto(ChildUser entity);

    @Mapping(source = "parentId", target = "parentUser.parentId")
    ChildUser toEntity(ChildUserDTO dto);

    @Mapping(source = "parentId", target = "parentUser.parentId")
    void updateEntityFromDto(ChildUserDTO dto, @MappingTarget ChildUser entity);
}
