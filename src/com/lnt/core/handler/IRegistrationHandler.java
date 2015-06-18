package com.lnt.core.handler;

import java.util.List;

import com.lnt.core.common.dto.ServiceProviderRegistrationDto;
import com.lnt.core.common.exception.ServiceApplicationException;

public interface IRegistrationHandler {

	public void createServiceProvider(ServiceProviderRegistrationDto register)
			throws ServiceApplicationException;

	public ServiceProviderRegistrationDto getServiceProvider(String userName)
			throws ServiceApplicationException;

	public void updateServiceProvider(ServiceProviderRegistrationDto register)
			throws ServiceApplicationException;

	public void deleteServiceProvider(String userName) throws ServiceApplicationException;


	public String changePassword(String password, String newPassword,
			String confirmPassword) throws ServiceApplicationException;

	public String passwordRecovery(String userName, String questionAns)
			throws ServiceApplicationException;

	public String getSecurityQuestions(String userName)
			throws ServiceApplicationException;

	public void setSecurityQuestions(String question, String answer)
			throws ServiceApplicationException;

	List<ServiceProviderRegistrationDto> getServiceProviderList()
			throws ServiceApplicationException;

	

}
