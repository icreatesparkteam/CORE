package com.lnt.core.handler;

import com.lnt.core.common.dto.SessionInfo;
import com.lnt.core.common.exception.InvalidTokenException;
import com.lnt.core.common.exception.TokenExpiredException;
import com.lnt.core.model.UserLoginSession;

public interface ISessionHandler {

	public UserLoginSession getSession(String sessionId)
			throws TokenExpiredException;

	public SessionInfo getSessionInfo(String sessionId)
			throws InvalidTokenException, TokenExpiredException;

}
