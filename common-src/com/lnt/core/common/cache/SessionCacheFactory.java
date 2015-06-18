package com.lnt.core.common.cache;

import com.lnt.core.common.dto.SessionInfo;
import com.lnt.core.common.exception.ServiceApplicationException;
import com.lnt.core.common.exception.ServiceRuntimeException;
import com.lnt.core.common.util.IConstants;

/**
 * 
 * Session Cache Mechanism Factory
 * 
 */
public class SessionCacheFactory {

	/**
	 * Returns instance of a session cache mehanism.
	 * 
	 * @param sessionCacheType
	 * @return
	 * @throws ServiceApplicationException
	 */
	public static ICache<String, SessionInfo> getCache(int cacheType) {
		switch (cacheType) {
		case IConstants.CACHE_TYPE_SIMPLE:
			return (ICache<String, SessionInfo>) new SimpleCache<SessionInfo>();
		default: // as of now, we do not have any other option
			throw new ServiceRuntimeException("Unknow Cache type configuration");
		}
	}

}
