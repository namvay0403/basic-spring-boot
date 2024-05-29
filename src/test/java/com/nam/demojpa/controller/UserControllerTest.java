package com.nam.demojpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nam.demojpa.dto.reponse.UserResponse;
import com.nam.demojpa.dto.request.UserCreationRequest;
import com.nam.demojpa.service.UserService;
import com.nam.demojpa.service.UserServiceTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/test.properties")
public class UserControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private UserService userService;

  private UserCreationRequest userCreationRequest;
  private UserResponse userResponse;
  private LocalDate dob;

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
  }

  @Test
  public void createUser_validRequest_success() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String userCreationRequestJson = objectMapper.writeValueAsString(userCreationRequest);

    Mockito.when(userService.createUser(ArgumentMatchers.any())).thenReturn(userResponse);

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userCreationRequestJson))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("code").value("200"));
  }

  @Test
  public void createUser_usernameInvalid_fail() throws Exception {

    userCreationRequest.setUsername("nam");

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    String userCreationRequestJson = objectMapper.writeValueAsString(userCreationRequest);

    mockMvc
            .perform(
                    MockMvcRequestBuilders.post("/users")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(userCreationRequestJson))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("code").value("102"))
            .andExpect(MockMvcResultMatchers.jsonPath("message").value("Username must be at least 6 characters"));
    ;
  }
}
