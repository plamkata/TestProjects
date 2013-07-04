package indexer.builder;

import indexer.Activator;
import indexer.database.DatabaseUtil;
import indexer.database.ResourceEntity;

import java.sql.SQLException;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class SampleBuilder extends IncrementalProjectBuilder {

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			if (! (resource instanceof IFile) ) {
				return true;
			}
			try {
				switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					// handle added resource
					DatabaseUtil.addResource(new ResourceEntity(resource));
					break;
				case IResourceDelta.REMOVED:
					DatabaseUtil.removeResource(new ResourceEntity(resource));
					break;
				}
			} catch (SQLException ex) {
				Activator.log(ex);
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class SampleResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			try {
				if (resource instanceof IFile) {
					DatabaseUtil.addResource(new ResourceEntity(resource));
				}
			} catch (SQLException ex) {
				Activator.log(ex);
			}
			//return true to continue visiting children.
			return true;
		}
	}

	public static final String BUILDER_ID = "indexer.sampleBuilder";

	/**
	 * @see  IncrementalProjectBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	/**
	 * Handles full build.
	 * @param monitor monitor used for building.
	 * @throws CoreException
	 */
	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new SampleResourceVisitor());
		} catch (CoreException e) {
			Activator.log(e);
		}
	}

	/**
	 * Handles incremental build.
	 * @param delta <code>IResourceDelta</code> to use for building.
	 * @param monitor monitor to use for building
	 * @throws CoreException
	 */
	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new SampleDeltaVisitor());
	}
	
	/**
	 * Clean current project.
	 * @param monitor monitor to use for building
	 */
	protected void clean(IProgressMonitor monitor) throws CoreException {
		try {
			DatabaseUtil.cleanProject(getProject().getName());
		} catch (SQLException ex) {
			Activator.log(ex);
		}
	}
}
