/**
 * 
 */
package com.damnhandy.aspects.bean.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.damnhandy.aspects.bean.JavaBean;

/**
 * A special property change listener that will propagate changes of 
 * nested properties to the parent JavaBean's property change support. 
 * 
 * @author P. Alexandrov
 * @since 1.1
 *
 */
public class NestedPropertyChangeListener implements PropertyChangeListener {
	
	private JavaBean parent;
	
	private String basePropertyName;
	
	/**
	 * Constructs a nested property change listener object.
	 * Only non-null parent beans and valid property names are accepted.
	 * 
	 * @throws IllegalArgumentException if an argument is null
	 */
	NestedPropertyChangeListener(JavaBean parent, String basePropertyName) {
		if (parent == null)	throw new IllegalArgumentException(
				"Cannot add nested property support for null parent.");
		if (basePropertyName == null) throw new IllegalArgumentException(
				"Cannot add nested property support for null property.");
		this.parent = parent;
		this.basePropertyName = basePropertyName;
	}

	/**
	 * Get the parent bean to whom property change events are fired.
	 * 
	 * @return the parent bean
	 */
	public JavaBean getParent() {
		return parent;
	}

	/**
	 * Get the base property name of the parent bean class. This is the 
	 * first component of the nested property name.
	 * 
	 * @return the base property name of the nested property
	 */
	public String getBasePropertyName() {
		return basePropertyName;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent event) {
		parent.firePropertyChange(new NestedPropertyChangeEvent(
				parent, basePropertyName, event));
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NestedPropertyChangeListener) {
			NestedPropertyChangeListener other = (NestedPropertyChangeListener) obj;
			return getParent() == other.getParent() && 
				getBasePropertyName().equals(other.getBasePropertyName());
		} else {
			return false;
		}
	}

}
