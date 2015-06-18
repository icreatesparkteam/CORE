package com.lnt.core.common.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleCache<T> implements ICache<String, T> {

	private static final Logger logger = LoggerFactory
			.getLogger(SimpleCache.class);

	private final Map<String, T> elements;

	private final Map<String, Long> expiryTable;

	private final static int defaultExpiryTime = 30 * 60 * 1000; // in millisecs

	private long lastCleanupTime;

	private int expireTime;

	SimpleCache(int expireTime) {
		this.expireTime = expireTime;
		this.elements = Collections.synchronizedMap(new HashMap<String, T>());
		this.expiryTable = Collections
				.synchronizedMap(new HashMap<String, Long>());
	}

	SimpleCache() {
		this(defaultExpiryTime);
	}

	public void put(final String name, final T obj) {
		elements.put(name, obj);
		expiryTable.put(name, System.currentTimeMillis() + expireTime);
	}

	public T get(final String name) {
		// first cleanup cache if we haven't done so in a while.
		// ideally, we should do this in a separate thread
		if (System.currentTimeMillis() > lastCleanupTime + expireTime) {
			logger.info("Initiating a cache clean-up");
			lastCleanupTime = System.currentTimeMillis();
			removeExpired();
		}

		if (elements.containsKey(name)) {
			logger.info("{} found in cache", name);
			return elements.get(name);
		}

		return null;
	}

	public void removeExpired() {
		Iterator<Entry<String, Long>> iter = expiryTable.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Long> entry = iter.next();
			if (System.currentTimeMillis() > entry.getValue()) {
				logger.info("Removing entry : {}", entry.getKey());
				iter.remove();
				elements.remove(entry.getKey());
			}
		}
	}

}
