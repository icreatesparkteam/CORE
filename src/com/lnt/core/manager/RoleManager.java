package com.lnt.core.manager;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.dao.impl.RoleDao;
import com.lnt.core.model.Role;

@Component
public class RoleManager implements IRoleManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RoleManager.class);

	@Autowired
	RoleDao roleDao;

	@Override
	public Role getRole(String name) throws ServiceApplicationException {
		logger.info("RoleManager Fetching role name : {}", name);
		return roleDao.findByName(name);

	}

	@Override
	public Role getRole(int id) throws ServiceApplicationException {
		logger.info("RoleManager Fetching role id : {}", id);
		return roleDao.findById(id);

	}

	@Override
	public List<Role> getAllRolesList() throws ServiceApplicationException {
		logger.info("RoleManager getAllRolesList");
		return roleDao.findAll();
	}

}
