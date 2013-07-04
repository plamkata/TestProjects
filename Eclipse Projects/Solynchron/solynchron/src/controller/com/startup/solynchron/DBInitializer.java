/**
 * 
 */
package com.startup.solynchron;

import java.io.File;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import com.startup.solynchron.obj.LocalUser;
import com.startup.solynchron.obj.MyUser;
import com.startup.solynchron.obj.domain.DomainCategory;
import com.startup.solynchron.obj.domain.ToolType;
import com.startup.solynchron.obj.problem.ArtifactType;
import com.startup.solynchron.obj.problem.ProblemType;
import com.startup.solynchron.obj.solution.SolutionMethod;

/**
 * This class is responsible for database creation, initialization and 
 * propagation with system data. Connecting to the database might be 
 * an expensive operation on first access when the database must be 
 * created from scratch. Hence, this class provides progress and cancellation 
 * monitoring functionality for this process.
 * 
 * @author plamKaTa
 *
 */
class DBInitializer extends Job {

	
	private static final String INITIALIZATION_JOB_NAME = 
		"Solynchron Plug-in Initialization";

	private static final String CONNECTION_URL = "jdbc:derby:SolynchronDB";
	
	private static final String INDEX_FOLDER = "SolynchronIndex";
	
	private static final String PERSISTENCE_UNIT_NAME = "solunit";
	
	private EntityManagerFactory emf;
	
	public DBInitializer() {
		super(INITIALIZATION_JOB_NAME);
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		return initEntityManagerFactory(monitor);
	}
	
	/**
	 * Initializes the entity manager factory. If the database dose not exist
	 * this method will automatically create the database and init the factory.
	 * 
	 * @param monitor progress monitor for tracking creation of the entity
	 * manager factory
	 * @return the status of initialization; if cancel status is returned 
	 * this means that plug-in initialization failed and the plug-in is deactivated
	 */
	public IStatus initEntityManagerFactory(IProgressMonitor monitor) {
		IStatus status = Status.OK_STATUS;
		monitor.beginTask("Initialaize database.", 5);
		try {
			// Step 1: Create factory (database might not exist)
			emf = buildEntityManagerFactory(false);
			tick(monitor, 1);
			
			// create the database if it did not exist
			if (emf == null) {
				// Step 2: If the factory was not created create the database
				status = createDB(monitor);
				// Step 3: tick
				tick(monitor, 1);
			} else {
				// Step 2: Use the factory and check if database exists
				boolean dbExists = checkDBExists();
				tick(monitor, 1);

				if (!dbExists) {
					// Step 3: If database dose not exist, create the database
					status = createDB(monitor);
				}
				else tick(monitor, 1);
			}

			// Step 4: Check if data already exists in database
			boolean dataExists = checkDataExists();
			tick(monitor, 1);

			// Step 5: Propagate initial data in database
			if (!dataExists) {
				status = createData(new SubProgressMonitor(monitor, 1));
			} else tick(monitor, 1);
			
		} catch (Throwable e) {
			// initialization have failed: log the reason and return a cancel status
			Activator.log(e);
			status = Status.CANCEL_STATUS;
		} finally {
			monitor.done();
		}
		return status;
	}

	/**
	 * Checks if the database exists and was previously created.
	 * 
	 * @return true, if the database have been correctly created 
	 */
	private boolean checkDBExists() {
		boolean exists = false;
		
		try {
			EntityManager em = emf.createEntityManager();
			try {
				em.find(LocalUser.class, new Long(1));
				exists = true;
			} finally {
				if (em != null && em.isOpen()) em.close();
			}
		} catch (Exception e) {		
			if (emf.isOpen()) emf.close();
			emf = null;
		}
		
		return exists;
	}
	
	/**
	 * Check if system tables are populated with data.
	 * 
	 * @return true, if all data have been populated correctly to the database
	 */
	private boolean checkDataExists() {
		boolean exists = false;
		
		EntityManager em = emf.createEntityManager();
		try {
			Object result = em.createQuery("from LocalUser u").getSingleResult();
			if (result != null) exists = true;
		} catch (NoResultException e) {
			exists = false;
		} finally {
			if (em != null && em.isOpen()) em.close();
		}
		
		return exists;
	}
	
	/**
	 * Create the database for the first time.
	 * 
	 * @param monitor the progress monitor
	 * @return the status of database creation
	 * @throws Throwable if something goes wrong
	 */
	private IStatus createDB(IProgressMonitor monitor) throws Throwable {
		// Step 1: Create the database meta-data from hibernate configuration
		emf = buildEntityManagerFactory(true);
		tick(monitor, 1);
		
		return Status.OK_STATUS;
	}
	
