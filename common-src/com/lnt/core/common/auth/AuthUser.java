package com.lnt.core.common.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lnt.core.model.ServiceProvider;

public class AuthUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(AuthUser.class);
	private ServiceProvider serviceProvider;

	List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();

	public AuthUser(ServiceProvider serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public void setAuthorities(Set<String> permissions) {
		for (String permission : permissions) {
			logger.info(".... Adding permission : {} .........", permission);
			authorities.add(new SimpleGrantedAuthority(permission));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return serviceProvider.getPassword();
	}

	@Override
	public String getUsername() {
		return serviceProvider.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
