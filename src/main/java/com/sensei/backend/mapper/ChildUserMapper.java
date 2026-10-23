package com.sensei.backend.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sensei.backend.dto.ChildUserDTO;
import com.sensei.backend.dto.ChildUserUpdateDTO;
import com.sensei.backend.entity.ChildUser;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ChildUserMapper {
    @Mapping(source = "parentUser.parentId", target = "parentId")
    ChildUserDTO toDto(ChildUser entity);

    @Mapping(source = "parentId", target = "parentUser.parentId")
    ChildUser toEntity(ChildUserDTO dto);

    @Mapping(source = "parentId", target = "parentUser.parentId")
    void updateEntityFromDto(ChildUserDTO dto, @MappingTarget ChildUser entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "parentUser", ignore = true)
    void updateEntityFromUpdateDto(ChildUserUpdateDTO dto, @MappingTarget ChildUser entity);
}
