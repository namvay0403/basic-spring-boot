package com.nam.demojpa.dto.request;

import com.nam.demojpa.validator.DobConstraint;
import java.time.LocalDate;
import java.util.List;
import lombok.*;

public class UserUpdateRequest {
  private String password;
  private String firstName;
  private String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  private LocalDate dob;

  private List<String> roles;

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }
}
