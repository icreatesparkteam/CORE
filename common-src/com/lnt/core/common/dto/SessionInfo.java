package com.lnt.core.common.dto;

import java.util.List;

import com.lnt.core.model.Permission;
import com.lnt.core.model.ServiceProvider;
import com.lnt.core.model.UserLoginSession;

public class SessionInfo {

	private UserLoginSession session;

	private ServiceProvider serviceProvider;

	private long lastAccessedTime;

	private List<Permission> permissions;
	
	public UserLoginSession getSession() {
		return session;
	}

	public void setSession(UserLoginSession session) {
		this.session = session;
	}

	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

}
