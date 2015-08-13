package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.ServiceProvider;

@XmlRootElement
public class ServiceProviderRegistrationDto {

	private String userName;

	private String password;

	private String serviceProviderName;

	private String phoneNumber;
	
	private String altPhoneNumber;

	private String primaryEmailId;

	private int status;

	private String address;
	
	private String city;
	
	private String state;
	
	private String country;

	private String changePassword;
	
	private int role;
	
	private String sQuestion;

	private String sAnswer;
	
	private int id;

	public String getChangePassword() {
		return changePassword;
	}

	public void setChangePassword(String changePassword) {
		this.changePassword = changePassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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


	public ServiceProvider toServiceProvider(ServiceProvider serviceProvider) throws ServiceApplicationException {
		serviceProvider.setUserName(this.getUserName());
		serviceProvider.setServiceProviderName(this.getServiceProviderName());
		serviceProvider.setPhoneNum(this.getPhoneNumber());
		serviceProvider.setAltPhoneNum(this.getAltPhoneNumber());
		serviceProvider.setPrimaryEmailId(this.getPrimaryEmailId());
		serviceProvider.setAddress(this.getAddress());
		serviceProvider.setCity(this.getCity());
		serviceProvider.setState(this.getState());
		serviceProvider.setCountry(this.getCountry());
		serviceProvider.setsQuestion(this.getsQuestion());
		serviceProvider.setsAnswer(this.getsAnswer());
		return serviceProvider;
	}

	public ServiceProviderRegistrationDto fromServiceProvider(ServiceProvider serviceProvider) {
		this.userName = serviceProvider.getUserName();
		this.serviceProviderName = serviceProvider.getServiceProviderName();
		this.address = serviceProvider.getAddress();
		this.city = serviceProvider.getCity();
		this.state = serviceProvider.getState();
		this.country = serviceProvider.getCountry();
		this.phoneNumber = serviceProvider.getPhoneNum();
		this.altPhoneNumber = serviceProvider.getAltPhoneNum();
		this.primaryEmailId = serviceProvider.getPrimaryEmailId();
		this.changePassword = serviceProvider.getChangePassword();
		this.sQuestion = serviceProvider.getsQuestion();
		this.sAnswer = serviceProvider.getsAnswer();
		return this;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getsQuestion() {
		return sQuestion;
	}

	public void setsQuestion(String sQuestion) {
		this.sQuestion = sQuestion;
	}

	public String getsAnswer() {
		return sAnswer;
	}

	public void setsAnswer(String sAnswer) {
		this.sAnswer = sAnswer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
