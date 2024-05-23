package com.nam.demojpa.service;

import com.nam.demojpa.dto.reponse.PermissionResponse;
import com.nam.demojpa.dto.request.PermissionRequest;
import com.nam.demojpa.mapper.PermissionMapper;
import com.nam.demojpa.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.nam.demojpa.entity.Permission;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        log.info("Permission request: {}", request.getName());
        Permission permission = permissionMapper.toPermission(request);
        permission =  permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        log.info("Permissions: {}", permissions.stream().map(Permission::getName).toList());
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void delete(String permissionName) {
        permissionRepository.deleteById(permissionName);
    }
}
