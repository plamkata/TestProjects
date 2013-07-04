package com.damnhandy.aspects.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The interface that will be introduced to the advised class so that it 
 * can support bound properties. 
 * 
 * @author Ryan J. McDonough
 * @since 1.0
 */
public interface JavaBean {

	/**
	 * Adds a new PropertyChangeListener to the target
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public abstract void addPropertyChangeListener(
			PropertyChangeListener listener);

	/**
	 * Adds a new PropertyChangeListener to the target for the given property name
	 * @param propertyName
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public abstract void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener);
	


	/**
	 * @param propertyName
	 * @param index
	 * @param oldValue
	 * @param newValue
	 * @see java.beans.PropertyChangeSupport#fireIndexedPropertyChange(java.lang.String, int, java.lang.Object, java.lang.Object)
	 */
	public abstract void fireIndexedPropertyChange(String propertyName,
			int index, Object oldValue, Object newValue);

	/**
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public abstract void firePropertyChange(String propertyName,
			Object oldValue, Object newValue);
	
	/**
	 * 
	 * @param event
	 */
	public abstract void firePropertyChange(PropertyChangeEvent event);

	/**
	 * @return
	 * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners()
	 * @see java.beans.PropertyChangeSupport#firePropertyChange(PropertyChangeEvent)
	 */
	public abstract PropertyChangeListener[] getPropertyChangeListeners();

	/**
	 * @param propertyName
	 * @return
	 * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners(java.lang.String)
	 */
	public abstract PropertyChangeListener[] getPropertyChangeListeners(
			String propertyName);

	/**
	 * @param propertyName
	 * @return
	 * @see java.beans.PropertyChangeSupport#hasListeners(java.lang.String)
	 */
	public abstract boolean hasListeners(String propertyName);

	/**
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public abstract void removePropertyChangeListener(
			PropertyChangeListener listener);

	/**
	 * @param propertyName
	 * @param listener
	 * @see java.beans.PropertyChangeSupport#removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
	 */
	public abstract void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener);
	
	/**
	 * Checks if the java bean already contains the specified listener.
	 * 
	 * @param listener the listener to be checked for containment 
	 * @return true, if the JavaBean already contains the specified listener
	 */
	public abstract boolean contains(PropertyChangeListener listener);
	
	/**
	 * Filter any wrapped listeners by replacing them with their internal listeners.
	 * 
	 * @param listeners the array of listeners to be filtered
	 */
	public abstract void filterListeners(PropertyChangeListener[] listeners);
}