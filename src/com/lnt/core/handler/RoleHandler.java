package com.lnt.core.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.manager.IRoleManager;
import com.lnt.core.model.Role;

@Component
public class RoleHandler implements IRoleHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(RoleHandler.class);

	@Autowired
	private IRoleManager roleMgr;

	@Override
	@Transactional
	public List<Role> getAllRolesList() throws ServiceApplicationException {
		logger.info("RoleHandler getAllRolesList method");
		return roleMgr.getAllRolesList();
	}

	@Override
	public Role getRole(String roleName) throws ServiceApplicationException {
		logger.info("RoleHandler :  getRole by role name ");
		if (roleName == null || roleName.isEmpty()) {
			throw new ServiceApplicationException("Invalid Role name ");
		}
		Role role = roleMgr.getRole(roleName);
		if (role == null) {
			throw new ServiceApplicationException("Role not found : ");
		}
		return role;
	}

	@Override
	public Role getRole(int id) throws ServiceApplicationException {
		logger.info("RoleHandler :  getRole by role id ");
		Role role = roleMgr.getRole(id);
		if (role == null) {
			throw new ServiceApplicationException("Role not found : ");
		}
		return role;
	}
	
}
