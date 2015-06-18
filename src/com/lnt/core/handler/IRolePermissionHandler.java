package com.lnt.core.handler;


import java.util.List;

import com.lnt.core.common.dto.RolePermissionDto;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.RolePermission;

public interface IRolePermissionHandler {

	List<RolePermissionDto> getRolePermission(int id) throws ServiceApplicationException;

}
