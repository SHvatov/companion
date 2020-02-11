package com.ncec.companion.service.crud.user;

import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.enums.UserRole;
import com.ncec.companion.service.crud.CrudService;

import java.util.Set;

public interface UserCrudService extends CrudService<UserDto> {
    Set<UserDto> findAllWithRole(UserRole role);

    UserDto findByEmail(String email);

    UserDto getUserFromSecurityContext();

    boolean existByEmail(String email);
}
