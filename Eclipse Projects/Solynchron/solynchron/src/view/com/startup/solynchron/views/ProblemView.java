/**
 * 
 */
package com.startup.solynchron.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

import com.startup.solynchron.Resources;

/**
 * 
 * @author plamKaTa
 *
 */
public class ProblemView extends ViewPart {
	
	private FormToolkit toolkit;
	
	private ScrolledForm form;

	/**
	 * 
	 */
	public ProblemView() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText(Resources.getResource("ProblemView.title"));
		
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}
	
	@Override
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

}
