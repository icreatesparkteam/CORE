package com.lnt.core.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lnt.core.common.dto.SessionInfo;
import com.lnt.core.common.exception.ServiceRuntimeException;

public class SessionCache {

	private static final Logger logger = LoggerFactory
			.getLogger(SessionCache.class);

	private static SessionCache soleInstance = null;

	private ICache<String, SessionInfo> cache;

	private SessionCache(int cacheType) {
		this.cache = SessionCacheFactory.getCache(cacheType);
	}

	public synchronized static SessionCache createInstance(int cacheType) {
		if (soleInstance == null) {
			logger.info("Initializing Cache of type : {}", cacheType);
			soleInstance = new SessionCache(cacheType);
		}
		return soleInstance;
	}

	public static SessionCache getInstance() {
		if (soleInstance == null) {
			throw new ServiceRuntimeException("Session cache uninitialized");
		}
		return soleInstance;
	}

	public SessionInfo get(String token) {
		return cache.get(token);

	}

	public void put(String token, SessionInfo session) {
		cache.put(token, session);
	}

}
