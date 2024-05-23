package com.nam.demojpa.repository;

import com.nam.demojpa.entity.Permission;
import com.nam.demojpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
}
