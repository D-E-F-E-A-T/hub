package org.toy.serviceframework.impl;

import org.toy.propertyframework.api.IPropertyDictionary;
import org.toy.serviceframework.api.IServiceContext;
import org.toy.serviceframework.api.IServiceFactory;
import org.toy.serviceframework.api.IServiceRegistry;

public class InternalFactoryServiceReferenceImpl<T> extends AbstractInternalServiceReference<T> {

	private final IServiceFactory<T> factory;

	public InternalFactoryServiceReferenceImpl(IServiceRegistry serviceRegistry, IServiceContext serviceContext,
			Class<T> serviceType, IServiceFactory<T> factory) {
		super(serviceRegistry, serviceContext, serviceType);
		this.factory = factory;
	}

	@Override
	public T get(IPropertyDictionary dict) {
		return factory.create(dict);
	}
}