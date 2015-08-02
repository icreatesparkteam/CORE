package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DeviceCommandDto {

	private String gatewayID;

	private String deviceID;
	
	private String command;
	
	private String endpointID;


	/**
	 * @return the deviceID
	 */
	public String getDeviceID() {
		return deviceID;
	}

	/**
	 * @param deviceID the deviceID to set
	 */
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}

	/**
	 * @return the gatewayID
	 */
	public String getGatewayID() {
		return gatewayID;
	}

	/**
	 * @param gatewayID the gatewayID to set
	 */
	public void setGatewayID(String gatewayID) {
		this.gatewayID = gatewayID;
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the endpointID
	 */
	public String getEndpointID() {
		return endpointID;
	}

	/**
	 * @param endpointID the endpointID to set
	 */
	public void setEndpointID(String endpointID) {
		this.endpointID = endpointID;
	}
	

	

}
