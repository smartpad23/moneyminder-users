package com.moneyminder.moneyminderusers.processors.authServices;

import com.moneyminder.moneyminderusers.dto.LoginRequestDto;
import com.moneyminder.moneyminderusers.models.Session;
import com.moneyminder.moneyminderusers.processors.session.SaveSessionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SaveSessionProcessor saveSessionProcessor;

    @Mock
    private JwtEncoder jwtEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("successful authentication test")
    void successfulAuthenticationTest() throws Exception {
        LoginRequestDto request = new LoginRequestDto("user", "password");
        Jwt jwt = mock(Jwt.class);

        when(jwt.getTokenValue()).thenReturn("mockedToken");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);
        when(saveSessionProcessor.saveSession(any(Session.class))).thenReturn(new Session());

        String token = authenticationService.authenticationMethod(request);

        assertNotNull(token);
        assertEquals("mockedToken", token);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
        verify(saveSessionProcessor).saveSession(any(Session.class));
    }

    @Test
    @DisplayName("authentication with user not found test")
    void authenticationUserNotFoundTest() {
        LoginRequestDto request = new LoginRequestDto("user", "password");

        doThrow(new UsernameNotFoundException("Usuario no encontrado"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticationMethod(request));

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("authentication with bad credentials test")
    void authenticationBadCredentialsTest() {
        LoginRequestDto request = new LoginRequestDto("user", "wrongPassword");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticationMethod(request));

        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    @DisplayName("authentication with generic authentication exception test")
    void authenticationGenericAuthenticationExceptionTest() {
        LoginRequestDto request = new LoginRequestDto("user", "password");

        doThrow(new AuthenticationException("Generic error") {})
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticationMethod(request));

        assertEquals("Fallo de autenticación", exception.getMessage());
    }

}