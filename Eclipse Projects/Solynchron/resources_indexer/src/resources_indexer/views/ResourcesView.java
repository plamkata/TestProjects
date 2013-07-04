package resources_indexer.views;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import resources_indexer.ResourcesIndexerPlugin;
import resources_indexer.database.DatabaseUtil;
import resources_indexer.database.ResourceEntity;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class ResourcesView extends ViewPart {
	private TreeViewer viewer;
	private Action doubleClickAction;
	
	 
	class TreeObject implements IAdaptable {
		private String name;
		private TreeParent parent;
		
		/**
		 * Constructs tree object by given name.
		 * @param name name of new object
		 */
		public TreeObject(String name) {
			this.name = name;
		}
		/**
		 * returns object name.
		 * @return <code>String</code> name value
		 */
		public String getName() {
			return name;
		}
		/**
		 * Sets tree parent for element.
		 * @param parent parent for the object to set
		 */
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		/**
		 * Returns objects tree parent.
		 * @return parent of the object
		 */
		public TreeParent getParent() {
			return parent;
		}
		
		/**
		 * String representation of object.
		 * @return String representation of object
		 */
		public String toString() {
			return getName();
		}
		/**
		 * @see IAdaptable#getAdapter(java.lang.Class)
		 */
		public Object getAdapter(Class key) {
			return null;
		}
	}
	
	/**
	 * Extended TreeObject Class which can have children.
	 * @author Ilya Platonov
	 *
	 */
	class TreeParent extends TreeObject {
		/**
		 * List of children.
		 */
		private ArrayList children;
		/**
		 * Constructs by name.
		 * @param name name of object
		 */
		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}
		/**
		 * Adds new child.
		 * @param child child to add
		 */
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		/**
		 * removes child.
		 * @param child child to remove
		 */
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		/**
		 * Returns array of children.
		 * @return array of childeren
		 */
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		/**
		 * Checks if object has children.
		 * @return <code>true</code> if have and <code>false</code> if do not. 
		 */
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	/**
	 * Width of search input text widget. 
	 */
	private final int TEXT_FIELD_WIDTH = 200;
	
	/**
	 * Search input component.
	 */
	private Text searchInput = null;
	
	/**
	 * Toolkit for creating widgets.
	 */
	private FormToolkit toolkit = null;
	
	/**
	 * Resource search action.
	 */
	private Action searchAction = null;

	class ViewContentProvider implements IStructuredContentProvider, 
	   ITreeContentProvider {
		private TreeParent invisibleRoot;
	
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot == null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}

		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject) child).getParent();
			}
			return null;
		}

		public Object[] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent) parent).getChildren();
			}
			return new Object[0];
		}

		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent) parent).hasChildren();
			return false;
		}
		
		private void initialize() {
			TreeObject to1 = new TreeObject("Leaf 1");
			TreeObject to2 = new TreeObject("Leaf 2");
			TreeObject to3 = new TreeObject("Leaf 3");
			TreeParent p1 = new TreeParent("Parent 1");
			p1.addChild(to1);
			p1.addChild(to2);
			p1.addChild(to3);
			
			TreeObject to4 = new TreeObject("Leaf 4");
			TreeParent p2 = new TreeParent("Parent 2");
			p2.addChild(to4);
			
			TreeParent root = new TreeParent("Root");
			root.addChild(p1);
			root.addChild(p2);
			
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(root);
		}
}
	
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_FOLDER;
			if (obj instanceof TreeParent)
			   imageKey = ISharedImages.IMG_OBJ_FILE;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}

	/**
	 * The constructor.
	 */
	public ResourcesView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(final Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		toolkit.adapt(parent);
		
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		parent.setLayout(gl);
		
		GridData gd = new GridData();
		gd.widthHint = TEXT_FIELD_WIDTH;
		
		searchInput = toolkit.createText(parent, "", SWT.BORDER);
		searchInput.setLayoutData(gd);
		
		final Button searchButton = toolkit.createButton(parent, "Search", SWT.NONE);
		searchButton.addSelectionListener( new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				parent.getDisplay().asyncExec(new Runnable() {
					public void run() {
						searchButton.setEnabled(false);
						searchAction.run();
						searchButton.setEnabled(true);
					}
				});
			}
		}
		);
		
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		
		viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getTree().setLayoutData(gd);
		
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setInput("Hello");
		
		initActions();
		hookDoubleClickAction();
	}

	/**
	 * Initilizes actions for this view.
	 */
	private void initActions() {
		searchAction = new Action() {
			public void run() {
				String searchString = searchInput.getText();
				try {
					
					List list = DatabaseUtil.getResources(searchString);
					
					TreeParent rootElement = new TreeParent("");
					TreeParent currentElement = null;
					
					String lastElement = null;
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						
						ResourceEntity element = (ResourceEntity) iter.next();
						
						String name = element.getResourceName();
						
						if (lastElement == null || !name.equals(lastElement)) {
							currentElement = new TreeParent(name);
							rootElement.addChild(currentElement);
							lastElement = name;
						}
						int lastSlash = element.getResourcePath().lastIndexOf("/");
						currentElement.addChild(new TreeObject(element.getResourcePath().substring(0, lastSlash + 1)));
					}
					
					viewer.setInput(rootElement);
					viewer.refresh();
					
				} catch (SQLException ex) {
					ResourcesIndexerPlugin.log(ex);
					showMessage("Error quering database");
				}
			}
		};
		
		doubleClickAction = new Action() {
			public void run() {
				Object element = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
				if (element instanceof TreeObject && !(element instanceof TreeParent)) {
					TreeObject treeObject = (TreeObject) element;
					IWorkbenchPage page = PlatformUI.getWorkbench()
	                .getActiveWorkbenchWindow().getActivePage();
					IFile resource = ResourcesPlugin.getWorkspace().getRoot().
					getFile(new Path(treeObject.getName() + treeObject.getParent().getName()));
					if (resource != null) {
						try {
							IDE.openEditor(page,resource,true);
						} catch (CoreException ex) {
							ResourcesIndexerPlugin.log(ex);
						}
					}
				}
			};
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Resources View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}