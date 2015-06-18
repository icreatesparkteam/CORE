package com.lnt.core.dao.impl;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.lnt.core.model.Role;

@Component
public class RoleDao extends AbstractAppDao<Role, Integer> {

	public Role findByName(String roleName) {
		Criterion criterion = Restrictions.eq("name", roleName);
		List<Role> objList = findByCriteria(criterion);

		if (objList != null && !objList.isEmpty())
			return objList.get(0);
		return null;
	}

}
