package com.lnt.core.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lnt.core.common.dto.ServiceProviderContextData;

public class ServiceProviderInRequest {
	private static Logger logger = LoggerFactory.getLogger(ServiceProviderInRequest.class);

	private static ServiceProviderInRequest instance;

	private ThreadLocal<ServiceProviderContextData> serviceProvider = new ThreadLocal<ServiceProviderContextData>();

	private ServiceProviderInRequest() {

	}

	public static synchronized ServiceProviderInRequest getInstance() {
		if (instance == null) {
			logger.info("Instantiating the threadlocal container class");
			instance = new ServiceProviderInRequest();
		}
		return instance;
	}

	public void setServiceProviderContext(ServiceProviderContextData serviceProvider) {
		logger.info(
				"Setting the serviceProvider --- [{}] -- information of logged in user to the thread local context",
				serviceProvider.getServiceProviderInfo().getId());
		this.serviceProvider.set(serviceProvider);
	}

	public ServiceProviderContextData getServiceProviderContext() {
		return this.serviceProvider.get();
	}

	public void clearContext() {
		if (this.serviceProvider != null) {
			logger.info("Clearing user context");
			this.serviceProvider.remove();
		}
	}

}
