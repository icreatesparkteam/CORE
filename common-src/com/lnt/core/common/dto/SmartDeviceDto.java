package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.SmartDevice;

@XmlRootElement
public class SmartDeviceDto {

	private int gatewayID;

	private String deviceID;
	
	private String status;
	
	private String manufacturerID;
	
	private int deviceStatus;

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
		smartDevice.setDeviceStatus(this.deviceStatus);
		smartDevice.setManufacturerID(this.manufacturerID);
		return smartDevice;
	}

	public SmartDeviceDto formGateway(SmartDevice smartDevice) {
		this.gatewayID = smartDevice.getGatewayID();
		this.deviceID = smartDevice.getDeviceID();
		this.status = smartDevice.getActive();
		this.manufacturerID = smartDevice.getManufacturerID();
		this.deviceStatus = smartDevice.getDeviceStatus();
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

	public String getManufacturerID() {
		return manufacturerID;
	}

	public void setManufacturerID(String manufacturerID) {
		this.manufacturerID = manufacturerID;
	}

	public int getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(int deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

}
