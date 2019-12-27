package org.toy.propertyframework.api.event.container;

import org.toy.propertyframework.api.IProperty;
import org.toy.propertyframework.api.IPropertyDictionary;

public class PropertyRemovedEvent extends AbstractPropertyContainerEvent {

	public PropertyRemovedEvent(IProperty<?> prop, IPropertyDictionary dictionary, String key) {
		super(prop, dictionary, key);
	}
}