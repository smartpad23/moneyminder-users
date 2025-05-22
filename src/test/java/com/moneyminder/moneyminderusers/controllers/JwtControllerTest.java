package com.moneyminder.moneyminderusers.controllers;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@WebMvcTest(JwtController.class)
@AutoConfigureMockMvc(addFilters = false)
class JwtControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWKSet jwkSet;

    @Test
    @DisplayName("Get JWKS JSON")
    void getJwks() throws Exception {
        Map<String, Object> fakeJwkSet = Map.of(
                "keys", List.of(
                        Map.of(
                                "kty", "RSA",
                                "kid", "1234",
                                "use", "sig",
                                "n", "fake-modulus",
                                "e", "AQAB"
                        )
                )
        );

        JWKSet publicJwkSetMock = Mockito.mock(JWKSet.class);
        Mockito.when(jwkSet.toPublicJWKSet()).thenReturn(publicJwkSetMock);
        Mockito.when(publicJwkSetMock.toJSONObject()).thenReturn(fakeJwkSet);

        mockMvc.perform(get("/.well-known/jwks.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys").isArray())
                .andExpect(jsonPath("$.keys[0].kty").value("RSA"))
                .andExpect(jsonPath("$.keys[0].kid").value("1234"))
                .andExpect(jsonPath("$.keys[0].use").value("sig"));
    }
}