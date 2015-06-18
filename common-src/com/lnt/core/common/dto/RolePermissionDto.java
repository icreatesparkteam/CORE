package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RolePermissionDto {

	private int roleId;
	private int permissionId;
	private String roleName;
	
	public int getroleId() {
		return roleId;
	}

	public void setroleId(int roleId) {
		this.roleId = roleId;
	}
	
	public int getpermissionId() {
		return permissionId;
	}

	public void setpermissionId(int permissionId) {
		this.permissionId = permissionId;
	}
	
	public String getroleName() {
		return roleName;
	}

	public void setroleName(String roleName) {
		this.roleName = roleName;
	}
}
