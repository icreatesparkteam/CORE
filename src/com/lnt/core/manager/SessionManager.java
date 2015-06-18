package com.lnt.core.manager;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lnt.core.common.util.ESessionStatus;
import com.lnt.core.common.util.TokenGenerator;
import com.lnt.core.dao.impl.UserLoginSessionDao;
import com.lnt.core.model.ServiceProvider;
import com.lnt.core.model.UserLoginSession;

@Component
public class SessionManager implements ISessionManager {

	private static final Logger logger = LoggerFactory
			.getLogger(SessionManager.class);

	@Autowired
	UserLoginSessionDao sessionDao;

	public void setSessionDao(UserLoginSessionDao sessionDao) {
		this.sessionDao = sessionDao;
	}

	@Override
	public String createSession(ServiceProvider serviceProvider) {
		logger.info("SessionManager Creating login session for user {}",
				serviceProvider.getUserName());
		String token = TokenGenerator.getInstance().generateToken(
				serviceProvider.getUserName());
		UserLoginSession session = new UserLoginSession();
		session.setSessionId(token);
		session.setUserId(serviceProvider.getId());
		session.setStatus(ESessionStatus.ACTIVE.ordinal());
		Date now = new Date();
		session.setLoginTime(now);
		sessionDao.create(session);
		return token;
	}

	@Override
	public UserLoginSession getUserSession(String sessionId) {
		logger.info("SessionManager Fetching session id : |{}|", sessionId);
		return sessionDao.findById(sessionId);

	}

	@Override
	public void expireSession(UserLoginSession session) {
		logger.info("SessionManager.expireSession, sessionId : {}",
				session.getSessionId());
		session.setStatus(ESessionStatus.EXPIRED.ordinal());
		Date now = new Date();
		session.setLogoutTime(now);
		sessionDao.update(session);
	}

}
