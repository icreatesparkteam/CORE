package com.lnt.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device_command_queue")
public class DeviceCommandQueue  extends AbstractTimeStampEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(name = "DEVICE_ID", length = 100)
	private String deviceID;

	@Column(name = "GATEWAY_ID", length = 10)
	private int gatewayID;
	
	@Column(name = "ENDPOINT", length = 50)
	private String endPoint;
	
	@Column(name = "COMMAND", length = 100)
	private String command;
	
	@Column(name = "COMMAND_VAL", length = 100)
	private String commandVal;
	
	@Column(name = "SYNCED", length = 2)
	private int synced;
		
	public DeviceCommandQueue() {
	}
	
	public int getID() {
		return id;
	}
	
	public String getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	public int getGatewayID() {
		return gatewayID;
	}

	public void setGatewayID(int gatewayID) {
		this.gatewayID = gatewayID;
	}
	
	public String getEndpoint() {
		return endPoint;
	}

	public void setEndpoint(String endPoint) {
		this.endPoint = endPoint;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getCommandVal() {
		return commandVal;
	}

	public void setCommandVal(String commandVal) {
		this.commandVal = commandVal;
	}
	
	public int getSynced(){
		return synced;
	}
	
	public void setSynced(int synced){
		this.synced = synced;
	}


}
