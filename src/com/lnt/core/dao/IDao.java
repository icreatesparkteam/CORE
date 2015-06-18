package com.lnt.core.dao;

import java.util.List;

import com.lnt.core.common.exception.ServiceRuntimeException;

public interface IDao<E, K> {
	public void create(E entity) throws ServiceRuntimeException;

	public void remove(E entity);

	public E findById(K id);

	public E update(E entity) throws ServiceRuntimeException;

	public List<E> findAll();
	
}
