package com.nam.demojpa.dto.request;

import com.nam.demojpa.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;


public class UserCreationRequest {
    @Size(min = 6, message = "USERNAME_INVALID")
     private String username;

    @Size(min = 6, message = "PASSWORD_INVALID")
    private String password;
    private String firstName;
    private String lastName;

     @DobConstraint(min = 16, message = "INVALID_DOB")
     private LocalDate dob;

    public @Size(min = 6, message = "USERNAME_INVALID") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 6, message = "USERNAME_INVALID") String username) {
        this.username = username;
    }

    public @Size(min = 6, message = "PASSWORD_INVALID") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6, message = "PASSWORD_INVALID") String password) {
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
}
