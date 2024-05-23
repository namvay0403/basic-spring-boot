package com.nam.demojpa.repository;

import com.nam.demojpa.entity.Permission;
import com.nam.demojpa.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
