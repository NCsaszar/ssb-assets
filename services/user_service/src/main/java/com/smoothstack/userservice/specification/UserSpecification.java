package com.smoothstack.userservice.specification;

import com.smoothstack.userservice.model.AppUser;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class UserSpecification {

    public static Specification<AppUser> usernameContains(String username) {
        return (root, query, criteriaBuilder) -> {
            if (username == null || username.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("username"), "%" + username + "%");
        };
    }

    public static Specification<AppUser> emailContains(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("email"), "%" + email + "%");
        };
    }

    public static Specification<AppUser> firstNameContains(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null || firstName.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
        };
    }

    public static Specification<AppUser> lastNameContains(String lastName) {
        return (root, query, criteriaBuilder) -> {
            if (lastName == null || lastName.isEmpty()) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%");
        };
    }
}