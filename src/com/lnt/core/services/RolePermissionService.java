package com.lnt.core.services;

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

import com.lnt.core.common.dto.RolePermissionDto;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.exception.ServiceRuntimeException;
import com.lnt.core.handler.IRolePermissionHandler;
import com.lnt.core.model.RolePermission;

@Component
@Path("/rolepermission")
public class RolePermissionService {

	private static final Logger logger = LoggerFactory
			.getLogger(RolePermissionService.class);

	@Autowired
	private IRolePermissionHandler rolePermissionHandler;

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getrolepermissionbyid/{roleid}")
	public Response getRoleById(@PathParam("roleid") int roleId) {
		logger.info("RoleService getRoleById method");
		try {
			List<RolePermissionDto> rolePermission = rolePermissionHandler.getRolePermission(roleId);
			return Response.ok().entity(rolePermission).build();
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
