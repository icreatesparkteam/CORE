package com.lnt.core.handler;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.ServiceProvider;

public interface IAuthenticationHandler {

	public String authenticate(String userName, String password)
			throws ServiceApplicationException;

	public void logout(String token) throws ServiceApplicationException;

	public String validatePassword(String password, ServiceProvider serviceProvider)
			throws ServiceApplicationException;

}
