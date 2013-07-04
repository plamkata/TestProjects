package com.startup.solynchron.obj.event;

import java.util.Collection;
import java.util.Date;

import org.hibernate.event.EventSource;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.hibernate.type.Type;

import com.startup.solynchron.Activator;
import com.startup.solynchron.obj.IModelObject;
import com.startup.solynchron.obj.LocalUser;
import com.startup.solynchron.obj.MyUser;

public class AuditEventListener implements PreInsertEventListener, PostInsertEventListener, 
	PreUpdateEventListener, PostUpdateEventListener, PreDeleteEventListener, PostDeleteEventListener {

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		if (event.getEntity() instanceof IModelObject && 
				!(event.getEntity() instanceof DBLog) && 
				!(event.getEntity() instanceof DBChange)) {
			EventSource session = event.getSession();
			
			IModelObject modelObj = (IModelObject) event.getEntity();
			Date now = new Date();
			modelObj.setCreateDate(now);
			modelObj.setUpdateDate(now);
			
			MyUser user = (MyUser) session.load(LocalUser.class, new Long(1));
			modelObj.setCreatedBy(user);
			modelObj.setUpdatedBy(user);
			
			session.saveOrUpdate(modelObj);
		}
		return false;
	}
	
	@Override
	public void onPostInsert(PostInsertEvent event) {
		if (event.getEntity() instanceof IModelObject && 
				!(event.getEntity() instanceof DBLog) && 
				!(event.getEntity() instanceof DBChange)) {
			DBLog log = createLog(event);
			
			if (log != null && Activator.getDefault().isInitialized()) {
				event.getSession().save(log);
			}
		}
	}

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		if (event.getEntity() instanceof IModelObject && 
				!(event.getEntity() instanceof DBLog) && 
				!(event.getEntity() instanceof DBChange)) {
			if (isModified(event)) {
				EventSource session = event.getSession();
				IModelObject modelObj = (IModelObject) event.getEntity();
				
				Date now = new Date();
				modelObj.setUpdateDate(now);
				
				MyUser user = (MyUser) session.load(LocalUser.class, new Long(1));
				modelObj.setUpdatedBy(user);
				
				session.saveOrUpdate(modelObj);
			}
		}
		return false;
	}
	
	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		if (event.getEntity() instanceof IModelObject && 
				!(event.getEntity() instanceof DBLog) && 
				!(event.getEntity() instanceof DBChange)) {
			DBLog log = createLog(event);
			
			if (log != null) {
				if (Activator.getDefault().isInitialized()) {
					event.getSession().save(log);
				}
			}
		}
	}
	
	@Override
	public boolean onPreDelete(PreDeleteEvent event) {
		if (event.getEntity() instanceof IModelObject && 
				!(event.getEntity() instanceof DBLog) && 
				!(event.getEntity() instanceof DBChange)) {
			DBLog log = createLog(event);
			
			if (log != null) {
				if (Activator.getDefault().isInitialized()) {
					event.getSession().save(log);
				}
			}
		}
		return false;
	}
	
	@Override
	public void onPostDelete(PostDeleteEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private DBLog createLog(PostInsertEvent event) {
		IModelObject modelObj = (IModelObject) event.getEntity();
		
		String[] propNames = event.getPersister().getEntityMetamodel().getPropertyNames();
		Type[] propTypes = event.getPersister().getEntityMetamodel().getPropertyTypes();
		Object[] newValues = event.getState();
		Object[] oldValues = new Object[newValues.length];
		
		DBLog log = createLog(propNames, propTypes, oldValues, newValues);
		if (log != null) {
			log.setOperation("insert");
			log.setEntry(modelObj);
			if (modelObj.getUpdateDate() != null) {
				log.setEntryDate(modelObj.getUpdateDate());
			} else {
				log.setEntryDate(new Date());
			}
		}
		return log;
	}

	private static DBLog createLog(PostUpdateEvent event) {
		IModelObject modelObj = (IModelObject) event.getEntity();
		
		String[] propNames = event.getPersister().getEntityMetamodel().getPropertyNames();
		Type[] propTypes = event.getPersister().getEntityMetamodel().getPropertyTypes();
		Object[] oldValues = event.getOldState();
		Object[] newValues = event.getState();
		
		DBLog log = createLog(propNames, propTypes, oldValues, newValues);
		if (log != null) {
			log.setOperation("update");
			log.setEntry(modelObj);
			if (modelObj.getUpdateDate() != null) {
				log.setEntryDate(modelObj.getUpdateDate());
			} else {
				log.setEntryDate(new Date());
			}
		}
		return log;
	}
	
	private static DBLog createLog(PreDeleteEvent event) {
		IModelObject modelObj = (IModelObject) event.getEntity();
		
		DBLog log = new DBLog();
		log.setOperation("delete");
		log.setEntry(modelObj);
		if (modelObj.getUpdateDate() != null) {
			log.setEntryDate(modelObj.getUpdateDate());
		} else {
			log.setEntryDate(new Date());
		}
		return log;
	}

	private static DBLog createLog(String[] propNames, Type[] propTypes,
			Object[] oldValues, Object[] newValues) {
		DBLog log = new DBLog();
		boolean changed = false;
		for (int i = 0; i < propNames.length; i++) {
			if (Collection.class.isAssignableFrom(
					propTypes[i].getReturnedClass())) {
				continue;
			} else if (!CompareManager.equals(oldValues[i], newValues[i])) {
				if (!CompareManager.isSystemProperty(propNames[i])) {
					changed = true;
					break;
				}
				/*
				DBChange change = new DBChange();
				change.setProperty(propNames[i]);
				change.setPropertyType(propTypes[i].getReturnedClass().getName());
				change.setRealValue(newValues[i]);
				change.setLog(log);
				log.getChanges().add(change);
				*/
			}
		}
		if (!changed) log = null;
		return log;
	}
	
	private static boolean isModified(PreUpdateEvent event) {
		String[] propNames = event.getPersister().getEntityMetamodel().getPropertyNames();
		Type[] propTypes = event.getPersister().getEntityMetamodel().getPropertyTypes();
		Object[] oldValues = event.getOldState();
		Object[] newValues = event.getState();
		
		return CompareManager.isModified(
				propNames, propTypes, oldValues, newValues);
	}

}
