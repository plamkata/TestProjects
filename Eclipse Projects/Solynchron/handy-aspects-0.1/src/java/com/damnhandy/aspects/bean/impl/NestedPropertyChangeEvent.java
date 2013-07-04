/**
 * 
 */
package com.damnhandy.aspects.bean.impl;

import java.beans.PropertyChangeEvent;

/**
 * @author plamKaTa
 *
 */
public class NestedPropertyChangeEvent extends PropertyChangeEvent {
	
	private Object nested;
	
	public NestedPropertyChangeEvent(Object base, String baseProperty, PropertyChangeEvent event) {
		super(base, baseProperty + "." + event.getPropertyName(), 
				event.getOldValue(), event.getNewValue());
		this.nested = event.getSource();
		this.setPropagationId(event.getPropagationId());
	}

	public Object getNested() {
		return nested;
	}

}