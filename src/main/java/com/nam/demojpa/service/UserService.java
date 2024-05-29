package com.nam.demojpa.service;

import com.nam.demojpa.dto.reponse.UserResponse;
import com.nam.demojpa.dto.request.UserCreationRequest;
import com.nam.demojpa.dto.request.UserUpdateRequest;
import com.nam.demojpa.entity.User;
import com.nam.demojpa.enums.Role;
import com.nam.demojpa.exception.AppException;
import com.nam.demojpa.exception.ErrorCode;
import com.nam.demojpa.mapper.UserMapper;
import com.nam.demojpa.repository.RoleRepository;
import com.nam.demojpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  UserRepository userRepository;
  UserMapper userMapper;
  PasswordEncoder passwordEncoder;
  RoleRepository roleRepository;

  public UserResponse createUser(UserCreationRequest request) {
    log.info("In method create User");
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.USER_EXISTED);
    }
    User user = userMapper.toUser(request);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    com.nam.demojpa.entity.Role role = new com.nam.demojpa.entity.Role();
    HashSet<com.nam.demojpa.entity.Role> roles = new HashSet<>();
    role.setName(Role.USER.name());
    roles.add(role);
    user.setRoles(roles);
    return userMapper.toUserResponse(userRepository.save(user));
  }

//  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PreAuthorize("hasAuthority('CREATE_DATA')")
  public List<UserResponse> getAllUsers(){
    log.info("In method get Users");
    return userRepository.findAll().stream()
            .map(userMapper::toUserResponse).toList();
  }

  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse getUser(String userId) {
    return userMapper.toUserResponse(
        userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND)));
  }

  @PostAuthorize("returnObject.username == authentication.name")
  public UserResponse getMyInfo() {
    var context = SecurityContextHolder.getContext();
    String name = context.getAuthentication().getName();
    User byUserName =
        userRepository
            .findByUsername(name)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    return userMapper.toUserResponse(byUserName);
  }

  public UserResponse updateUser(String userId, UserUpdateRequest request) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    userMapper.updateUser(user, request);

    user.setPassword(passwordEncoder.encode(request.getPassword()));
    var roles = roleRepository.findAllById(request.getRoles());
    user.setRoles(new HashSet<>(roles));

    return userMapper.toUserResponse(userRepository.save(user));
  }

  @PreAuthorize("hasRole('ADMIN')")
  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }
}
