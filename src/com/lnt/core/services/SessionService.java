package com.lnt.core.services;

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

import com.lnt.core.common.dto.SessionInfo;
import com.lnt.core.common.exception.InvalidTokenException;
import com.lnt.core.common.exception.ServiceRuntimeException;
import com.lnt.core.common.exception.TokenExpiredException;
import com.lnt.core.handler.ISessionHandler;
import com.lnt.core.handler.IRegistrationHandler;

@Component
@Path("/session")
public class SessionService {

	private static final Logger logger = LoggerFactory
			.getLogger(SessionService.class);

	@Autowired
	private ISessionHandler sessionHandler;

	@Autowired
	private IRegistrationHandler userHandler;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Response getSessionInfo(@PathParam("id") String sessionId) {
		logger.info(
				"SessionService getSessionInfo method Fetching session with session id : {}",
				sessionId);
		try {
			SessionInfo session = sessionHandler.getSessionInfo(sessionId);
			return Response.ok().entity(session).build();
		} catch (InvalidTokenException | TokenExpiredException e) {
			logger.error("Token Exception while getSessionInfo : {}",
					e.getMessage());
			return Response.status(400).entity(e.getMessage()).build();
		} catch (ServiceRuntimeException e) {
			logger.error("Runtime Exception while getSessionInfo : {}",
					e.getMessage());
			return Response.status(e.getCode()).entity(e.getMessage()).build();
		}
	}

}
