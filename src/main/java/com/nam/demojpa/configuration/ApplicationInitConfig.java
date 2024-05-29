package com.nam.demojpa.configuration;

import com.nam.demojpa.entity.Role;
import com.nam.demojpa.entity.User;
import com.nam.demojpa.repository.UserRepository;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class ApplicationInitConfig {

  PasswordEncoder passwordEncoder;


  @Bean
  @ConditionalOnProperty(prefix = "spring", value = "datasource.driver-class-name", havingValue = "com.mysql.cj.jdbc.Driver")
  ApplicationRunner applicationRunner(UserRepository userRepository) {
    return args -> {
      if (userRepository.findByUsername("admin").isEmpty()) {
        Role role = new Role();
        role.setName(com.nam.demojpa.enums.Role.ADMIN.name());
        var roles = new HashSet<Role>();
        roles.add(role);
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setFirstName("Admin");
        user.setLastName("Admin");
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
        log.warn("Admin user created with default password: admin");
      }
      ;
    };
  }
}
