package com.lnt.core.manager;

import java.util.List;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.Role;

public interface IRoleManager {

	Role getRole(String name) throws ServiceApplicationException;

	Role getRole(int id) throws ServiceApplicationException;

	List<Role> getAllRolesList() throws ServiceApplicationException;

}
