package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PermissionDto {

	private String name;
	private String des;
	
	public String getname() {
		return name;
	}

	public void setroleId(String name) {
		this.name = name;
	}
	
	public String getpermissionId() {
		return des;
	}

	public void setpermissionId(String des) {
		this.des = des;
	}
}
