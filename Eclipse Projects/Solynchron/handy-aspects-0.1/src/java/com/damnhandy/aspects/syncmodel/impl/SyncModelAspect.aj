package com.damnhandy.aspects.syncmodel.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;

import javax.swing.SwingUtilities;

import com.damnhandy.aspects.syncmodel.SyncModel;


/**
 * TDB
 * @author Ryan J. McDonough
 * @since 1.0
 * @version $Revision: 1.3 $
 */
public aspect SyncModelAspect {

	declare warning : execution(@SyncModel * *(..)): 
		"This Aspect is ALPHA quality and not yet intended for production use.";
	
	/**
	 * 
	 */
	private static final Executor executor = new Executor() {
        public void execute(Runnable command) {
            Thread t = new Thread(command,"SM-Thread");
        		t.start();
        }
    };
	
	pointcut syncmodel() : execution(@SyncModel * *(..));
	
	/**
	 * If the method is executing on the EDT, method execution is
	 * then moved within a Callable.
	 */
	Object around() : syncmodel() {
		if (SwingUtilities.isEventDispatchThread()) {
			final Dispatcher dispatcher = new Dispatcher();
			FutureTask<Object> task = 
				new FutureTask<Object>(new Callable<Object>() {
				
				public Object call() throws Exception {
					try {
						Object result = proceed();
						dispatcher.stop();
						return result;
					} catch (Throwable e) {
						throw new Exception(e);
					}
				}
			});
			executor.execute(task);
			try {
				dispatcher.start();
				return task.get();
			} catch (Throwable e) {
				return proceed();
			}
		}
		else {
			return proceed();
		}
	}
}
