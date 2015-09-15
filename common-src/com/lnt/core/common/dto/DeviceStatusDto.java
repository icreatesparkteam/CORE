package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.DeviceStatus;

@XmlRootElement
public class DeviceStatusDto {

	private int id;

	private String deviceID;

	private String gatewayID;
	
	private String deviceStatus;
	
	private String hue;
	
	private String saturation;
	
	private String endPoint;
	
	private String level;
	
	public DeviceStatus toDeviceStatus(DeviceStatus device) throws ServiceApplicationException {
		device.setDeviceID(this.deviceID);
		device.setGatewayID(this.gatewayID);
		device.setDeviceStatus(this.deviceStatus);
		device.setHue(this.hue);
		device.setLevel(this.level);
		device.setEndpoint(this.endPoint);
		device.setSaturation(this.saturation);
		return device;
	}

	public DeviceStatusDto formDeviceStatus(DeviceStatus device) {
		this.id = device.getID();
		this.deviceID = device.getDeviceID();
		this.deviceStatus = device.getDeviceStatus();
		this.gatewayID = device.getGatewayID();
		this.hue = device.getHue();
		this.level = device.getLevl();
		this.endPoint = device.getEndpoint();
		this.saturation = device.getSaturation();
		return this;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public String getGatewayID() {
		return gatewayID;
	}

	public void setGatewayID(String gatewayID) {
		this.gatewayID = gatewayID;
	}

	public String getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getHue() {
		return hue;
	}

	public void setHue(String hue) {
		this.hue = hue;
	}

	public String getSaturation() {
		return saturation;
	}

	public void setSaturation(String saturation) {
		this.saturation = saturation;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	
}
