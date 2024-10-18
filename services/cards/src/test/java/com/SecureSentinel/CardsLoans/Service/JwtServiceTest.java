package com.SecureSentinel.CardsLoans.Service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtService, "secretKey", "UiynOe89DRPIBdPzuyiQ90FN4skkGOkGUO+aqW8ks3c=");
    }

    @Test
    void ExtractClaim() {
        String token = generateTestToken();
        String extractedClaim = jwtService.extractClaim(token, claims -> claims.getSubject());
        assertEquals("testUser", extractedClaim);
    }

    private String generateTestToken() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "USER");
        claims.put("userId", "123");

        Collection<GrantedAuthority> authorities = Collections.emptyList();

        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User("testUser", "testPassword", authorities);

        return jwtService.generateToken(claims, userDetails);
    }


    @Test
    void generateToken() {
        UserDetails userDetails = User.withUsername("testUser")
                .password("testPassword")
                .authorities("ROLE_USER")
                .build();

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
    }

    @Test
    void testGenerateToken() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "USER");
        extraClaims.put("userId", "123");

        Collection<GrantedAuthority> authorities = Collections.emptyList();

        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User("testUser", "testPassword", authorities);

        String token = jwtService.generateToken(extraClaims, userDetails);
        assertNotNull(token);
    }

    @Test
    void isTokenValid() {
        String token = jwtService.generateToken(createUserDetails());

        assertTrue(jwtService.isTokenValid(token, createUserDetails()));
    }
    private UserDetails createUserDetails() {
        return User.withUsername("testUser")
                .password("testPassword")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    void extractUserRole() {
        Claims claims = mock(Claims.class);
        JwtService jwtService = mock(JwtService.class);

        String expectedRole = null;

        when(jwtService.extractClaim(
                "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjY1Mywic3ViIjoidXNlcjEiLCJpYXQiOjE3MDU1OTM3NjQsImV4cCI6MTkwNTYyOTc2NH0.oiDGzkVNL_ja3Xog9CBl3FFZMo8SpM9EZnvTfZF9c0k",
                claimsResolver -> expectedRole))
                .thenReturn(expectedRole);

        String actualRole = jwtService.extractUserRole("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjY1Mywic3ViIjoidXNlcjEiLCJpYXQiOjE3MDU1OTM3NjQsImV4cCI6MTkwNTYyOTc2NH0.oiDGzkVNL_ja3Xog9CBl3FFZMo8SpM9EZnvTfZF9c0k");

        assertEquals(expectedRole, actualRole);
    }


    @Test
    void extractUserId() {
        JwtService jwtServiceMock = mock(JwtService.class);

        Claims claimsMock = mock(Claims.class);

        String expectedUserId = null;

        when(jwtServiceMock.extractClaim(anyString(), any(Function.class)))
                .thenReturn(expectedUserId);

        String actualUserId = jwtServiceMock.extractUserId("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjY1Mywic3ViIjoidXNlcjEiLCJpYXQiOjE3MDU1OTM3NjQsImV4cCI6MTkwNTYyOTc2NH0.oiDGzkVNL_ja3Xog9CBl3FFZMo8SpM9EZnvTfZF9c0k");

        assertEquals(expectedUserId, actualUserId);
    }
}