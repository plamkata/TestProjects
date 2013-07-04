package indexer;

import indexer.database.DatabaseUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "indexer";
	
	/**
     * Error status.
     */
    private static final int ERROR_STATUS = 0;
    
    /**
     * Error message.
     */
    private static final String ERROR_MESSAGE = "Resources Indexer internal error";

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		DatabaseUtil.initDatasource();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
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
        log(new Status(IStatus.ERROR, PLUGIN_ID, ERROR_STATUS, ERROR_MESSAGE, e)); 
    }
}
