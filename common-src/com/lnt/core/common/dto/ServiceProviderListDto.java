package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.ServiceProvider;

@XmlRootElement
public class ServiceProviderListDto {

	private String userName;

	private String serviceProviderName;
	
	private String phoneNumber;
	
	private String altPhoneNumber;

	private String primaryEmailId;

	private String address;
	
	private String city;
	
	private String state;
	
	private String country;


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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAltPhoneNumber() {
		return altPhoneNumber;
	}

	public void setAltPhoneNumber(String altPhoneNumber) {
		this.altPhoneNumber = altPhoneNumber;
	}

	public String getPrimaryEmailId() {
		return primaryEmailId;
	}

	public void setPrimaryEmailId(String primaryEmailId) {
		this.primaryEmailId = primaryEmailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	

	
}
