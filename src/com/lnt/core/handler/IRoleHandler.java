package com.lnt.core.handler;

import java.util.List;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.Role;

public interface IRoleHandler {

	List<Role> getAllRolesList() throws ServiceApplicationException;

	Role getRole(String name) throws ServiceApplicationException;

	Role getRole(int id) throws ServiceApplicationException;

}
