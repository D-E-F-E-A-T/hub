package org.toy.serviceframework.api;

public interface IServiceReference<T> {

	Class<T> getServiceType();
	
	IServiceRegistry getServiceRegistry();
	
	IServiceContext getContext();
}