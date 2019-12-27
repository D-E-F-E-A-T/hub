package org.toy.propertyframework.api.event.container;

import org.toy.propertyframework.api.IProperty;
import org.toy.propertyframework.api.IPropertyDictionary;

public class PropertyAddedEvent extends AbstractPropertyContainerEvent {

	public PropertyAddedEvent(IProperty<?> prop, IPropertyDictionary dictionary, String key) {
		super(prop, dictionary, key);
	}
}