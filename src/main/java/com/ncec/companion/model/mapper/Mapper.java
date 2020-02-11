package com.ncec.companion.model.mapper;

import com.ncec.companion.model.dto.DataTransferObject;
import com.ncec.companion.model.entity.AbstractDataBaseEntity;

public interface Mapper<E extends AbstractDataBaseEntity, D extends DataTransferObject> {
    D map(E source);

    void map(D source, E destination);
}
