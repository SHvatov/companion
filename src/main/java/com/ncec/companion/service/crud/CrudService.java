package com.ncec.companion.service.crud;

import com.ncec.companion.model.dto.DataTransferObject;

import java.util.Set;

public interface CrudService<D extends DataTransferObject> {
    D create(D dto);

    D update(D dto);

    void delete(Integer id);

    D findById(Integer id);

    boolean existsById(Integer id);

    Set<D> findAll(Integer page, Integer size);

    Set<D> findAll();

    long count();
}
