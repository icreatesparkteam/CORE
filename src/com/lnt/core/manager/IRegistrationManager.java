package com.lnt.core.manager;

import java.util.List;

import com.lnt.core.common.dto.ServiceProviderRegistrationDto;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.model.Permission;
import com.lnt.core.model.ServiceProvider;

public interface IRegistrationManager {

	public void createServiceProvider(ServiceProvider serviceProvider) throws ServiceApplicationException;

	public ServiceProvider getServiceProvider(String username) throws ServiceApplicationException;

	public ServiceProvider getServiceProviderById(int id);

	public void updateServiceProvider(ServiceProvider user) throws ServiceApplicationException;

	public List<ServiceProviderRegistrationDto> getServiceProviderlist(int id) throws ServiceApplicationException;
	
	public List<ServiceProviderRegistrationDto> getAllServiceProviderlist() throws ServiceApplicationException;
	
	public List<Permission> getUserPermissions(int id) throws ServiceApplicationException;

}
