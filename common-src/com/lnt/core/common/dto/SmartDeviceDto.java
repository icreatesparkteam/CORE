package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.SmartDevice;

@XmlRootElement
public class SmartDeviceDto {

	private int gatewayID;

	private String deviceID;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public SmartDevice toServiceProvider(SmartDevice smartDevice) throws ServiceApplicationException {
		smartDevice.setGatewayID(this.getGatewayID());
		smartDevice.setActive(this.getStatus());
		smartDevice.setDeviceID(this.getDeviceID());
		return smartDevice;
	}

	public SmartDeviceDto formGateway(SmartDevice smartDevice) {
		this.gatewayID = smartDevice.getGatewayID();
		this.deviceID = smartDevice.getDeviceID();
		this.status = smartDevice.getActive();
		return this;
	}

	public int getGatewayID() {
		return gatewayID;
	}

	public void setGatewayID(int gatewayID) {
		this.gatewayID = gatewayID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

}
