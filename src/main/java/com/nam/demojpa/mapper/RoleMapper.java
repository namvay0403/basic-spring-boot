package com.nam.demojpa.mapper;

import com.nam.demojpa.dto.reponse.PermissionResponse;
import com.nam.demojpa.dto.reponse.RoleResponse;
import com.nam.demojpa.dto.request.PermissionRequest;
import com.nam.demojpa.dto.request.RoleRequest;
import com.nam.demojpa.entity.Permission;
import com.nam.demojpa.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
