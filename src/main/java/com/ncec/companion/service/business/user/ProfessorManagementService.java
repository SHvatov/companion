package com.ncec.companion.service.business.user;

import com.ncec.companion.model.dto.user.ProfessorDto;
import com.ncec.companion.service.crud.professor.ProfessorCrudService;
import com.ncec.companion.service.crud.user.UserCrudService;
import com.ncec.companion.service.mail.EmailSupportService;
import com.ncec.companion.service.security.PasswordGeneratorService;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

@Service
public class ProfessorManagementService
        extends AbstractUserManagementService<ProfessorDto, ProfessorCrudService>
        implements UserManagementService<ProfessorDto> {
    public ProfessorManagementService(ProfessorCrudService crudService,
                                      UserCrudService userCrudService,
                                      EmailSupportService emailSupportService,
                                      DozerBeanMapper dozerBeanMapper,
                                      PasswordGeneratorService passwordGeneratorService) {
        super(crudService, userCrudService, emailSupportService, dozerBeanMapper, passwordGeneratorService);
    }
}
