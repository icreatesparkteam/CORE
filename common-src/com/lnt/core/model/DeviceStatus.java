package com.lnt.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device_status")
public class DeviceStatus  extends AbstractTimeStampEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(name = "DEVICE_ID", length = 10)
	private String deviceID;

	@Column(name = "GATEWAY_ID", length = 10)
	private String gatewayID;
	
	@Column(name = "DEVICE_STATUS", length = 50)
	private String status;
	
	@Column(name = "DEVICE_HUE", length = 10)
	private String hue;
	
	@Column(name = "DEVICE_SATURATION", length = 10)
	private String saturation;
	
	@Column(name = "DEVICE_ENDPOINT", length = 10)
	private String endPoint;
	
	@Column(name = "DEVICE_LEVEL", length = 10)
	private String level;
	
	public DeviceStatus() {
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
		return status;
	}

	public void setDeviceStatus(String deviceStatus) {
		this.status = deviceStatus;
	}
	
	public int getID() {
		return id;
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
	
	public String getLevl() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	public String getEndpoint() {
		return endPoint;
	}

	public void setEndpoint(String endPoint) {
		this.endPoint = endPoint;
	}


}
