package com.lnt.core.manager;


import java.util.List;

import com.lnt.core.common.dto.RolePermissionDto;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.RolePermission;;

public interface IRolePermissionManager {

	List<RolePermissionDto> getRolePermission(int id) throws ServiceApplicationException;

	
}