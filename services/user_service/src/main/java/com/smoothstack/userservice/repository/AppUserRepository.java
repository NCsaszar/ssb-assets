package com.smoothstack.userservice.repository;
import com.smoothstack.userservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer>, JpaSpecificationExecutor<AppUser> {

    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email);
}//JpaRepository provides built-in methods for basic CRUD (Create, Read, Update, Delete) operations on the AppUser entity.
// These methods include save, findById, findAll, delete, and more.
// You can use these methods to interact with the database without writing custom SQL queries.

//JpaSpecificationExecutor allows you to build and execute dynamic queries
// on JPA entity without writing SQL queries manually.