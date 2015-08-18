package com.lnt.core.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lnt.core.common.dto.ServiceProviderListDto;
import com.lnt.core.common.dto.ServiceProviderRegistrationDto;
import com.lnt.core.common.exception.ServiceApplicationException;

import com.lnt.core.dao.impl.PermissionDao;
import com.lnt.core.dao.impl.RolePermissionDao;
import com.lnt.core.dao.impl.OEMDao;
import com.lnt.core.model.Permission;
import com.lnt.core.model.Role;
import com.lnt.core.model.RolePermission;
import com.lnt.core.model.ServiceProvider;

@Component
public class OEMManager implements IOEMManager {

	private static final Logger logger = LoggerFactory
			.getLogger(OEMManager.class);
	@Autowired
	private OEMDao serviceProviderDao;
	
	@Autowired
	private RolePermissionDao rolePermDao;

	@Autowired
	private PermissionDao permissionDao;
	
	@Autowired
	private RoleManager roleMgr;

	@Override
	public void createServiceProvider(ServiceProvider serviceProvider) throws ServiceApplicationException {
		logger.info("UserManager : serviceProvider");
		serviceProviderDao.create(serviceProvider);
	}

	@Override
	public ServiceProvider getServiceProvider(String username) {
		logger.info("UserManager Retrieving user information with userName {}",
				username);
		ServiceProvider serviceProvider = serviceProviderDao.findByUserName(username);
		return serviceProvider;
	}

	@Override
	public ServiceProvider getServiceProviderById(int id) {
		logger.info("UserManager Retrieving user information with id {}", id);
		return serviceProviderDao.findById(id);
	}

	@Override
	public void updateServiceProvider(ServiceProvider serviceProvider) throws ServiceApplicationException {
		logger.info("UserManager : updateUser ");
		serviceProviderDao.update(serviceProvider);
	}

	
	@Override
	public List<ServiceProviderRegistrationDto> getServiceProviderlist(int id) throws ServiceApplicationException {
		logger.info("UserManager  getUserlist for role - id {}", id);
		List<ServiceProviderRegistrationDto> serviceProviderDtoList = new ArrayList<>();
		List<ServiceProvider> serviceProviderListInfoList = serviceProviderDao.getServiceProviderList();
		if (serviceProviderListInfoList == null) {
			throw new ServiceApplicationException(
					"Bad request.No serviceProvider available with the given role id "
							+ id);
		}
		for (ServiceProvider serviceProvider : serviceProviderListInfoList) {
			ServiceProviderRegistrationDto dto = new ServiceProviderRegistrationDto();
			dto.setServiceProviderName(serviceProvider.getServiceProviderName());
			dto.setUserName(serviceProvider.getUserName());
			
			serviceProviderDtoList.add(dto);

		}
		return serviceProviderDtoList;
	}

	@Override
//	@Transactional
	public List<ServiceProviderListDto> getAllServiceProviderlist() throws ServiceApplicationException {
		logger.info("UserManager  getAllServiceProviderlist!!!!!!!!!!!!!!");
		List<ServiceProviderListDto> serviceProviderDtoList = new ArrayList<>();
		List<ServiceProvider> serviceProviderRegistrationDtoList = serviceProviderDao.getServiceProviderList();
		if (serviceProviderRegistrationDtoList == null) {
			throw new ServiceApplicationException(
					"Bad request.No ServiceProvider available "
							);
		}
		for (ServiceProvider serviceProviderRegistrationDto : serviceProviderRegistrationDtoList) {
			ServiceProviderListDto dto = new ServiceProviderListDto();
			dto.setServiceProviderName(serviceProviderRegistrationDto.getServiceProviderName());
			dto.setUserName(serviceProviderRegistrationDto.getUserName());
//			Role role = roleMgr.getRole(serviceProviderRegistrationDto.getRole());
//			dto.setRole(role.getId());
			
			serviceProviderDtoList.add(dto);

		}
		return serviceProviderDtoList;
	}
	
	@Override
	public List<Permission> getUserPermissions(int id)
			throws ServiceApplicationException {
		logger.info("UserManager  getUserPermissions for user - id {}", id);
		List<Permission> permissions = new ArrayList<Permission>();
		ServiceProvider user = serviceProviderDao.findById(id);
		List<RolePermission> rolePerms = rolePermDao.findByRoleId(user
				.getRole());
		if (rolePerms == null) {
			throw new ServiceApplicationException(
					"Bad request.No Permission allotted to this role " + id);
		}
		for (RolePermission rolePerm : rolePerms) {
			int permissionId = rolePerm.getPermissionId();
			System.out.println("Permissions " + permissionId);
			permissions.add(permissionDao.findById(permissionId));
		}
		return permissions;

	}

}
