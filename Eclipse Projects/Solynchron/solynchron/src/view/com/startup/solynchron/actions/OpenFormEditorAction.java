/**
 * 
 */
package com.startup.solynchron.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.progress.UIJob;

import com.startup.solynchron.Activator;
import com.startup.solynchron.forms.FormEditorInput;

/**
 * @author plamKaTa
 *
 */
public abstract class OpenFormEditorAction 
		implements IWorkbenchWindowActionDelegate {
	
	private IWorkbenchWindow window;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {

	}
	
	protected void openEditor(String inputName, String editorId) {
		openEditor(new FormEditorInput(inputName), editorId);
	}
	
	protected void openEditor(final IEditorInput input, final String editorId) {
		// ensure that this action is executed in the UI thread
		UIJob job = new UIJob(getWindow().getShell().getDisplay(), input.getName()) {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				IStatus status = Status.CANCEL_STATUS;
				
				IWorkbenchPage page = window.getActivePage();
				try {
					page.openEditor(input, editorId);
					status = Status.OK_STATUS;
				} catch (PartInitException ex) {
					Activator.log(ex);
				}
				
				return status;
			}
		};
		job.schedule();
	}
	
	protected IWorkbenchWindow getWindow() {
		return window;
	}

}
