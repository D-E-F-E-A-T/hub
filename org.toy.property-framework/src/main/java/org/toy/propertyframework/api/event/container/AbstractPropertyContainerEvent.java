package org.toy.propertyframework.api.event.container;

import org.toy.propertyframework.api.IProperty;
import org.toy.propertyframework.api.IPropertyDictionary;
import org.toy.propertyframework.api.event.AbstractPropertyEvent;

public abstract class AbstractPropertyContainerEvent extends AbstractPropertyEvent {

	private final IPropertyDictionary dictionary;
	private final String key;
	
	public AbstractPropertyContainerEvent(IProperty<?> prop, IPropertyDictionary dictionary, String key) {
		super(prop);
		this.dictionary = dictionary;
		this.key = key;
	}
	
	public IPropertyDictionary getDictionary() {
		return dictionary;
	}
	
	public String getKey() {
		return key;
	}
}