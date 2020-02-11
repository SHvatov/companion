package com.ncec.companion.service.crud.user;

import com.ncec.companion.exception.EntityAlreadyExistsException;
import com.ncec.companion.exception.EntityNotFoundException;
import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.entity.UserEntity;
import com.ncec.companion.model.enums.UserRole;
import com.ncec.companion.model.mapper.Mapper;
import com.ncec.companion.model.repository.UserRepository;
import com.ncec.companion.service.crud.AbstractCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserCrudServiceImpl
        extends AbstractCrudService<UserEntity, UserDto, UserRepository>
        implements UserCrudService {
    @Autowired
    public UserCrudServiceImpl(UserRepository repository, Mapper<UserEntity, UserDto> mapper) {
        super(repository, mapper);
    }

    @Override
    public UserDto create(UserDto dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new EntityAlreadyExistsException(UserEntity.class, dto.getEmail());
        }

        UserEntity userEntity = new UserEntity();
        mapper.map(dto, userEntity);
        return mapper.map(repository.save(userEntity));
    }

    @Override
    public UserDto update(UserDto dto) {
        UserEntity userEntity = repository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, dto.getId()));

        if (!userEntity.getEmail().equals(dto.getEmail())
                && repository.existsByEmail(dto.getEmail())) {
            throw new EntityAlreadyExistsException(UserEntity.class, dto.getEmail());
        }

        mapper.map(dto, userEntity);
        return mapper.map(repository.save(userEntity));
    }

    @Override
    public void delete(Integer id) {
        repository.findById(id).ifPresent(repository::delete);
    }

    @Override
    public Set<UserDto> findAllWithRole(UserRole role) {
        return repository.findAllByRole(role)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toSet());
    }

    @Override
    public UserDto findByEmail(String email) {
        return mapper.map(repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(UserEntity.class, email)));
    }

    @Override
    public UserDto getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByEmail(authentication.getName());
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
