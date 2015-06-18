package com.lnt.core.manager;

import com.lnt.core.model.ServiceProvider;
import com.lnt.core.model.UserLoginSession;

public interface ISessionManager {

	public String createSession(ServiceProvider serviceProvider);

	public UserLoginSession getUserSession(String sessionId);

	void expireSession(UserLoginSession session);
}
