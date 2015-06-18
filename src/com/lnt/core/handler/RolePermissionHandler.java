package com.lnt.core.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lnt.core.common.dto.RolePermissionDto;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.manager.IRoleManager;
import com.lnt.core.manager.IRolePermissionManager;
import com.lnt.core.model.Role;
import com.lnt.core.model.RolePermission;

@Component
public class RolePermissionHandler implements IRolePermissionHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(RoleHandler.class);

	@Autowired
	private IRolePermissionManager rolePermissionMgr;
	@Autowired
	private IRoleManager roleMgr;

	@Override
	@Transactional
	public List<RolePermissionDto> getRolePermission(int id) throws ServiceApplicationException {
		logger.info("RoleHandler :  getRole by role id ");
		Role role = roleMgr.getRole(id);
//		RolePermissionDto rolePermission = rolePermissionMgr.getRolePermission(id);
//		if (rolePermission == null) {
//			throw new ServiceApplicationException("Role not found : ");
//		}
		return rolePermissionMgr.getRolePermission(role.getId());
	}
	
}
