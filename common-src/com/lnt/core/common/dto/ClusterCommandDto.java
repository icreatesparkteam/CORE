package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.ClusterCommand;

@XmlRootElement
public class ClusterCommandDto {

	private int clusterID;
	
	private String commandName;
	
	private String commandValue;	
	

	public ClusterCommand toCluster(ClusterCommand clusterCommand) throws ServiceApplicationException {
		clusterCommand.setClusterID(this.clusterID);
		clusterCommand.setCommandName(this.commandName);
		clusterCommand.setCommandValue(commandValue);
		return clusterCommand;
	}

	public ClusterCommandDto formCluster(ClusterCommand clusterCommand) {
		this.clusterID = clusterCommand.getClusterID();
		this.commandName = clusterCommand.getCommandName();
		this.commandValue = clusterCommand.getCommandValue();
		return this;
	}

	public int getClusterID() {
		return clusterID;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}
	
	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getCommandValue() {
		return commandValue;
	}

	public void setCommandValue(String commandValue) {
		this.commandValue = commandValue;
	}

	
}
