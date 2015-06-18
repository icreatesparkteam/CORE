package com.lnt.core.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.exception.ServiceRuntimeException;
import com.lnt.core.common.util.IConstants;
import com.lnt.core.handler.IAuthenticationHandler;
import com.lnt.core.handler.IRegistrationHandler;

@Component
@Path("/auth")
public class AuthenticationService {

	private static final Logger logger = LoggerFactory
			.getLogger(AuthenticationService.class);

	@Autowired
	private IAuthenticationHandler authHandler;

	@Autowired
	private IRegistrationHandler userHandler;

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticate(@FormParam("username") String userName,
			@FormParam("password") String password)
			throws ServiceApplicationException {
		logger.info("AuthenticationService authenticate method userName:"
				+ userName);
		String token = null;
		try {
			token = authHandler.authenticate(userName, password);
			return Response.ok().entity(token).build();
		} catch (ServiceApplicationException e) {
			logger.error("Application Exception while authenticating : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		} catch (ServiceRuntimeException e) {
			logger.error("Runtime Exception while login : {}", e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(
			@HeaderParam(IConstants.TOKEN_HEADER_KEY) String token) {
		logger.info("AuthenticationService logout method token");
		if (token != null) {
			try {
				logger.info("Logout request recieved for token : " + token);
				authHandler.logout(token);
				return Response.ok().entity("Logedout successfully").build();
			} catch (ServiceRuntimeException e) {
				logger.error("Runtime Exception while logout : {}",
						e.getMessage());
				return Response.status(e.getCode()).entity(e.getMessage())
						.build();
			} catch (ServiceApplicationException e) {
				logger.error("application Exception while logout : {}",
						e.getMessage());
				return Response.status(e.getCode()).entity(e.getMessage())
						.build();
			}
		} else {
			logger.info("LogoutService:logout, empty token");
			return Response.status(400)
					.entity("Token not present in logout request").build();
		}
	}

}
