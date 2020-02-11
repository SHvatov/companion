package com.ncec.companion.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Object id) {
        super(String.format("Cannot find entity with such id: [%s]", id));
    }

    public EntityNotFoundException(Class<?> cls, Object id) {
        super(String.format("[%s] - cannot find entity with such id: [%s]", cls.getCanonicalName(), id));
    }
}
