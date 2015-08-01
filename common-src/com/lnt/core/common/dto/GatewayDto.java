package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.Gateway;

@XmlRootElement
public class GatewayDto {

	private String gatewayID;

	private int serviceProviderID;
	
	private int userID;

	private String status;
	
	private String ipAddress;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getServiceProviderID() {
		return serviceProviderID;
	}

	public void setServiceProviderID(int serviceProviderID) {
		this.serviceProviderID = serviceProviderID;
	}

	
	public Gateway toGateway(Gateway gateway) throws ServiceApplicationException {
		gateway.setUserID(this.getUserID());
		gateway.setServiceProviderID(this.getServiceProviderID());
		gateway.setGatewayID(this.getGatewayID());
		gateway.setActive(this.getStatus());
		gateway.setIPAddress(this.ipAddress);
		return gateway;
	}

	public GatewayDto formGateway(Gateway gateway) {
		this.userID = gateway.getUserID();
		this.serviceProviderID = gateway.getServiceProviderID();
		this.ipAddress = gateway.getIPAddress();
		this.gatewayID = gateway.getGatewayID();
		this.status = gateway.getActive();
		return this;
	}

	public String getGatewayID() {
		return gatewayID;
	}

	public void setGatewayID(String gatewayID) {
		this.gatewayID = gatewayID;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
