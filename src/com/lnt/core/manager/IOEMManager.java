package com.lnt.core.manager;

import java.util.List;

import com.lnt.core.common.dto.ServiceProviderRegistrationDto;
import com.lnt.core.common.dto.ServiceProviderListDto;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.Permission;
import com.lnt.core.model.ServiceProvider;

public interface IOEMManager {

	public void createServiceProvider(ServiceProvider serviceProvider) throws ServiceApplicationException;

	public ServiceProvider getServiceProvider(String username) throws ServiceApplicationException;

	public ServiceProvider getServiceProviderById(int id);

	public void updateServiceProvider(ServiceProvider user) throws ServiceApplicationException;

	public List<ServiceProviderRegistrationDto> getServiceProviderlist(int id) throws ServiceApplicationException;
	
	public List<ServiceProviderListDto> getAllServiceProviderlist() throws ServiceApplicationException;
	
	public List<Permission> getUserPermissions(int id) throws ServiceApplicationException;

}
