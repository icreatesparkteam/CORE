package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.DeviceCommandQueue;

@XmlRootElement
public class DeviceCommandQueueDto {

	private int id;

	private String deviceID;

	private int gatewayID;
	
	private String endPoint;
	
	private String command;
	
	private int synced;
	
	private String commandVal;
	
	public DeviceCommandQueue toDeviceCommandQueue(DeviceCommandQueue queue) throws ServiceApplicationException {
		queue.setDeviceID(this.deviceID);
		queue.setGatewayID(this.gatewayID);
		queue.setEndpoint(this.endPoint);
		queue.setCommand(this.command);
		queue.setSynced(synced);
		queue.setCommandVal(commandVal);
		return queue;
	}

	public DeviceCommandQueueDto formDeviceCommandQueue(DeviceCommandQueue device) {
		this.id = device.getID();
		this.deviceID = device.getDeviceID();
		this.gatewayID = device.getGatewayID();
		this.endPoint = device.getEndpoint();
		this.command = device.getCommand();
		this.synced = device.getSynced();
		this.commandVal = device.getCommandVal();
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

	public int getGatewayID() {
		return gatewayID;
	}

	public void setGatewayID(int gatewayID) {
		this.gatewayID = gatewayID;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getSynced(){
		return synced;
	}
	
	public void setSynced(int synced){
		this.synced = synced;
	}

	public String getCommandVal() {
		return commandVal;
	}

	public void setCommandVal(String commandVal) {
		this.commandVal = commandVal;
	}
}
