package org.toy.serviceframework.api;

import org.toy.propertyframework.api.IPropertyDictionary;

public interface IServiceFactory<T> {

	T create(IPropertyDictionary dict);
}