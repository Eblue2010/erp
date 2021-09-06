package io.eliteblue.erp.core.service;

import java.util.List;

public interface CoreErpService<T, ID> {

    public List<T> getAll();

    public T findById(ID id);

    public void delete(T t);

    public void save(T t);
}
