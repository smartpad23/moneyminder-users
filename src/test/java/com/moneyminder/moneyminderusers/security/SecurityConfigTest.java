package com.moneyminder.moneyminderusers.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class SecurityConfigTest {

    @Mock
    private JWKSource<SecurityContext> jwkSource;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Public endpoints security filter chain configuration test")
    void publicEndpointsSecurityFilterChainTest() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(httpSecurity.securityMatcher(any(String[].class))).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any(Customizer.class))).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        SecurityFilterChain chain = securityConfig.publicEndpoints(httpSecurity);

        assertNotNull(chain);
    }

    @Test
    @DisplayName("Secured endpoints security filter chain configuration test")
    void securedEndpointsSecurityFilterChainTest() throws Exception {
        HttpSecurity httpSecurity = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any(Customizer.class))).thenReturn(httpSecurity);
        when(httpSecurity.oauth2ResourceServer(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        SecurityFilterChain chain = securityConfig.publicEndpoints(httpSecurity);

        assertNotNull(chain);
    }

    @Test
    @DisplayName("jwkSet bean creation test")
    void jwkSetBeanCreationTest() {
        RSAKey rsaKey = mock(RSAKey.class);

        JWKSet jwkSet = securityConfig.jwkSet(rsaKey);

        assertNotNull(jwkSet);
    }

    @Test
    @DisplayName("jwkSource bean creation test")
    void jwkSourceBeanCreationTest() {
        JWKSet jwkSet = mock(JWKSet.class);

        JWKSource<SecurityContext> source = securityConfig.jwkSource(jwkSet);

        assertNotNull(source);
    }

    @Test
    @DisplayName("jwtEncoder bean creation test")
    void jwtEncoderBeanCreationTest() {
        NimbusJwtEncoder encoder = (NimbusJwtEncoder) securityConfig.jwtEncoder(jwkSource);

        assertNotNull(encoder);
    }

    @Test
    @DisplayName("jwtDecoder bean creation test")
    void jwtDecoderBeanCreationWithRealKeyTest() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // Correcto: 2048 bits
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID("test-id")
                .build();

        JwtDecoder decoder = securityConfig.jwtDecoder(rsaKey);

        assertNotNull(decoder);
    }

}