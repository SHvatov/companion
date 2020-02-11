package com.ncec.companion.service.business.user;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.DataTransferObject;
import com.ncec.companion.model.dto.user.UserContainer;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.service.crud.CrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import com.ncec.companion.service.mail.EmailSupportService;
import com.ncec.companion.service.security.PasswordGeneratorService;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;

@RequiredArgsConstructor
public abstract class AbstractUserManagementService<
        D extends DataTransferObject & UserContainer,
        S extends CrudService<D>
        > implements UserManagementService<D> {
    protected final S crudService;
    private final UserCrudService userCrudService;
    private final EmailSupportService emailSupportService;
    private final DozerBeanMapper dozerBeanMapper;
    private final PasswordGeneratorService passwordGeneratorService;

    @Override
    public D save(D dto) {
        String password = passwordGeneratorService.generateRandomPassword();
        dto.setPassword(password);
        UserDto mappedUser = userCrudService.create(dozerBeanMapper.map(dto, UserDto.class));

        dto.setUser(mappedUser.getId());
        D createdUser;

        try {
            createdUser = crudService.create(dto);
        } catch (EntityNotFoundException | EntityAlreadyExistsException exception) {
            // in case of exception we should delete already created user entity
            userCrudService.delete(mappedUser.getId());
            throw exception;
        }

        emailSupportService.sendWelcomeMessage(mappedUser.getEmail(), password, mappedUser.getRole());
        return createdUser;
    }

    @Override
    public D update(D dto) {
        UserDto userDto = dozerBeanMapper.map(dto, UserDto.class);
        userDto.setId(dto.getUser());
        userCrudService.update(userDto);
        return crudService.update(dto);
    }

    @Override
    public void updatePassword(Integer userId) {
        UserDto user = userCrudService.findById(userId);

        String password = passwordGeneratorService.generateRandomPassword();
        user.setPassword(password);
        userCrudService.update(user);

        emailSupportService.sendUserPasswordUpdatedMessage(user.getEmail(), user.getEmail());
    }

    @Override
    public void delete(Integer id) {
        if (crudService.existsById(id)) {
            D dto = crudService.findById(id);
            UserDto user = userCrudService.findById(dto.getUser());
            emailSupportService.sendUserDeletedMessage(user.getEmail());
            crudService.delete(id);
        }
    }
}
