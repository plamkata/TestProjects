/**
 * 
 */
package com.damnhandy.aspects.bean.impl;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;

/**
 * @author plamKaTa
 *
 */
public class NestedIndexedPropertyChangeEvent extends IndexedPropertyChangeEvent {
	
	private Object nested;

	/**
	 * @param source
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @param index
	 */
	public NestedIndexedPropertyChangeEvent(Object base, String baseProperty, 
			int index, PropertyChangeEvent event) {
		super(base, baseProperty + "." + event.getPropertyName(), 
				event.getOldValue(), event.getNewValue(), index);
		this.nested = event.getSource();
		setPropagationId(event.getPropagationId());
	}

	public Object getNested() {
		return nested;
	}

}
