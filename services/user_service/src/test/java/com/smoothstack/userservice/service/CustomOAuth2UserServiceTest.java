package com.smoothstack.userservice.service;

import com.smoothstack.userservice.model.AppUser;
import com.smoothstack.userservice.model.Role;
import com.smoothstack.userservice.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomOAuth2UserServiceTest {

    private static final String DEFAULT_OAUTH2_PASSWORD = "$2a$10$eEgz5Q3U8Bxz13oLBZHzrukHZ6mJpTAUOuMdPPIkhPglJ806luJ16";

    @Mock
    private AppUserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        ServletRequestAttributes attributes = new ServletRequestAttributes(httpServletRequest, httpServletResponse);
        RequestContextHolder.setRequestAttributes(attributes);

        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testLoadUser_ExistingUser() throws IOException {
        String email = "test@example.com";
        String name = "Test User";
        AppUser existingUser = new AppUser();
        existingUser.setUserId(1);  // Setting the user ID to avoid null issues
        existingUser.setEmail(email);
        existingUser.setUsername("username");
        existingUser.setPassword(DEFAULT_OAUTH2_PASSWORD);
        existingUser.setRole(Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(jwtService.generateToken(existingUser, existingUser.getRole().name(), existingUser.getUserId())).thenReturn("jwt-token");

        String userInfoResponse = "{\"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
        mockWebServer.enqueue(new MockResponse()
                .setBody(userInfoResponse)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("client-id")
                .tokenUri(mockWebServer.url("/token").toString())
                .authorizationUri(mockWebServer.url("/auth").toString())
                .userInfoUri(mockWebServer.url("/userinfo").toString())
                .clientId("client-id")
                .clientSecret("client-secret")
                .redirectUri("http://localhost")
                .scope("read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userNameAttributeName("email")
                .build();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(TokenType.BEARER, "token", Instant.now(), Instant.now().plusSeconds(3600));
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        OAuth2User loadedUser = customOAuth2UserService.loadUser(userRequest);

        verify(userRepository, times(0)).save(any(AppUser.class));

        Set<String> expectedAuthorities = Set.of("OAUTH2_USER", "ADMIN");
        Set<String> actualAuthorities = loadedUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertThat(actualAuthorities).containsExactlyInAnyOrderElementsOf(expectedAuthorities);
    }

    @Test
    public void testLoadUser_NewUser() throws IOException {
        String email = "newuser@example.com";
        String name = "New User";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        doReturn("jwt-token").when(jwtService).generateToken(any(AppUser.class), anyString(), any());

        String userInfoResponse = "{\"email\": \"" + email + "\", \"name\": \"" + name + "\"}";
        mockWebServer.enqueue(new MockResponse()
                .setBody(userInfoResponse)
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json"));

        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("client-id")
                .tokenUri(mockWebServer.url("/token").toString())
                .authorizationUri(mockWebServer.url("/auth").toString())
                .userInfoUri(mockWebServer.url("/userinfo").toString())
                .clientId("client-id")
                .clientSecret("client-secret")
                .redirectUri("http://localhost")
                .scope("read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userNameAttributeName("email")
                .build();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(TokenType.BEARER, "token", Instant.now(), Instant.now().plusSeconds(3600));
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        OAuth2User loadedUser = customOAuth2UserService.loadUser(userRequest);

        verify(userRepository).save(argThat(user -> user.getEmail().equals(email) &&
                user.getPassword().equals(DEFAULT_OAUTH2_PASSWORD) &&
                user.getRole().equals(Role.CUSTOMER)));

        Set<String> expectedAuthorities = Set.of("OAUTH2_USER", "CUSTOMER");
        Set<String> actualAuthorities = loadedUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        assertThat(actualAuthorities).containsExactlyInAnyOrderElementsOf(expectedAuthorities);
    }
}
