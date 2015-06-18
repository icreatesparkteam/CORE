package com.lnt.core.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.lnt.core.common.auth.AuthUser;
import com.lnt.core.common.dto.SessionInfo;
import com.lnt.core.common.dto.ServiceProviderContextData;
import com.lnt.core.common.util.ServiceProviderInRequest;
import com.lnt.core.handler.ISessionHandler;
import com.lnt.core.model.Permission;
import com.lnt.core.model.ServiceProvider;

@Component
public class UserDetailsByTokenServiceImpl implements
		AuthenticationUserDetailsService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserDetailsByTokenServiceImpl.class);

	@Autowired
	ISessionHandler sessionHandler;

	public UserDetails loadUserDetails(Authentication authentication)
			throws UsernameNotFoundException {
		String token = authentication.getName();
		logger.info("UserDetailsByTokenServiceImpl loadUserDetails ...... {}",
				token);

		SessionInfo sessionInfo = sessionHandler.getSessionInfo(token);
		ServiceProvider serviceProvider = sessionInfo.getServiceProvider();
		AuthUser authUser = new AuthUser(serviceProvider);
		
		List<Permission> permissions = sessionInfo.getPermissions();
		logger.info(
				"UserDetailsByTokenServiceImpl list of permissions and permission size ...... {}",
				permissions, permissions.size());
		Set<String> permissionSet = new HashSet<String>(permissions.size());
		logger.info("UserDetailsByTokenServiceImpl for loop ");
		for (Permission permission : permissions) {
			logger.info("UserDetailsByTokenServiceImpl : adding Permission name "
					+ permission.getName().toUpperCase());
			permissionSet.add(permission.getName().toUpperCase());
		}
		authUser.setAuthorities(permissionSet);
		ServiceProviderContextData serviceProviderContext = new ServiceProviderContextData();
		serviceProviderContext.setServiceProviderInfo(serviceProvider);
		serviceProviderContext.setPermissions(permissionSet);
		ServiceProviderInRequest.getInstance().setServiceProviderContext(serviceProviderContext);
		
		return authUser;
	}

}
