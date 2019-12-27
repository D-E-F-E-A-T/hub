package org.toy.serviceframework.util;

import org.toy.propertyframework.api.IPropertyDictionary;
import org.toy.serviceframework.api.IServiceContext;
import org.toy.serviceframework.api.IServiceReference;
import org.toy.serviceframework.api.IServiceRegistry;
import org.toy.serviceframework.impl.ServiceRegistryImpl;

public class ServiceHelper {
	
	private static IServiceRegistry GLOBAL_REGISTRY;
	private static IServiceContext GLOBAL_CONTEXT;
	
	public static IServiceRegistry getGlobalServiceRegistry() {
		return GLOBAL_REGISTRY;
	}
	
	public static IServiceContext getGlobalServiceContext() {
		return GLOBAL_CONTEXT;
	}
	
	private static void __bootstrap() {
		if(GLOBAL_REGISTRY != null || GLOBAL_CONTEXT != null) {
			throw new RuntimeException(new IllegalStateException());
		} else {
			GLOBAL_REGISTRY = new ServiceRegistryImpl();
			GLOBAL_CONTEXT = new GlobalServiceContext();
		}
	}
	
	static {
		__bootstrap();
	}
	
	public static <T> T attemptGet(IServiceRegistry registry, IServiceContext context, Class<T> serviceClazz) {
		return attemptGet(registry, context, serviceClazz, null);
	}
	
	public static <T> T attemptGet(IServiceRegistry registry, IServiceContext context, Class<T> serviceClazz, IPropertyDictionary dict) {
		IServiceReference<T> ref = registry.getServiceReference(context, serviceClazz);
		try {
			if(dict != null) {
				return registry.getService(ref, dict);
			} else {
				return registry.getService(ref);
			}
		} finally {
			if(ref != null) {
				registry.ungetService(ref);
			}
		}
	}
}