	/**
	 * Populates database system tables with preliminary data. If something goes
	 * wrong no data will be populated at all.
	 * 
	 * @param monitor monitor progress and cancellation
	 * @return the status of whether all data was correctly populated in database
	 * @throws Throwable if something goes wrong with database work
	 */
	private IStatus createData(final IProgressMonitor monitor) throws Throwable {
		IStatus status = Status.OK_STATUS;
		monitor.beginTask("Solynchron Data Initialization", 6);
		
		EntityManager em = emf.createEntityManager();
		em.setFlushMode(FlushModeType.COMMIT);
		try {
			EntityTransaction t = em.getTransaction();
			try {
				t.begin();
				
				// Step 1: Create the local user
				status = createUser(new SubProgressMonitor(monitor, 1), em);
				
				// Step 2: Create tool types
				status = createToolTypes(new SubProgressMonitor(monitor, 1), em);
				
				// Step 3: Create domain categories
				status = createDomainCategories(new SubProgressMonitor(monitor, 1), em);
				
				// Step 4: Create problem types
				status = createProblemTypes(new SubProgressMonitor(monitor, 1), em);
				
				// Step 5: Create artifact types
				status = createArtifactTypes(new SubProgressMonitor(monitor, 1), em);
				
				// Step 6: Create Solution methods
				status = createSolutionMethods(new SubProgressMonitor(monitor, 1), em);
				
				t.commit();
			} catch (PersistenceException e) {
				if (t != null && t.isActive()) t.rollback();
				throw e;
			}
		} finally {
			em.close();
			monitor.done();
		}
		
		return status;
	}

	/**
	 * Creates the native local user if it dose not exist.
	 * 
	 * @param monitor report progress in a separate monitor
	 * @param em use the custom entity manager for initialization
	 * @return status of creation
	 */
	private IStatus createUser(IProgressMonitor monitor, EntityManager em) {
		monitor.beginTask("Create Native User", 1);
		try {
			MyUser user = new LocalUser();
			user.setName(System.getProperty("user.name"));
			em.persist(user);
			tick(monitor, 1);
		} finally {
			monitor.done();
		}
		return Status.OK_STATUS;
	}
	
	/**
	 * Create tool types system data.
	 * 
	 * @param monitor monitor progress and cancellation
	 * @param em entity manager to be used transactionally
	 * @return the status of tool types creation
	 * @throws Exception if something went wrong
	 */
	private IStatus createToolTypes(IProgressMonitor monitor, EntityManager em) 
	throws Exception {
		String[] names = new String[] {
				"os", "database", "ui", "platform", "library", "plug-in"};
		monitor.beginTask("Create Tool Types", names.length);
		
		try {
			for (int i = 0; i < names.length; i++) {
				ToolType type = new ToolType();
				type.setName(names[i]);
				em.persist(type);
				
				tick(monitor, 1);
			}
		} finally {
			monitor.done();
		}
		
		return Status.OK_STATUS;
	}
	
	private IStatus createDomainCategories(IProgressMonitor monitor, EntityManager em) {
		monitor.beginTask("Create Domain Categories", 3);
		
		try {
			DomainCategory general = new DomainCategory();
			general.setName("General");
			
			tick(monitor, 1);
			
			DomainCategory programming = new DomainCategory();
			programming.setName("Programming");
			programming.setParent(general);
			general.getChildren().add(programming);

			tick(monitor, 1);
			
			DomainCategory database = new DomainCategory();
			database.setName("Database");
			database.setParent(programming);
			programming.getChildren().add(database);
			
			DomainCategory app = new DomainCategory();
			app.setName("Application");
			app.setParent(programming);
			programming.getChildren().add(app);
			
			DomainCategory ui = new DomainCategory();
			ui.setName("UI");
			ui.setParent(programming);
			programming.getChildren().add(ui);
			
			DomainCategory low = new DomainCategory();
			low.setName("LowLevel");
			low.setParent(programming);
			programming.getChildren().add(low);

			tick(monitor, 1);
			
			em.persist(general);
		} finally {
			monitor.done();
		}
		
		return Status.OK_STATUS;
	}
	
