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
	private int deviceID;

	@Column(name = "GATEWAY_ID", length = 10)
	private int gatewayID;
	
	@Column(name = "DEVICE_STATUS", length = 50)
	private String deviceStatus;
	
	@Column(name = "DEVICE_INTENSITY_VALUE", length = 10)
	private int intensity;
	
	public DeviceStatus() {
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
	
	public int getID() {
		return id;
	}
	
	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}


}
