package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.ServiceProvider;

@XmlRootElement
public class ServiceProviderListDto {

	private String userName;

	private String serviceProviderName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	


	public ServiceProvider toServiceProvider(ServiceProvider serviceProvider) throws ServiceApplicationException {
		serviceProvider.setUserName(this.getUserName());
		serviceProvider.setServiceProviderName(this.getServiceProviderName());
		return serviceProvider;
	}

	public ServiceProviderListDto fromServiceProvider(ServiceProvider serviceProvider) {
		this.userName = serviceProvider.getUserName();
		this.setServiceProviderName(serviceProvider.getServiceProviderName());
		return this;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	

	
}
