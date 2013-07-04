/**
 * 
 */
package com.startup.solynchron.forms;

import org.eclipse.core.runtime.IProgressMonitor;

import net.sf.rcpforms.form.RCPFormEditorPart;

/**
 * @author plamKaTa
 *
 */
public class SearchFormEditorPart extends RCPFormEditorPart<SearchForm> {

	/**
	 * @param form
	 */
	public SearchFormEditorPart(SearchForm form) {
		super(form);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		setDirty(false);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

}
