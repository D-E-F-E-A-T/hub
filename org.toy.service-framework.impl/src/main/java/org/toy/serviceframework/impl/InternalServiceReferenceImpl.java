package org.toy.serviceframework.impl;

import org.toy.propertyframework.api.IPropertyDictionary;
import org.toy.serviceframework.api.IServiceContext;
import org.toy.serviceframework.api.IServiceRegistry;

public class InternalServiceReferenceImpl<T> extends AbstractInternalServiceReference<T> {

	private final T obj;

	public InternalServiceReferenceImpl(IServiceRegistry serviceRegistry, IServiceContext serviceContext,
			Class<T> serviceType, T obj) {
		super(serviceRegistry, serviceContext, serviceType);
		this.obj = obj;
	}

	@Override
	public T get(IPropertyDictionary dict) {
		return obj;
	}
}