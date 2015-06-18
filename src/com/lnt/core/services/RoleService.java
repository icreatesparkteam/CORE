package com.lnt.core.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.exception.ServiceRuntimeException;
import com.lnt.core.handler.IRoleHandler;
import com.lnt.core.model.Role;


@Component
@Path("/role")
public class RoleService {

	private static final Logger logger = LoggerFactory
			.getLogger(RoleService.class);

	@Autowired
	private IRoleHandler roleHandler;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getList")
	public Response getAllRolesList() {
		logger.info("RoleService getAllRolesList method");
		try {
			List<Role> roleList = roleHandler.getAllRolesList();
			return Response.ok().entity(roleList).build();
		} catch (ServiceRuntimeException e) {
			logger.error("Runtime Exception while getting Roles list  : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		} catch (ServiceApplicationException e) {
			logger.error("Application Exception while getting Roles list : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/get/{rolename}")
	public Response getRoleByName(@PathParam("rolename") String roleName) {
		logger.info("RoleService getRoleByName method");
		try {
			Role role = roleHandler.getRole(roleName);
			return Response.ok().entity(role).build();
		} catch (ServiceRuntimeException e) {
			logger.error("Runtime Exception while getRoleByName  : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		} catch (ServiceApplicationException e) {
			logger.error("Application Exception while getRoleByName : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		}
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getbyid/{roleid}")
	public Response getRoleById(@PathParam("roleid") int roleId) {
		logger.info("RoleService getRoleById method");
		try {
			Role role = roleHandler.getRole(roleId);
			return Response.ok().entity(role).build();
		} catch (ServiceRuntimeException e) {
			logger.error("Runtime Exception while getRoleById  : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		} catch (ServiceApplicationException e) {
			logger.error("Application Exception while getRoleById : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		}
	}
	
}
