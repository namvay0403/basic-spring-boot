package com.nam.demojpa.service;
import com.nam.demojpa.dto.reponse.RoleResponse;
import com.nam.demojpa.dto.request.RoleRequest;
import com.nam.demojpa.exception.AppException;
import com.nam.demojpa.exception.ErrorCode;
import com.nam.demojpa.mapper.RoleMapper;
import com.nam.demojpa.repository.PermissionRepository;
import com.nam.demojpa.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoleService {

  private static final Logger log = LoggerFactory.getLogger(RoleService.class);
  RoleRepository roleRepository;
  PermissionRepository permissionRepository;
  RoleMapper roleMapper;

  public RoleResponse create (RoleRequest request) {
    var role = roleMapper.toRole(request);
    var permissions = permissionRepository.findAllById(request.getPermissions());
    role.setPermissions(new HashSet<>(permissions));
    role = roleRepository.save(role);
    log.info("Role created: {}", role);
    return roleMapper.toRoleResponse(role);
  }

  public List<RoleResponse> getAll() {
    return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
  }

  public RoleResponse getById(String roleName) {
    var role = roleRepository.findById(roleName).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    return roleMapper.toRoleResponse(role);
  }

  public void delete(String roleName) {
    roleRepository.deleteById(roleName);
  }

}
