/**
 * 
 */
package com.startup.solynchron.obj.event;

import java.util.Collection;
import java.util.Date;

import org.hibernate.type.Type;

import com.startup.solynchron.obj.IModelObject;

/**
 * @author plamKaTa
 *
 */
public class CompareManager {

	private static final String[] SYS_PROPERTIES = new String[] {
	"createDate", "updateDate", "createdBy", "updatedBy"};

	public static boolean equals(IModelObject left, IModelObject right) {
		boolean equal = false;
		if (left == null && right == null) {
			equal = true;
		} else if (left == null || right == null) {
			equal = false;
		} else if (left.isNew() || right.isNew()) {
			equal = left.equals(right);
		} else {
			equal = CompareManager.equals(left.getId(), right.getId());
		}
		return equal;
	}

	public static boolean equals(Date left, Date right) {
		boolean equal = false;
		if (left == null && right == null) {
			equal = true;
		} else if (left == null || right == null) {
			equal = false;
		} else {
			equal = Math.abs(left.getTime() - right.getTime()) < 200;
		}
		return equal;
	}

	public static boolean equals(Object left, Object right) {
		boolean equal = false;
		if (left == null && right == null) {
			equal = true;
		} else if (left == null || right == null) {
			equal = false;
		} else {
			equal = left.equals(right);
		}
		return equal;
	}

	public static boolean isSystemProperty(String property) {
		boolean systemProp = false;
		for (int i = 0; i < SYS_PROPERTIES.length; i++) {
			if (property.equals(SYS_PROPERTIES[i])) {
				systemProp = true;
				break;
			}
		}
		return systemProp;
	}

	public static boolean isModified(String[] propNames, Type[] propTypes,
			Object[] oldValues, Object[] newValues) {
		boolean changed = false;
		for (int i = 0; i < propNames.length; i++) {
			if (Collection.class.isAssignableFrom(
					propTypes[i].getReturnedClass())) {
				continue;
			} else if (!equals(oldValues[i], newValues[i])) {
				if (!isSystemProperty(propNames[i])) {
					changed = true;
					break;
				}
			}
		}
		return changed;
	}

}