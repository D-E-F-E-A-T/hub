package org.toy.serviceframework.api;

public interface IServiceQuery<T> {

	boolean accept(IServiceReference<T> ref);
}