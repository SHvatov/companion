package com.ncec.companion.service.crud.group;

import com.ncec.companion.model.dto.GroupDto;
import com.ncec.companion.model.enums.WeekDay;
import com.ncec.companion.service.crud.CrudService;

import java.util.Set;

public interface GroupCrudService extends CrudService<GroupDto> {
    Set<GroupDto> findLeisureGroups(WeekDay weekDay, Integer start, Integer duration);

    GroupDto findByNumber(String number);
}
