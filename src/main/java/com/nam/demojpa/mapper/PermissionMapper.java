package com.nam.demojpa.mapper;

import com.nam.demojpa.dto.reponse.PermissionResponse;
import com.nam.demojpa.dto.request.PermissionRequest;
import org.mapstruct.Mapper;
import com.nam.demojpa.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
