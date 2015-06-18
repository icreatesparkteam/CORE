package com.lnt.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.lnt.core.model.RolePermission;

@Repository
@Component
public class RolePermissionDao extends AbstractAppDao<RolePermission, Integer> {
	
	private static final Logger logger = LoggerFactory
			.getLogger(RolePermissionDao.class);
	
	public List<RolePermission> findByRoleId(int roleId) {
		Criterion criterion = Restrictions.eq("roleId", roleId);
		List<RolePermission> objList = findByCriteria(criterion);
		
		return objList;
	}
	
	public List<RolePermission> findRolePermissionByID(int roleId) {
		logger.info("RolePermissionDao Fetching role id : {}", roleId);
		Criterion criterion = Restrictions.eq("roleId", roleId);
		List<RolePermission> objList = findByCriteria(criterion);

		return objList;
	}
}
