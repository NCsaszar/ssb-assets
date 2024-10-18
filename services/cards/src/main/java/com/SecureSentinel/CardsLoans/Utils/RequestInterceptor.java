package com.SecureSentinel.CardsLoans.Utils;

import com.SecureSentinel.CardsLoans.Service.ApiService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final ApiService apiService;

    @Autowired
    public RequestInterceptor(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());

            if (!checkActive(userId)) {
                System.out.println("User is not active or does not exist.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return false;
        }
        return true;
    }

    private boolean checkActive(Integer userId) {
        JsonNode jsonNode = apiService.getUserInfo(userId);
        return jsonNode != null && jsonNode.get("isActive").asBoolean();
    }
}

