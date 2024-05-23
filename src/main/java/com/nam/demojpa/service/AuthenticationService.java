package com.nam.demojpa.service;

import com.nam.demojpa.dto.reponse.AuthenticationResponse;
import com.nam.demojpa.dto.reponse.IntrospectResponse;
import com.nam.demojpa.dto.request.AuthenticationRequest;
import com.nam.demojpa.dto.request.IntrospectRequest;
import com.nam.demojpa.entity.User;
import com.nam.demojpa.exception.AppException;
import com.nam.demojpa.exception.ErrorCode;
import com.nam.demojpa.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationService {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
  UserRepository userRepository;

  @NonFinal
  @Value("${jwt.signerKey}")
  protected String SECRET_KEY;

  public IntrospectResponse introspectResponse(IntrospectRequest request)
      throws JOSEException, ParseException {
    var token = request.getToken();

    JWSVerifier jwsVerifier = new MACVerifier(SECRET_KEY.getBytes());

    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    var verified = signedJWT.verify(jwsVerifier);

    return IntrospectResponse.builder().valid(verified && expirationTime.after(new Date())).build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    var user =
        userRepository
            .findByUsername(request.getUsername())
            .orElseThrow(() -> new AppException(ErrorCode.USER_EXISTED));
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!authenticated) {
      throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    var token = generateToken(user);
    
    return AuthenticationResponse.builder().token(token).authenticated(true).build();
  }

  private String generateToken(User user) {
    JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .subject(user.getUsername())
            .issuer("ltnam43202.dev")
            .issueTime(new Date())
            .expirationTime(new Date(Instant.now().plus(30, ChronoUnit.DAYS).toEpochMilli()))
            .claim("scope", buildScope(user))
            .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(jwsHeader, payload);

    try {
      jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error("Error signing token", e);
      throw new RuntimeException(e);
    }
  }

  private String buildScope(User user) {
    StringJoiner stringJoiner = new StringJoiner(" ");
    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        stringJoiner.add("ROLE_" + role.getName());
        if (!CollectionUtils.isEmpty(role.getPermissions())){
          role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
        }
      });
    }
    return stringJoiner.toString();
  }
}
