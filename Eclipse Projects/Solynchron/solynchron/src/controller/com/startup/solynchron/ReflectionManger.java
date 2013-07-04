/**
 * 
 */
package com.startup.solynchron;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import com.startup.solynchron.obj.ModelObject;

/**
 * @author plamKaTa
 *
 */
public class ReflectionManger {

	public static Object convertFromString(EntityManager em, String propType, String propValue) {
		Object value = null;
		try {
			Class propClass = Class.forName(propType);
			if (String.class.isAssignableFrom(propClass)) {
				value = propValue;
			} else if (Number.class.isAssignableFrom(propClass)) {
				Constructor cstr = propClass.getConstructor(new Class[] {String.class});
				value = cstr.newInstance(propValue);
			} else if (Date.class.isAssignableFrom(propClass)) {
				DateFormat format = new SimpleDateFormat();
				value = format.parse(propValue);
			} else if (ModelObject.class.isAssignableFrom(propClass)) {
				Long id = new Long(propValue);
				value = em.find(propClass, id);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public static String convertToString(Object value) {
		String strValue = null;
		if (value instanceof String) {
			strValue = (String) value;
		} else if (value instanceof Number) {
			strValue = value.toString();
		} else if (value instanceof Date) {
			strValue = value.toString();
		} else if (value instanceof ModelObject) {
			ModelObject modelObject = (ModelObject) value;
			Long id = modelObject.getId();
			strValue = id == null ? null : id.toString();
		}
		return strValue;
	}

}
