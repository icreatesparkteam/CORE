package com.lnt.core.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lnt.core.common.exception.ServiceRuntimeException;
import com.lnt.core.dao.IDao;

public abstract class AbstractAppDao<E, K> implements IDao<E, K> {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractAppDao.class);
	protected Class<E> entityClass;

	@PersistenceContext(unitName = "iControlE-Core")
	protected EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public AbstractAppDao() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass()
				.getGenericSuperclass();
		this.entityClass = (Class<E>) genericSuperclass
				.getActualTypeArguments()[0];
	}

	@Override
	public void create(E entity) {
		logger.info("AbstractAppDao create : ");
		try {
			entityManager.persist(entity);			
		} catch (PersistenceException e) {
			logger.error("AbstractAppDao error while creating : "
					+ e.getMessage());
			throw new ServiceRuntimeException(e);
		}
	}	

	@Override
	public void remove(E entity) {
		entityManager.remove(entity);
	}

	@Override
	public E findById(K id) {
		return entityManager.find(entityClass, id);
	}

	@Override
	public E update(E entity) {
		try {
			return entityManager.merge(entity);
		} catch (PersistenceException e) {
			throw new ServiceRuntimeException("Update Fail: ", e);
		}
	}

	@Override
	public List<E> findAll() {
		return findByCriteria();
	}

	protected List<E> findByCriteria(final Criterion... criterion) {
		return findByCriteria(-1, -1, criterion);
	}

	@SuppressWarnings("unchecked")
	protected List<E> findByCriteria(final int firstResult,
			final int maxResults, final Criterion... criterion) {

		Session session = (Session) entityManager.getDelegate();
		Criteria crit = session.createCriteria(entityClass);

		for (final Criterion c : criterion)
			crit.add(c);
		if (firstResult > 0)
			crit.setFirstResult(firstResult);
		if (maxResults > 0)
			crit.setMaxResults(maxResults);

		final List<E> result = crit.list();
		return result;
	}

}
