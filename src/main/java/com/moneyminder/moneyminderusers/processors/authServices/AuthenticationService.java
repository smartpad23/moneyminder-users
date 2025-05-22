package com.moneyminder.moneyminderusers.processors.authServices;

import com.moneyminder.moneyminderusers.dto.LoginRequestDto;
import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.processors.session.SaveSessionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final SaveSessionProcessor saveSessionProcessor;
    private final JwtEncoder jwtEncoder;

    public String authenticationMethod( LoginRequestDto request) throws Exception {
        final int EXPIRATION_TIME = 604800;

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException("Usuario no encontrado");
        } catch (BadCredentialsException e) {
            throw e;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Fallo de autenticación");
        }


        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(request.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRATION_TIME))
                .issuer("http://localhost:18082")
                .claim("scope", "USER")
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        final Session session = new Session();
        session.setToken(token);
        session.setUser(request.getUsername());
        session.setCreationDate(LocalDate.now());
        session.setExpirationDate(LocalDate.ofInstant(now.plusSeconds(EXPIRATION_TIME), ZoneId.systemDefault()));

        this.saveSessionProcessor.saveSession(session);

        return token;
    }
}
