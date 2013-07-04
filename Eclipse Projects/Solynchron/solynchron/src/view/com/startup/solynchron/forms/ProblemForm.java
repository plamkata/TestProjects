/**
 * 
 */
package com.startup.solynchron.forms;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.form.RCPForm;

/**
 * @author plamKaTa
 *
 */
public class ProblemForm extends RCPForm {

	/**
	 * @param title
	 * @param parts
	 */
	public ProblemForm(String title, RCPFormPart... parts) {
		super(title, parts);
	}
	
	/**
	 * 
	 * @param title
	 * @param vm
	 * @param parts
	 */
	public ProblemForm(String title, ValidationManager vm, RCPFormPart... parts) {
		super(title, vm, parts);
	}
	
	@Override
	public void initializeUI() {
		super.initializeUI();
		
		// handle any dependencies between the form parts
		// by attaching appropriate listeners
	}

}
