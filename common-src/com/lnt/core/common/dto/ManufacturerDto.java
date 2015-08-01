package com.lnt.core.common.dto;

import javax.xml.bind.annotation.XmlRootElement;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.Manufacturer;

@XmlRootElement
public class ManufacturerDto {


	private String manufacturerName;
	
	public Manufacturer toManufacturer(Manufacturer manufacturer) throws ServiceApplicationException {
		manufacturer.setManufacturerName(this.manufacturerName);
		return manufacturer;
	}

	public ManufacturerDto formManufacturer(Manufacturer manufacturer) {
		this.manufacturerName = manufacturer.getManufacturerName();
		return this;
	}

	public String getmanufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	
}
