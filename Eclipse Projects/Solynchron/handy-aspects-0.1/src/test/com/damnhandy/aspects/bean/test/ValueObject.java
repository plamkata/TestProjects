/**
 * 
 */
package com.damnhandy.aspects.bean.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.damnhandy.aspects.bean.JavaBean;
import com.damnhandy.aspects.bean.Nested;
import com.damnhandy.aspects.bean.Observable;
import com.damnhandy.aspects.bean.Silent;

/**
 * @author ryan
 *
 */
@Observable
public class ValueObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1067071642544894858L;
	
	private String name;
	
	private Integer value;
	
	private String type;
	
	@Silent
	private String type1;
	
	private NestedObject nested;
	
	@Nested
	private NestedObject nested1;

	@Nested
	private NestedObject nested2;
	
	private NestedObject nonNested;
	
	private List<String> values = new ArrayList<String>();
	
	private Set<String> values1 = new HashSet<String>();
	
	@Nested
	private List<NestedObject> nestedList = new ArrayList<NestedObject>();
	
	private List<NestedObject> nestedList1 = new ArrayList<NestedObject>();
	
	@Nested
	private Set<NestedObject> nestedSet = new HashSet<NestedObject>();
	
	public ValueObject() {
		
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public ValueObject(String name, Integer value) {
		this.name = name;
		this.value = value;
	}
	
	/**
	 * First adds the listener to the current object and then sets 
	 * the value for name. No property changes should be fired in 
	 * a constructor.
	 * 
	 * @param name the name of the value object
	 * @param listener the value object listener to be added
	 */
	public ValueObject(String name, ValueObjectListener listener) {
		((JavaBean) this).addPropertyChangeListener(listener);
		setName(name);
		setType(name);
		setNestedList(new ArrayList<NestedObject>());
		setNestedList1(new ArrayList<NestedObject>());
		setNestedSet(new HashSet<NestedObject>());
	}
	
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the value.
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * @param value The value to set.
	 */
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	@Silent
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType1() {
		return type1;
	}
	
	public void setType1(String type1) {
		this.type1 = type1;
	}
	
	public NestedObject getNested() {
		return nested;
	}
	
	@Nested
	public void setNested(NestedObject nested) {
		this.nested = nested;
	}
	
	public NestedObject getNested1() {
		return nested1;
	}
	
	public void setNested1(NestedObject nested1) {
		this.nested1 = nested1;
	}
	
	public NestedObject getNested2() {
		return nested2;
	}
	
	@Silent
	public void setNested2(NestedObject nested2) {
		this.nested2 = nested2;
	}
	
	public NestedObject getNonNested() {
		return nonNested;
	}
	
	public void setNonNested(NestedObject nonNested) {
		this.nonNested = nonNested;
	}
	
	/**
	 * @return Returns the values.
	 */
	public List<String> getValues() {
		return values;
	}
	
	/**
	 * @param values The values to set.
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	public void addValue(String value) {
		int index = values.size();
		if (index != -1) {
			values.add(index, value);
		}
	}
	
	@Silent
	public void addNewValue(String value) {
		int index = values.size();
		if (index != -1) {
			values.add(index, value);
		}
	}
	
	public void removeValue(String value) {
		int index = values.indexOf(value);
		if (index != -1) {
			values.remove(index);
		}
	}
	
	public void addRemoveValue(String value) {
		int index = values.size();
		values.add(index, value);
		values.remove(index);
	}

	public Set<String> getValues1() {
		return values1;
	}

	public void setValues1(Set<String> values1) {
		this.values1 = values1;
	}
	
	public void addValue1(String value1) {
		values1.add(value1);
	}
	
	@Silent
	public void addNewValue1(String value1) {
		values1.add(value1);
	}
	
	public void removeValue1(String value1) {
		values1.remove(value1);
	}
	
	public void addRemoveValue1(String value1) {
		values1.add(value1);
		values1.remove(value1);
	}

	public List<NestedObject> getNestedList() {
		return nestedList;
	}

	public void setNestedList(List<NestedObject> nestedList) {
		this.nestedList = nestedList;
	}
	
	public void addNested(NestedObject object) {
		int index = nestedList.size();
		nestedList.add(index, object);
	}
	
	public void addNested(int index, NestedObject object) {
		nestedList.add(index, object);
	}
	
	@Silent
	public void addNewNested(NestedObject object) {
		int index = nestedList.size();
		nestedList.add(index, object);
	}
	
	public void removeNested(NestedObject object) {
		int index = nestedList.indexOf(object);
		if (index != -1) {
			nestedList.remove(index);
		}
	}
	
	public void reverseNestedList() {
		NestedObject[] arrNested = nestedList.toArray(
				new NestedObject[nestedList.size()]);
		for (int i = arrNested.length - 1; i >= 0; i--) {
			nestedList.set(arrNested.length - 1 - i, arrNested[i]);
		}
	}
	
	public void addRemoveNested(NestedObject object) {
		int index = nestedList.size();
		nestedList.add(index, object);
		nestedList.remove(index);
	}

	public List<NestedObject> getNestedList1() {
		return nestedList1;
	}

	@Silent
	public void setNestedList1(List<NestedObject> nestedList1) {
		this.nestedList1 = nestedList1;
	}
	
	@Nested
	public void addNested1(NestedObject object) {
		int index = nestedList1.size();
		nestedList1.add(index, object);
	}
	
	// NonNested
	public void addNewNested1(NestedObject object) {
		int index = nestedList1.size();
		nestedList1.add(index, object);
	}
	
	@Nested
	public void removeNested1(NestedObject object) {
		int index = nestedList1.indexOf(object);
		if (index != -1) {
			nestedList1.remove(index);
		}
	}
	
	@Nested
	public void addRemoveNested1(NestedObject object) {
		int index = nestedList1.size();
		nestedList1.add(index, object);
		nestedList1.remove(index);
	}

	public Set<NestedObject> getNestedSet() {
		return nestedSet;
	}

	public void setNestedSet(Set<NestedObject> nestedSet) {
		this.nestedSet = nestedSet;
	}
	
	public void addSetNested(NestedObject object) {
		nestedSet.add(object);
	}
	
	@Silent
	public void addSetNewNested(NestedObject object) {
		nestedSet.add(object);
	}
	
	public void removeSetNested(NestedObject object) {
		nestedSet.remove(object);
	}
	
	public void addSetRemoveNested(NestedObject object) {
		nestedSet.add(object);
		nestedSet.remove(object);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
