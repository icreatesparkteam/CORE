package com.lnt.core.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "user_login_session")
public class UserLoginSession extends AbstractTimeStampEntity implements
		Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SESSION_ID", unique = true, nullable = false, length = 100)
	private String sessionId;

	@Column(name = "USER_ID", nullable = false)
	private int userId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGIN_TIME")
	private Date loginTime;

	@Column(name = "IP_ADDRESS", length = 20)
	private String ipAddress;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOGOUT_TIME")
	private Date logoutTime;

	@Column(nullable = false)
	private int status;

	public UserLoginSession() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getLoginTime() {
		return this.loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return this.logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}