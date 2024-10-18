package com.smoothstack.userservice.model;

public enum Role {
    CUSTOMER,
    ADMIN,
    BANKER

} //Enums can be easily mapped to database columns as strings, representing the role names.
// This simplifies database operations related to roles, such as storing and retrieving them from the database.