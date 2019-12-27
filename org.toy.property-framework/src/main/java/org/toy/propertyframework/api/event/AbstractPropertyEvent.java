package org.toy.propertyframework.api.event;

import org.toy.propertyframework.api.IProperty;

public abstract class AbstractPropertyEvent implements IPropertyEvent {

	private final IProperty<?> prop;

	public AbstractPropertyEvent(IProperty<?> prop) {
		this.prop = prop;
	}

	@Override
	public IProperty<?> getProperty() {
		return prop;
	}
}