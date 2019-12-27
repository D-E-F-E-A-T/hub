package org.toy.serviceframework.impl;

import org.toy.propertyframework.api.IPropertyDictionary;
import org.toy.serviceframework.api.IServiceReference;

public interface IInternalServiceReference<T> extends IServiceReference<T> {

	T get(IPropertyDictionary dict);
	
	void lock();
	
	void unlock();
	
	boolean locked();
}