/**
 * 
 */
package com.damnhandy.aspects.bean.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author ryan
 *
 */
public class ValueObjectListener implements PropertyChangeListener {

	private int changeCount = 0;
	
	private String propertyName;
	
	private Object newValue;
	
	private Object oldValue;
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		propertyName = evt.getPropertyName();
		newValue = evt.getNewValue();
		oldValue = evt.getOldValue();
		changeCount++;
		print(evt);
	}

	/**
	 * @param evt
	 */
	protected void print(PropertyChangeEvent evt) {
		System.out.println("Property: " + propertyName + 
				" Old: " + evt.getOldValue() + " New: " + evt.getNewValue());
	}
	
	public int getChangeCount() {
		return changeCount;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Object getNewValue() {
		return newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	public boolean hasChanged() {
		return changeCount > 0;
	}
	
	public boolean hasChangedOnce() {
		return changeCount == 1;
	}
	
	public void reset() {
		changeCount = 0;
		newValue = null;
		oldValue = null;
	}

}
