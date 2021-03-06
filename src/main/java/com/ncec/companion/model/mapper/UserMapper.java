package com.ncec.companion.model.mapper;

import com.ncec.companion.model.dto.user.UserDto;
import com.ncec.companion.model.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class UserMapper extends AbstractMapper<UserEntity, UserDto> {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserMapper(ModelMapper modelMapper,
                      PasswordEncoder passwordEncoder) {
        super(modelMapper);
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initMapper() {
        mapper.createTypeMap(dtoClass, entityClass)
                .addMappings(m -> {
                    m.skip(UserEntity::setId);
                    m.skip(UserEntity::setPassword);
                })
                .setPostConverter(convertToEntity());

        mapper.createTypeMap(entityClass, dtoClass)
                .addMappings(m -> m.skip(UserDto::setPassword))
                .setPostConverter(convertToDto());
    }

    @Override
    protected void mapSpecificFields(UserDto source, UserEntity destination) {
        // may be null while updating entity without password update
        Optional.ofNullable(source.getPassword())
                .ifPresent(password -> destination.setPassword(passwordEncoder.encode(password)));
    }

    @Override
    protected void mapSpecificFields(UserEntity source, UserDto destination) {
        destination.setPassword(source.getPassword());
    }
}
