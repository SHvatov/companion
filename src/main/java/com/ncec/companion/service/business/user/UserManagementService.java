package com.ncec.companion.service.business.user;

import com.ncec.companion.model.dto.DataTransferObject;
import com.ncec.companion.model.dto.user.UserContainer;

public interface UserManagementService<D extends DataTransferObject & UserContainer> {
    D save(D dto);

    D update(D dto);

    void updatePassword(Integer userId);

    void delete(Integer id);
}
