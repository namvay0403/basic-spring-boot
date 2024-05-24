package com.nam.demojpa.configuration;

import com.nam.demojpa.dto.request.IntrospectRequest;
import com.nam.demojpa.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {

  @Value("${jwt.signerKey}")
  private String signerKey;

  @Autowired private AuthenticationService authenticationService;

  private NimbusJwtDecoder nimbusJwtDecoder = null;

  @Override
  public Jwt decode(String token) throws JwtException {
    try {
      var response =  authenticationService.introspectResponse(IntrospectRequest.builder().token(token).build());
        if (!response.isValid()){
            throw new JwtException("Invalid token");
        }
    } catch (JOSEException | ParseException e) {
      throw new JwtException(e.getMessage());
    }
    if (Objects.isNull(nimbusJwtDecoder)){
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
    return nimbusJwtDecoder.decode(token);
  }
}
