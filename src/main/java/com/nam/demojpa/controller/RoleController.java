package com.nam.demojpa.controller;

import com.nam.demojpa.dto.reponse.RoleResponse;
import com.nam.demojpa.dto.request.ApiResponse;
import com.nam.demojpa.dto.request.RoleRequest;
import com.nam.demojpa.mapper.RoleMapper;
import com.nam.demojpa.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RoleController {
  RoleService roleService;

  @PostMapping
  ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
    log.info("Role request: {}", request.getName());
    log.info("Role request: {}", request.getPermissions());
    log.info("Role request: {}", request.getDescription());
    return ApiResponse.<RoleResponse>builder().result(roleService.create(request)).build();
  }

  @GetMapping
  ApiResponse<List<RoleResponse>> getAll() {
    return ApiResponse.<List<RoleResponse>>builder().result(roleService.getAll()).build();
  }

  @DeleteMapping("/{role}")
  ApiResponse<Void> delete(@PathVariable String role) {
    roleService.delete(role);
    return ApiResponse.<Void>builder().build();
  }
}
