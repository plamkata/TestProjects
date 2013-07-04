package com.startup.solynchron;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.startup.solynchron.dao.LogicManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "solynchron";
	
	private static final int ERROR_STATUS = 0;
    
    private static final String ERROR_MESSAGE = "Simple Solynchron internal error";

    
	// The shared instance
	private static Activator plugin;
	
	// shared plug-in colors
	private FormColors formColors;
	
	// Database initializer
	private DBInitializer dbInit;
	
	// marks the successful end of initialization process
	private boolean initialized = false;
	
	// local storage for the current entity manager (hibernate session)
	private final ThreadLocal local = new ThreadLocal();
	
	/**
	 * The constructor
	 */
	public Activator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(final BundleContext context) throws Exception {
		plugin = this;
		super.start(context);
		LogicManager.init();
		
		// initialization of the entity manager factory is an expensive operation
		// that may include creation of the database (on first access);
		// i.e. it is run in background
		dbInit = new DBInitializer();
		dbInit.addJobChangeListener(new JobChangeAdapter() {
			@Override
			public void done(IJobChangeEvent event) {
				super.done(event);
				if (event.getResult().isOK()) {
					synchronized (event.getJob()) {
						initialized = true;
					}
				}
				ExecutionManager.flushExecutionQueue(new NullProgressMonitor());
			}
		});
		dbInit.schedule();
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		ImageManager.initialize(reg);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		try {
			dbInit.disposeEntityManagerFactory();
			if (formColors != null) {
				formColors.dispose();
				formColors = null;
			}
			// automatically disposed in super.stop(...)
			// ImageManager.dispose(getImageRegistry());
			LogicManager.dispose();
			plugin = null;
		} finally {
			super.stop(context);
		}
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public FormColors getFormColors(Display display) {
		if (formColors == null) {
			formColors = new FormColors(display);
			formColors.markShared();
		}
		return formColors;
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
	 * Check if the bundle initialization has finished.
	 * 
	 * @return true, if initialization have finished
	 */
	public synchronized boolean isInitialized() {
		return initialized;
	}
	
	/**
	 * This method ensures that the bundle activator have been initialized 
	 * properly, otherwise it throws an initialization progress exception; 
	 * i.e. the plug-in initialization process is still in progress.  
	 * 
	 * @throws InitializationProgressException if the bundle is in an 
	 * initialization progress.
	 */
	public synchronized void initialized() 
		throws InitializationProgressException {
		
		if (!initialized) 
			throw new InitializationProgressException();
	}
	
	/**
	 * Get the entity manager factory.
	 * 
	 * @return the entity manager factory
	 * @throws InitializationProgressException
	 */
	private EntityManagerFactory getEntityManagerFactory() 
	throws InitializationProgressException {
		initialized();
		return dbInit.getEntityManagerFactory();
	}

	public static EntityManager getSession() throws InitializationProgressException {
        EntityManager em = (EntityManager) getDefault().local.get();
        // Open a new Session, if this thread has none yet
        if (em == null) {
            EntityManagerFactory emf = getDefault().getEntityManagerFactory();
            if (emf == null) throw new InitializationProgressException();
			em = emf.createEntityManager();
			em.setFlushMode(FlushModeType.COMMIT);
            // Store it in the ThreadLocal variable
            getDefault().local.set(em);
        }
        return em;
    }

    public static void closeSession() {
    	if (getDefault() != null) {
	        EntityManager em = (EntityManager) getDefault().local.get();
	        if (em != null && em.isOpen()) em.close();
	        getDefault().local.set(null);
    	}
    }

	/**
     * Logs status. 
     * @param status for logging
     */
    public static void log(IStatus status) {
    	if (getDefault() != null) {
    		getDefault().getLog().log(status);
    	}
    }
    
    /**
     * Logs error exception.
     * @param e <code>Throwable</code> that contains message.
     */
    public static void log(Throwable e) {
    	log(new Status(IStatus.ERROR, PLUGIN_ID, ERROR_STATUS, ERROR_MESSAGE, e)); 
    }
}