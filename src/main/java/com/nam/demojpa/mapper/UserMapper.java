package com.nam.demojpa.mapper;

import com.nam.demojpa.dto.reponse.UserResponse;
import com.nam.demojpa.dto.request.UserCreationRequest;
import com.nam.demojpa.dto.request.UserUpdateRequest;
import com.nam.demojpa.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = User.class)
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(ignore = true, target = "roles")
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
