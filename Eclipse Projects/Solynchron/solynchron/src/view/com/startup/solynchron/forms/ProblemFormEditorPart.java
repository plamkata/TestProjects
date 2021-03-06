/**
 * 
 */
package com.startup.solynchron.forms;

import net.sf.rcpforms.form.RCPFormEditorPart;

import org.eclipse.core.runtime.IProgressMonitor;

import com.startup.solynchron.Activator;
import com.startup.solynchron.CodeExecutor;
import com.startup.solynchron.ExecutionManager;
import com.startup.solynchron.obj.problem.Problem;

/**
 * @author plamKaTa
 *
 */
public class ProblemFormEditorPart extends RCPFormEditorPart<ProblemForm> {

	/**
	 * @param form
	 */
	public ProblemFormEditorPart(ProblemForm form) {
		super(form);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		FormEditorInput input = (FormEditorInput) getEditorInput();
		Object[] models = input.getModels();
		if (models.length > 0) {
			Object result = ExecutionManager.executeInTransaction(
					new CodeExecutor(models[0]) {
				@Override
				public Object execute(Object[] params) throws Exception {
					Problem problem = (Problem) params[0];
					if (problem.isNew()) {
						Activator.getSession().persist(problem);
					} else {
						Activator.getSession().merge(problem);
					}
					return problem;
				}
			}, true, models);
			
			if (result != null) {
				setDirty(false);
			}
		}
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
