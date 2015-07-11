package com.lnt.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device_info")
public class SmartDevice  extends AbstractTimeStampEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(name = "GATEWAY_ID", length = 100)
	private int gatewayID;

	@Column(name = "DEVICE_ID", length = 50)
	private String deviceID;
	
	@Column(name = "MANUFACTURER_ID", length = 50)
	private String manufacturerID;
	
	@Column(name = "DEVICE_STATUS", length = 10)
	private int deviceStatus;

	@Column(name = "ACTIVE", length = 10)
	private String active;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	public SmartDevice() {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
