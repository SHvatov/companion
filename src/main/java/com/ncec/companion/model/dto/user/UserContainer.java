package com.ncec.companion.model.dto.user;

import com.ncec.companion.model.enums.UserRole;

public interface UserContainer {
    String getEmail();

    void setEmail(String email);

    Integer getUser();

    void setUser(Integer id);

    String getPassword();

    void setPassword(String password);

    UserRole getRole();

    void setRole(UserRole role);
}
