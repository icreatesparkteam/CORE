package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.DeviceStatus;

@XmlRootElement
public class DeviceStatusDto {

	private int id;

	private int deviceID;

	private int gatewayID;
	
	private String deviceStatus;
	
	private int intensity;
	
	public DeviceStatus toDeviceStatus(DeviceStatus device) throws ServiceApplicationException {
		device.setDeviceID(this.deviceID);
		device.setGatewayID(this.gatewayID);
		device.setDeviceStatus(this.deviceStatus);
		device.setIntensity(this.intensity);
		return device;
	}

	public DeviceStatusDto formDeviceStatus(DeviceStatus device) {
		this.id = device.getID();
		this.deviceID = device.getDeviceID();
		this.deviceStatus = device.getDeviceStatus();
		this.gatewayID = device.getGatewayID();
		this.intensity = device.getIntensity();
		return this;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public int getGatewayID() {
		return gatewayID;
	}

	public void setGatewayID(int gatewayID) {
		this.gatewayID = gatewayID;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	
}
