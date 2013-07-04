/**
 * 
 */
package com.damnhandy.aspects.bean.impl;

import java.beans.PropertyChangeEvent;

import com.damnhandy.aspects.bean.JavaBean;

/**
 * Nested property change listener with index support.
 * Indexed nested listeners are used in order to add nested
 * property change support for collection properties containing 
 * @Observables.
 * 
 * @author plamKaTa
 *
 */
public class NestedIndexedPropertyChangeListener extends
		NestedPropertyChangeListener {
	
	private int index;

	/**
	 * Construct a nested indexed property change listener.
	 * 
	 * @param parent the parent JavaBean
	 * @param baseProperty a collection property name that contains @Observables
	 */
	NestedIndexedPropertyChangeListener(JavaBean parent,
			String baseProperty, int baseIndex) {
		super(parent, baseProperty);
		this.index = baseIndex;
	}
	
	/**
	 * Get the base index for the base object in the list property.
	 * An index is available
	 * 
	 * @return
	 */
	public int getBaseIndex() {
		return index;
	}
	
	public void setBaseIndex(int baseIndex) {
		this.index = baseIndex;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		getParent().firePropertyChange(new NestedIndexedPropertyChangeEvent(
				getParent(), getBasePropertyName(), getBaseIndex(), event));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NestedIndexedPropertyChangeListener) {
			// ignore indices since sets add/remove method indices do not correspond
			return super.equals(obj);
		} else {
			return false;
		}
	}

}
