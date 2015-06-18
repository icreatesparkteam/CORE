package com.lnt.core.common.dto;

import java.util.HashSet;
import java.util.Set;

import com.lnt.core.model.ServiceProvider;

public class ServiceProviderContextData {

	private ServiceProvider serviceProvider;

	Set<String> permissions = new HashSet<String>();

	public Set<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<String> permissions) {
		this.permissions = permissions;
	}

	public ServiceProvider getServiceProviderInfo() {
		return serviceProvider;
	}

	public void setServiceProviderInfo(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}
}