	private IStatus createProblemTypes(IProgressMonitor monitor, EntityManager em) {
		monitor.beginTask("Create Problem Types", 3);
		
		try {
			ProblemType error = new ProblemType();
			error.setName("Error");
			error.setDescription("Describe a common error which is encountered with the specified tools.");
			em.persist(error);
			tick(monitor, 1);
			
			ProblemType howToUse = new ProblemType();
			howToUse.setName("How to use");
			howToUse.setDescription("Describe what do you want to achieve by using the specified tools.");
			em.persist(howToUse);
			tick(monitor, 1);
			
			ProblemType howToConfigure = new ProblemType();
			howToConfigure.setName("How to configure");
			howToConfigure.setDescription("Describe what do you want to achieve by configuration of the specified tools.");
			em.persist(howToConfigure);
			tick(monitor, 1);
		} finally {
			monitor.done();
		}
		
		return Status.OK_STATUS;
	}
	
	private IStatus createArtifactTypes(IProgressMonitor monitor, EntityManager em) {
		monitor.beginTask("Create Artifact Types", 5);
		
		try {
			ArtifactType sourceCode = new ArtifactType();
			sourceCode.setName("Source code");
			em.persist(sourceCode);
			tick(monitor, 1);
			
			ArtifactType exc = new ArtifactType();
			exc.setName("Exception");
			em.persist(exc);
			tick(monitor, 1);
			
			ArtifactType config = new ArtifactType();
			config.setName("Configuration");
			em.persist(config);
			tick(monitor, 1);
			
			ArtifactType url = new ArtifactType();
			url.setName("URL");
			em.persist(url);
			tick(monitor, 1);
			
			ArtifactType sciRef = new ArtifactType();
			sciRef.setName("Scientific Reference");
			em.persist(sciRef);
			tick(monitor, 1);
			
		} finally {
			monitor.done();
		}
		
		return Status.OK_STATUS;
	}
	
	private IStatus createSolutionMethods(IProgressMonitor monitor, EntityManager em) {
		monitor.beginTask("Create solution methods", 5);
		
		try {
			SolutionMethod note = new SolutionMethod();
			note.setName("Notice");
			em.persist(note);
			tick(monitor, 1);
			
			SolutionMethod config = new SolutionMethod();
			config.setName("Configuration");
			em.persist(config);
			tick(monitor, 1);
			
			SolutionMethod exc = new SolutionMethod();
			exc.setName("Proper Use");
			em.persist(exc);
			tick(monitor, 1);
			
			SolutionMethod workaround = new SolutionMethod();
			workaround.setName("Workaround");
			em.persist(workaround);
			tick(monitor, 1);
			
			SolutionMethod depend = new SolutionMethod();
			depend.setName("Dependency");
			em.persist(depend);
			tick(monitor, 1);
			
		} finally {
			monitor.done();
		}
		
		return Status.OK_STATUS;
	}
	
	/**
	 * Build the entity manager factory for accessing the database. If the database
	 * dose not exist it can be created by specifying create = true.
	 * 
	 * @param create whether the database should be created or it already exists
	 * @return an instance of the entity manager factory
	 */
	private EntityManagerFactory buildEntityManagerFactory(boolean create) {
    	// determine database path at runtime
    	String dbFilePath = Activator.getDefault()
    		.getStateLocation().toFile().getAbsolutePath();
		System.setProperty("derby.system.home", dbFilePath);
		System.out.println("Database is located in: " + dbFilePath);
		
		Properties props = new Properties();
    	if (create) {
    		// database dose not exist - create it
			props.put("hibernate.connection.url", 
					CONNECTION_URL + ";create=true");
			props.put("hibernate.hbm2ddl.auto", "create");
    	}
    	props.put("hibernate.search.default.indexBase", dbFilePath + File.separator + INDEX_FOLDER);
    	System.out.println("Lucene indexes are located in: " + dbFilePath);
    	
    	return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, props);
    }


	/**
	 * Perform certain amount of work with the given monitor and check for cancellation.
	 * 
	 * @param monitor the monitor to be invoked
	 * @param work the amount of work to be marked as done
	 */
	private void tick(IProgressMonitor monitor, int work) {
		monitor.worked(work);
		if (monitor.isCanceled()) throw new OperationCanceledException();
	}
	
	/**
	 * Get the entity manager factory.
	 * 
	 * @return the entity manager factory
	 * @throws InitializationProgressException
	 */
	public EntityManagerFactory getEntityManagerFactory() 
	throws InitializationProgressException {
		return emf;
	}

	/**
	 * Dispose the entity manager factory.
	 */
	public void disposeEntityManagerFactory() {
		if (emf != null && emf.isOpen()) {
			emf.close();
			emf = null;
		}
	}

}