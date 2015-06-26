package com.lnt.core.handler;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lnt.core.annotations.WriteTransaction;
import com.lnt.core.common.exception.AuthenticationException;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.util.IConstants;
import com.lnt.core.manager.ISessionManager;
import com.lnt.core.manager.IRegistrationManager;
import com.lnt.core.model.ServiceProvider;
import com.lnt.core.model.UserLoginSession;

@Component
public class AuthenticationHandler implements IAuthenticationHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(AuthenticationHandler.class);
	@Autowired
	private IRegistrationManager regMgr;

	@Autowired
	private ISessionManager sessionMgr;

	@Override
	@Transactional
	public String authenticate(String userName, String password)
			throws ServiceApplicationException {
		logger.info("AuthenticationHandler.authenticate - user : {}", userName);
		ServiceProvider serviceProvider = regMgr.getServiceProvider(userName);
		if (serviceProvider == null) {
			throw new AuthenticationException(
					"Authentication failed : Unable to find ServiceProvider : " + userName,
					401);
		}

		if (0 == (serviceProvider.getActive())) {
			throw new AuthenticationException(
					"Authentication failed :User marked as deleted  : "
							+ userName, 401);
		}

		if (serviceProvider.getAttemptsLeft() == 0) {
			Date lastUpdated = serviceProvider.getUpdated();
			Date currentTime = new Date();
			long diff = currentTime.getTime() - lastUpdated.getTime();
			long diffMinutes = diff / (60 * 1000) % 60;
			
			if (diffMinutes != IConstants.SESSION_INACTIVE_TIME) {
				throw new AuthenticationException(
						"You have crossed your maximum no. of attempts for login. Please try after "
								+ (IConstants.SESSION_INACTIVE_TIME - diffMinutes)
								+ " minute(s)",
						HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
			} else {
				serviceProvider.setAttemptsLeft(IConstants.LOGIN_ATTEMPTS);
				regMgr.updateServiceProvider(serviceProvider);
			}
		}

		String result = validatePassword(password, serviceProvider);
		if (!"Success".equals(result)) {
			serviceProvider.setAttemptsLeft(serviceProvider.getAttemptsLeft() - 1);
			regMgr.updateServiceProvider(serviceProvider);
			throw new AuthenticationException(
					"Please Enter valid login credentials",
					HttpStatus.NON_AUTHORITATIVE_INFORMATION.value());
		}
		String token = getToken(serviceProvider);
		return token;
	}

	@Override
	@WriteTransaction
	public void logout(String token) throws ServiceApplicationException {
		logger.info("AuthenticationHandler.logout, token : {}", token);
		UserLoginSession session = sessionMgr.getUserSession(token);
		if (session == null) {
			throw new ServiceApplicationException(
					HttpStatus.BAD_REQUEST.value(), "Invalid Token");
		}
		sessionMgr.expireSession(session);
	}

	public String validatePassword(String password, ServiceProvider user)
			throws ServiceApplicationException {
		boolean valid = false;
		String result = "Success";
		String userName = user.getUserName();
		valid = user.getPassword().equals(password);
		if (!valid) {
			logger.info("Authentication failed : wrong password for user "
					+ userName);
			result = "Wrong password.";
		}
		return result;
	}

	@WriteTransaction
	private String getToken(ServiceProvider serviceProvider) {
		return sessionMgr.createSession(serviceProvider);
	}

}
