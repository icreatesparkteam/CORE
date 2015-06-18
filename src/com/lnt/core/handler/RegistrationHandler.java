package com.lnt.core.handler;

import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lnt.core.annotations.WriteTransaction;
import com.lnt.core.common.dto.ServiceProviderRegistrationDto;

import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.exception.ValidationException;
import com.lnt.core.common.util.IConstants;
import com.lnt.core.common.util.ServiceProviderInRequest;
import com.lnt.core.common.util.Validator;

import com.lnt.core.manager.IPasswordManager;

import com.lnt.core.manager.IRegistrationManager;
import com.lnt.core.model.ServiceProvider;


@Component
public class RegistrationHandler implements IRegistrationHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(RegistrationHandler.class);

	@Autowired
	private IRegistrationManager regMgr;

	@Autowired
	private IPasswordManager passwordManager;

	@Autowired
	private IAuthenticationHandler authHandler;

	@Override
	@WriteTransaction
	public void createServiceProvider(ServiceProviderRegistrationDto register)
			throws ServiceApplicationException {
		logger.info("createServiceProvider :  register method ");
		if (register == null) {
			throw new ServiceApplicationException("Invalid service provider Details ");
		}

		if (!Validator.mandatory(register.getPassword()))
			throw new ValidationException("Password is mandatory");

		if (!Validator.mandatory(register.getUserName()))
			throw new ValidationException("username is mandatory");

		if (regMgr.getServiceProvider(register.getUserName()) != null) {
			throw new ValidationException(
					"Duplicate service provider - service provider already exists with User name: "
							+ register.getUserName());
		}
		ServiceProvider serviceProvider = new ServiceProvider();
		serviceProvider = register.toServiceProvider(serviceProvider);
		serviceProvider.setPassword(register.getPassword());
		serviceProvider.setActive("y");
		serviceProvider.setChangePassword("yes");
		serviceProvider.setAttemptsLeft(IConstants.LOGIN_ATTEMPTS);
		regMgr.createServiceProvider(serviceProvider);
	}

	@Override
	@Transactional
	public ServiceProviderRegistrationDto getServiceProvider(String userName)
			throws ServiceApplicationException {
		logger.info("ServiceProviderHandler :  getServiceProvider method ");
		ServiceProvider serviceProvider = regMgr.getServiceProvider(userName);
		if (serviceProvider == null) {
			logger.error("serviceProvider : {} not found", userName);
			throw new ServiceApplicationException("serviceProvider not found : "
					+ userName);
		}
		ServiceProviderRegistrationDto reg = new ServiceProviderRegistrationDto();
		reg.fromServiceProvider(serviceProvider);
		return reg;
	}

	@Override
	@WriteTransaction
	public void updateServiceProvider(ServiceProviderRegistrationDto register)
			throws ServiceApplicationException {
		logger.info("serviceProvider :  updateserviceProvider method ");
		if (register == null) {
			throw new ServiceApplicationException("Invalid serviceProvider :");
		}
		ServiceProvider serviceProvider = regMgr.getServiceProvider(register.getServiceProviderName());
		if (serviceProvider == null) {
			throw new ServiceApplicationException(
					"serviceProvider is not available with this username : "
							+ register.getUserName());
		}
		register.toServiceProvider(serviceProvider);
		regMgr.updateServiceProvider(serviceProvider);

	}

	@Override
	@WriteTransaction
	public void deleteServiceProvider(String userName) throws ServiceApplicationException {
		logger.info("UserHandler :  deleteduser method ");
		ServiceProvider serviceProvider = regMgr.getServiceProvider(userName);
		if (serviceProvider == null) {
			throw new ServiceApplicationException("Invalid serviceProvider :");
		}
		serviceProvider.setActive("n");
		regMgr.updateServiceProvider(serviceProvider);
	}

	@Override
	@WriteTransaction
	public String changePassword(String password, String newPassword,
			String confirmPassword) throws ServiceApplicationException {
		logger.info("serviceProviderHandler : change password ");

		if (password.equals(newPassword)) {
			throw new ValidationException(IConstants.PASSWORD_COMPARISION);
		}

		String resultMsg;
		ServiceProvider serviceProvider = ServiceProviderInRequest.getInstance().getServiceProviderContext()
				.getServiceProviderInfo();

		int userId = serviceProvider.getId();
		resultMsg = passwordManager.validatePassword(serviceProvider.getUserName(),
				userId, newPassword);
		if (!"success".equalsIgnoreCase(resultMsg)) {
			logger.debug(resultMsg);
			throw new ValidationException(resultMsg);
		}

		resultMsg = authHandler.validatePassword(password, serviceProvider);
		if (!"Success".equalsIgnoreCase(resultMsg)) {
			throw new ValidationException(resultMsg);
		} else {
			logger.debug("Authentication success. Setting new password for user"
					+ serviceProvider.getUserName());
			serviceProvider.setPassword(newPassword);
			if (serviceProvider.getChangePassword().equalsIgnoreCase("yes")) {
				serviceProvider.setChangePassword("no");
			}
			regMgr.updateServiceProvider(serviceProvider);
		}

		return resultMsg;
	}

	/**
	 * Get serviceProvider name and security answer and validates answer with the answer
	 * stored in database.
	 * 
	 * @param userName
	 * @param userAns
	 * @return String specifying whether user security answer is validated or
	 *         not
	 * @throws ServiceApplicationException
	 */
	@Override
	@WriteTransaction
	public String passwordRecovery(String userName, String userAns)
			throws ServiceApplicationException {
		ServiceProvider serviceProvider = regMgr.getServiceProvider(userName);
		if (serviceProvider == null) {
			logger.error("serviceProvider : {} not found", userName);
			throw new ServiceApplicationException("serviceProvider not found : "
					+ userName);
		}
		if (userAns.equals(serviceProvider.getsAnswer())) {
			char[] pswd = passwordManager.generateTmpPswd(IConstants.MINLEN,
					IConstants.MINLEN, IConstants.NOOFCAPSALPHA,
					IConstants.NOOfDIGITS, IConstants.NOOfSPLCHARS);
			String tempPassword = new String(pswd);
			serviceProvider.setPassword(tempPassword);
			serviceProvider.setChangePassword("yes");
			regMgr.updateServiceProvider(serviceProvider);
			return tempPassword;
		} else {
			throw new ServiceApplicationException(
					HttpStatus.EXPECTATION_FAILED.value(),
					IConstants.VALIDATE_ANSWER_FAILED);
		}

	}

	@Override
	@Transactional
	public String getSecurityQuestions(String userName)
			throws ServiceApplicationException {
		ServiceProvider serviceProvider = regMgr.getServiceProvider(userName);
		if (serviceProvider == null) {
			logger.error("serviceProvider : {} not found", userName);
			throw new ServiceApplicationException("User not found : "
					+ userName);
		}

		String question = serviceProvider.getsQuestion();
		JSONObject obj = new JSONObject();
		obj.put("question", question);
		// String jsonData={question:question, answer:answer};

		return obj.toJSONString();
	}

	@Override
	@WriteTransaction
	public void setSecurityQuestions(String question, String answer)
			throws ServiceApplicationException {
		ServiceProvider serviceProvider = ServiceProviderInRequest.getInstance().getServiceProviderContext()
				.getServiceProviderInfo();
		// UserInfo user = userMgr.getUser(userName);
		if (serviceProvider == null) {
			logger.error("serviceProvider : {} not found");
			throw new ServiceApplicationException("serviceProvider not found : ");
		}
		serviceProvider.setsQuestion(question);
		serviceProvider.setsAnswer(answer);
		regMgr.updateServiceProvider(serviceProvider);
	}


	@Override
	@Transactional
	public List<ServiceProviderRegistrationDto> getServiceProviderList() throws ServiceApplicationException {
		logger.info("UserHandler :  getUserList by role id method ");
		return regMgr.getAllServiceProviderlist();
	}

}
