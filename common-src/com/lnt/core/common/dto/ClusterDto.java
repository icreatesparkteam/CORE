package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.Cluster;

@XmlRootElement
public class ClusterDto {

	private int id;

	private String clusterID;
	
	private String clusterName;
	
	

	public Cluster toCluster(Cluster cluster) throws ServiceApplicationException {
		cluster.setClusterID(this.clusterID);
		cluster.setClusterName(this.clusterName);
		return cluster;
	}

	public ClusterDto formCluster(Cluster cluster) {
		this.setId(cluster.getID());
		this.clusterID = cluster.getClusterID();
		this.clusterName = cluster.getClusterName();
		return this;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterID() {
		return clusterID;
	}

	public void setClusterID(String clusterID) {
		this.clusterID = clusterID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
