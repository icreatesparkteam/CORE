package com.lnt.core.common.cache;

public interface ICache<K, E> {

	public void put(K key, E entity);

	public E get(K key);

}
