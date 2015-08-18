package com.lnt.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cluster_command")
public class ClusterCommand  extends AbstractTimeStampEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private int id;

	@Column(name = "CLUSTER_ID", length = 10)
	private int clusterID;

	@Column(name = "COMMAND_NAME", length = 50)
	private String commandName;
	
	@Column(name = "COMMAND_VALUE", length = 50)
	private String commandValue;
	
	@Column(name = "COMMAND_UPPER_RANGE", length = 50)
	private int commandUpRange;
	
	@Column(name = "COMMAND_LOWER_RANGE", length = 50)
	private int commandLowRange;
	
	public ClusterCommand() {
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
	
	public int getID() {
		return id;
	}
	
	public int getCommandUpRange() {
		return commandUpRange;
	}

	public void setCommandUpRange(int commandUpRange) {
		this.commandUpRange = commandUpRange;
	}
	
	public int getCommandLowRange() {
		return commandLowRange;
	}

	public void setCommandLowRange(int commandLowRange) {
		this.commandLowRange = commandLowRange;
	}

}
