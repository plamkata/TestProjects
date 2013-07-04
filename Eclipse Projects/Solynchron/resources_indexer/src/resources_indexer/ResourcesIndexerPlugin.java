package resources_indexer;

import resources_indexer.database.DatabaseUtil;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;


/**
 * The main plugin class to be used in the desktop.
 */
public class ResourcesIndexerPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static ResourcesIndexerPlugin plugin;
	
	
    /**
     * Error status.
     */
    private static final int ERROR_STATUS = 0;
    
    /**
     * Error message.
     */
    private static final String ERROR_MESSAGE = "Resources Indexer internal error";
    /**
     * Plugin id.
     */
    public static final String ID = "resources_indexer.ResourcesIndexerPlugin";
	
	/**
	 * The constructor.
	 */
	public ResourcesIndexerPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		DatabaseUtil.initDatasource();
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		DatabaseUtil.closeDatasource();
	}

	/**
	 * Returns the shared instance.
	 * @return <code>ResourcesIndexerPlugin</code> shared instance.
	 */
	public static ResourcesIndexerPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("resources_indexer", path);
	}
	
    /**
     * Logs status. 
     * @param status for logging
     */
    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }
    
    /**
     * Logs error exception.
     * @param e <code>Throwable</code> that contains message.
     */
    public static void log(Throwable e) {
        log(new Status(IStatus.ERROR, ID, ERROR_STATUS, ERROR_MESSAGE, e)); 
    }
}
