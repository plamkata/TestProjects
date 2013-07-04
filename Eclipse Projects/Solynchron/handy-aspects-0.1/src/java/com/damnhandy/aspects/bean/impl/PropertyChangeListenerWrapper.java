/**
 * 
 */
package com.damnhandy.aspects.bean.impl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This class provides a wrapper for any property change listener. The wrapper
 * isolates property change event propagation to only property changes of the 
 * specified (nested) property. This class wraps an original listener and propagates
 * changes only if the specified property has been changed. 
 * <p>
 * It is used when a specific property change listener needs to be registered for 
 * a nested property. Since a nested listener will propagate modification events for 
 * all of the nested properties it is important that specific listeners receive only 
 * those events for the specific properties (that the listeners are registered for). This 
 * is simply achieved by this wrapper when specific listeners for nested properties are
 * being registered.
 *  
 * @author plamKaTa
 * @since 1.1
 *
 */
class PropertyChangeListenerWrapper implements PropertyChangeListener {
	
	private String nestedPropertyName;
	
	private PropertyChangeListener specificListener;

	/**
	 * Creates a wrapper of the listener that will propagate only modification events 
	 * of the specific (nested) property.
	 * 
	 * @param nestedProperty - the specific (nested) property that changes will be filtered for
	 * @param listener - the listener to be wrapped
	 */
	PropertyChangeListenerWrapper(String nestedProperty, PropertyChangeListener listener) {
		if (nestedProperty == null) throw new IllegalArgumentException(
				"Specific proeprty change listeners should be applied to non-null nested proeprties.");
		this.nestedPropertyName = nestedProperty;
		this.specificListener = listener;
	}
	
	/**
	 * Get the specific (nested) property for which modification events should be filtered.
	 * 
	 * @return the specific (nested) property for filtering events
	 */
	public String getSpecificNestedProperty() {
		return nestedPropertyName;
	}
	
	/**
	 * Get the internal listener that is wrapped by this wrapper.
	 * 
	 * @return the internal wrapped listener
	 */
	public PropertyChangeListener getInternal() {
		return specificListener;
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (nestedPropertyName.equals(evt.getPropertyName())) {
			specificListener.propertyChange(evt);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PropertyChangeListenerWrapper) {
			PropertyChangeListenerWrapper other = (PropertyChangeListenerWrapper) obj;
			return getInternal().equals(other.getInternal());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return getInternal().hashCode();
	}

}
