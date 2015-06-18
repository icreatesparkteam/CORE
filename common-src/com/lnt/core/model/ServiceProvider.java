package com.lnt.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "serviceprovider_info")
public class ServiceProvider  extends AbstractTimeStampEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(name = "PASSWORD", length = 100)
	// @Type(type="com.lnt.core.encryption.AESEncryption")
	private String password;

	@Column(name = "USER_NAME", length = 50)
	private String userName;

	@Column(name = "SERVICEPROVIDER_NAME", length = 50)
	private String serviceProviderName;

	@Column(name = "PHONE_NUM", length = 50)
	private String phoneNum;
	
	@Column(name = "ALT_PHONE_NUM", length = 50)
	private String altPhoneNum;

	@Column(name = "ADDRESS", length = 300)
	private String address;
	
	@Column(name = "CITY", length = 300)
	private String city;
	
	@Column(name = "STATE", length = 300)
	private String state;
	
	@Column(name = "COUNTRY", length = 300)
	private String country;
	
	@Column(name = "ZIPCODE")
	private int zipCode;
	
	@Column(name = "PRIMARY_EMAIL_ID", length = 50)
	private String primaryEmailId;

	@Column(name = "ACTIVE", length = 10)
	private String active;

	@Column(name = "QUESTION")
	private String sQuestion;

	@Column(name = "ANSWER")
	private String sAnswer;

	@Column(name = "CHANGE_PASSWORD_BOOLEAN")
	private String changePassword;
	
	@Column(name = "ROLE", length = 10)
	private Integer role;

	@Column(name = "ATTEMPTS_LEFT")
	private int attemptsLeft;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public ServiceProvider() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String firstName) {
		this.serviceProviderName = firstName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.altPhoneNum = phoneNum;
	}
	
	public String getAltPhoneNum() {
		return altPhoneNum;
	}

	public void setAltPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
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
	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}


	public String getPrimaryEmailId() {
		return primaryEmailId;
	}

	public void setPrimaryEmailId(String primaryEmailId) {
		this.primaryEmailId = primaryEmailId;
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

	public String getChangePassword() {
		return changePassword;
	}

	public void setChangePassword(String changePassword) {
		this.changePassword = changePassword;
	}

	public int getAttemptsLeft() {
		return attemptsLeft;
	}

	public void setAttemptsLeft(int attemptsLeft) {
		this.attemptsLeft = attemptsLeft;
	}
	
	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

}
