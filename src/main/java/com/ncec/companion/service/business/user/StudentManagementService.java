package com.ncec.companion.service.business.user;

import com.ncec.companion.model.dto.user.StudentDto;
import com.ncec.companion.service.crud.student.StudentCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import com.ncec.companion.service.mail.EmailSupportService;
import com.ncec.companion.service.security.PasswordGeneratorService;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

@Service
public class StudentManagementService
        extends AbstractUserManagementService<StudentDto, StudentCrudService>
        implements UserManagementService<StudentDto> {
    public StudentManagementService(StudentCrudService crudService,
                                    UserCrudService userCrudService,
                                    EmailSupportService emailSupportService,
                                    DozerBeanMapper dozerBeanMapper,
                                    PasswordGeneratorService passwordGeneratorService) {
        super(crudService, userCrudService, emailSupportService, dozerBeanMapper, passwordGeneratorService);
    }
}

