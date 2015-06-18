package com.lnt.core.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lnt.core.common.dto.RolePermissionDto;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.dao.impl.PermissionDao;
import com.lnt.core.dao.impl.RolePermissionDao;
import com.lnt.core.model.Permission;
import com.lnt.core.model.RolePermission;

@Component
public class RolePermissionManager implements IRolePermissionManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RoleManager.class);

	@Autowired
	RolePermissionDao rolePermissDao;
	
	@Autowired
	PermissionDao permissDao;

	@Override
	public List<RolePermissionDto> getRolePermission(int id) throws ServiceApplicationException {
		logger.info("RolePermissionManager Fetching role id : {}", id);
				
		List<RolePermissionDto> rolePermissionDtoList = new ArrayList<>();
		List<RolePermission> rolePermissionList = rolePermissDao.findRolePermissionByID(id);
		
		if (rolePermissionList == null) {
			throw new ServiceApplicationException(
					"Bad request.No Permission available with the given role id "
							+ id);
		}
		Permission permission = null;
		for (RolePermission rolePermissionInfo : rolePermissionList) {
			RolePermissionDto dto = new RolePermissionDto();
			dto.setroleId(rolePermissionInfo.getRoleId());
			dto.setpermissionId(rolePermissionInfo.getPermissionId());
			
			permission = permissDao.findPermissionNameByID(rolePermissionInfo.getPermissionId());
			if(permission != null) {
				dto.setroleName(permission.getName());
			} else {
				dto.setroleName("Bad permission ID");
			}
			rolePermissionDtoList.add(dto);

		}
		return rolePermissionDtoList;

	}

}
