package org.toy.serviceframework.api;

import org.toy.propertyframework.api.IPropertyDictionary;

public interface IServiceReferenceHandler {

	<T> T loadService(IServiceReference<T> ref, IPropertyDictionary dict);

	void unloadService(IServiceReference<?> ref);
}