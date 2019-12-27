package org.toy.propertyframework.api.event;

import org.toy.propertyframework.api.IProperty;

public interface IPropertyEvent {

	IProperty<?> getProperty();
}