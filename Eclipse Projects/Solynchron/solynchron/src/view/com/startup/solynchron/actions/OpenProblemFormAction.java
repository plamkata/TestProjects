package com.startup.solynchron.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.startup.solynchron.CodeExecutor;
import com.startup.solynchron.ExecutionManager;
import com.startup.solynchron.InitializationProgressException;
import com.startup.solynchron.Resources;
import com.startup.solynchron.dao.LogicManager;
import com.startup.solynchron.dao.RecordLogic;
import com.startup.solynchron.forms.FormEditorInput;
import com.startup.solynchron.obj.problem.Problem;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class OpenProblemFormAction extends OpenFormEditorAction {
    
	/**
	 * The constructor.
	 */
	public OpenProblemFormAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		Object count = ExecutionManager.executeInTransaction(
		new CodeExecutor("open") {
			public Object execute(Object[] params) throws Exception {
				// first reindex existing data
				return newProblem();
			}
		}, null);
		
		if (count != null) {
			MessageDialog.openInformation(
				getWindow().getShell(),
    			"Sample_derby Plug-in",
    			"We have " + count + 
				" records in the database");
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	private Object newProblem() throws InitializationProgressException {
		RecordLogic logic = (RecordLogic) LogicManager.get(LogicManager.RECORD_LOGIC);
		Long result = logic.incrementRecord();
		if (result != null) {
			FormEditorInput input = new FormEditorInput(
					Resources.getResource("OpenProblemFormAction.name"), 
					new Problem());
			
			openEditor(input, "com.startup.solynchron.problem-editor"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return result;
	}
	
}