package com.lnt.core.handler;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lnt.core.common.cache.SessionCache;
import com.lnt.core.common.dto.SessionInfo;
import com.lnt.core.common.exception.InvalidTokenException;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.exception.TokenExpiredException;
import com.lnt.core.common.util.DateUtil;
import com.lnt.core.common.util.ESessionStatus;
import com.lnt.core.common.util.IConstants;
import com.lnt.core.manager.ISessionManager;
import com.lnt.core.manager.IRegistrationManager;
import com.lnt.core.model.ServiceProvider;

import com.lnt.core.model.UserLoginSession;

@Component
public class SessionHandler implements ISessionHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(SessionHandler.class);

	@Autowired
	ISessionManager sessionMgr;

	@Autowired
	IRegistrationManager regMgr;

	@Override
	@Transactional
	public UserLoginSession getSession(String sessionId)
			throws TokenExpiredException {
		logger.info("SessionHandler : getSession sessionId : " + sessionId);
		UserLoginSession session = sessionMgr.getUserSession(sessionId);
		if (session != null) {
			Date loginTime = session.getLoginTime();
			if (session.getStatus() == ESessionStatus.EXPIRED.ordinal()
					|| DateUtil.isExpired(loginTime,
							IConstants.TOKEN_EXPIRY_DURATION)) {
				sessionMgr.expireSession(session);
				throw new TokenExpiredException("Token Expired : " + sessionId);
			}
		}

		return session;
	}

	@Override
	@Transactional
	public SessionInfo getSessionInfo(String sessionId)
			throws InvalidTokenException, TokenExpiredException {
		logger.info("SessionHandler : getSessionInfo sessionId : " + sessionId);
		UserLoginSession session = sessionMgr.getUserSession(sessionId);

		if (session == null) {
			logger.info("SessionHandler Token provided is Invalid : "
					+ sessionId);
			throw new InvalidTokenException("Token provided is Invalid : "
					+ sessionId);
		}

		Date loginTime = session.getLoginTime();
		if (session.getStatus() == ESessionStatus.EXPIRED.ordinal()
				|| DateUtil.isExpired(loginTime,
						IConstants.TOKEN_EXPIRY_DURATION)) {
			sessionMgr.expireSession(session);
			throw new TokenExpiredException("Token Expired : " + sessionId);
		}

		SessionInfo sessionInfo = SessionCache.getInstance().get(sessionId);
		if (sessionInfo == null) {
			sessionInfo = new SessionInfo();
			logger.info(
					"SessionHandler Session {} not found in cache. Retrieve from DB",
					sessionId);
			int id = session.getUserId();
			logger.info("SessionHandler user id   {} ", id);
			ServiceProvider serviceProvider = regMgr.getServiceProviderById(id);
			sessionInfo.setServiceProvider(serviceProvider);
			sessionInfo.setLastAccessedTime(System.currentTimeMillis());
			SessionCache.getInstance().put(sessionId, sessionInfo);
		}

		sessionInfo.setSession(session);
		logger.info("SeesionHandler : return sessionInfo : {}", sessionInfo);
		return sessionInfo;

	}

}
