package com.lnt.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.lnt.core.model.Permission;
import com.lnt.core.model.RolePermission;

@Repository
@Component
public class PermissionDao extends AbstractAppDao<Permission, Integer> {
	
	public Permission findPermissionNameByID(int roleId) {
		Criterion criterion = Restrictions.eq("roleId", roleId);
		Permission permission = findById(roleId);

		return permission;
	}
}
