/**
 * 
 */
package com.damnhandy.aspects.syncmodel.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.swing.SwingUtilities;

/**
 * Dispatcher class which has been lifted from the Spin project.
 * 
 * @author Ryan J. McDonough
 * 
 */
public class Dispatcher implements InvocationHandler {

	/**
	 * The AWT conditional class.
	 */
	private static Class conditionalClass;

	/**
	 * The pumpMethod of the EDT.
	 */
	private static Method pumpMethod;

	/**
	 * Flag indicating that dispatching should stop.
	 */
	private boolean stopDispatching = false;

	/**
	 * Start the dispatching.
	 * 
	 * @throws Exception
	 */
	public void start() throws Throwable {
		try {
			Object conditional = 
				Proxy.newProxyInstance(conditionalClass.getClassLoader(), 
									  new Class[] { conditionalClass }, 
									  this);
			pumpMethod.invoke(Thread.currentThread(),
							 new Object[] { conditional });

			synchronized (this) {
				// if the EDT refuses to pump events (e.g. because of a
				// sun.awt.AWTAutoShutdown)
				// we can do nothing else but wait for stop() to be called
				while (!stopDispatching) {
					wait();
				}
			}
		} catch (InvocationTargetException ex) {
			throw ex.getTargetException();
		}
	}

	/**
	 * Stop dispatching.
	 */
	public void stop() {
		synchronized (this) {
			stopDispatching = true;
			// notify possibly waiting start()
			notifyAll();
		}
		// force the event queue to re-evaluate our conditional
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			}
		});
	}

	/**
	 * Invoke <code>evaluate()</code> on the wrapped <code>Conditional</code>
	 * instance - called by the EventDispatchThread to test if pumping of events
	 * should be continued.
	 * 
	 * @return <code>true</code> if events still should be continued pumped,
	 *         <code>false</code> otherwise
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		if (stopDispatching) {
			return Boolean.FALSE;
		} else {
			return Boolean.TRUE;
		}
	}

	/**
	 * Initialize AWT internals. <br>
	 * Get references to class <code>java.awt.Conditional</code> and method
	 * <code>java.awt.EventDispatchThread.pumpEvents()</code>.
	 */
	static {
		try {
			conditionalClass = Class.forName("java.awt.Conditional");
			pumpMethod = Class.forName("java.awt.EventDispatchThread")
				.getDeclaredMethod("pumpEvents", new Class[] { conditionalClass });
			pumpMethod.setAccessible(true);
		} catch (Exception ex) {
			throw new Error(ex.getMessage());
		}
	}
}
