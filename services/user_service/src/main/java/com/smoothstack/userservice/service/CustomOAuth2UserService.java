package com.smoothstack.userservice.service;

import com.smoothstack.userservice.model.AppUser;
import com.smoothstack.userservice.model.Role;
import com.smoothstack.userservice.repository.AppUserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final AppUserRepository userRepository;
    private final JwtService jwtService;

    private static final String DEFAULT_OAUTH2_PASSWORD = "$2a$10$eEgz5Q3U8Bxz13oLBZHzrukHZ6mJpTAUOuMdPPIkhPglJ806luJ16";

    public CustomOAuth2UserService(AppUserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        Optional<AppUser> userOptional = userRepository.findByEmail(email);

        AppUser user;
        if (userOptional.isEmpty()) {
            user = new AppUser();
            user.setEmail(email);
            user.setUsername(generateUniqueUsername(name));
            user.setPassword(DEFAULT_OAUTH2_PASSWORD);
            user.setRole(Role.CUSTOMER);
            userRepository.save(user);
        } else {
            user = userOptional.get();
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("OAUTH2_USER"));
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        System.out.println("authorities:"+ authorities);
        String jwtToken = jwtService.generateToken(user, user.getRole().name(), user.getUserId());

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Cookie cookie = new Cookie("token", jwtToken);
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(cookie);

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "name");
    }

    private String generateUniqueUsername(String baseName) {
        String username = baseName;
        Random random = new Random();
        while (userRepository.findByUsername(username).isPresent()) {
            username = baseName + random.nextInt(10000);
        }
        return username;
    }
}
