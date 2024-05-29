package com.nam.demojpa.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import com.nam.demojpa.dto.reponse.UserResponse;
import com.nam.demojpa.dto.request.UserCreationRequest;
import com.nam.demojpa.entity.User;
import com.nam.demojpa.exception.AppException;
import com.nam.demojpa.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/test.properties")

public class UserServiceTest {
  @Autowired private UserService userService;

  @MockBean private UserRepository userRepository;

  private UserCreationRequest userCreationRequest;
  private UserResponse userResponse;
  private LocalDate dob;
  private User user;

  @BeforeEach
  void initData() {
    dob = LocalDate.parse("1990-01-01");
    userCreationRequest = new UserCreationRequest();
    userCreationRequest.setFirstName("Nam");
    userCreationRequest.setLastName("Nguyen");
    userCreationRequest.setUsername("namnguyen");
    userCreationRequest.setPassword("123456");
    userCreationRequest.setDob(dob);

    userResponse = new UserResponse();
    userResponse.setId("1");
    userResponse.setFirstName("Nam");
    userResponse.setLastName("Nguyen");
    userResponse.setUsername("namnguyen");
    userResponse.setDob(dob);

    user = new User();
    user.setId("1");
    user.setFirstName("Nam");
    user.setLastName("Nguyen");
    user.setUsername("namnguyen");
    user.setDob(dob);
  }

  @Test
  void createUser_validRequest_pass() {
    Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(false);
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);

    var response = userService.createUser(userCreationRequest);
    assertThat(response.getId()).isEqualTo("1");
    assertThat(response.getUsername()).isEqualTo("namnguyen");
  }

  @Test
  void createUser_userExisted_fail() {
    Mockito.when(userRepository.existsByUsername(anyString())).thenReturn(true);

    var exception =
            assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));

    assertThat(exception.getErrorCode().getCode()).isEqualTo(100);
  }

  @Test
  @WithMockUser(username = "namnguyen", roles = {"USER"})
  void getMyInfo_validRequest_pass() {
    Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

    var response = userService.getMyInfo();
    assertThat(response.getUsername()).isEqualTo("namnguyen");
    assertThat(response.getId()).isEqualTo("1");
  }

  @Test
  @WithMockUser(username = "namnguyen", roles = {"USER"})
  void getMyInfo_userNotFound_error() {
    Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

    var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
    assertThat(exception.getErrorCode().getCode()).isEqualTo(404);
  }
}
