package net.sf.swtbot.finder;

import java.util.List;

import net.sf.swtbot.utils.SWTUtils;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

/**
 * Performs operations in the UI thread. If the {@link #run()} method of this class is called from an non-UI thread, the
 * instance ensures that it runs in the UI thread by invoking {@link Display#syncExec(Runnable)}, else it executes in
 * the UI thread. All operations are blocking operations.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: UIThreadRunnable.java 772 2008-06-22 04:19:08Z kpadegaonkar $
 */
public abstract class UIThreadRunnable implements Runnable {

	private static final Logger	log	= Logger.getLogger(UIThreadRunnable.class);

	/**
	 * A runnable that returns a {@link Widget}.
	 */
	public interface WidgetResult {
		/**
		 * @return a {@link Widget}
		 */
		Widget run();
	}

	/**
	 * A runnable that returns an {@link Object}.
	 */
	public interface ObjectResult {
		/**
		 * @return an {@link Object}
		 */
		Object run();
	}

	/**
	 * A runnable that returns nothing.
	 */
	public interface VoidResult {
		/**
		 * return nothing.
		 */
		void run();
	}

	/**
	 * A runnable that returns a boolean.
	 */
	public interface BoolResult {
		/**
		 * @return a boolean
		 */
		boolean run();
	}

	/**
	 * A runnable that returns an integer.
	 */
	public interface IntResult {
		/**
		 * @return an integer
		 */
		int run();
	}

	/**
	 * A runnable that returns a character.
	 */
	public interface CharResult {
		/**
		 * @return a character
		 */
		char run();
	}

	/**
	 * A runnable that returns a {@link String}.
	 */
	public interface StringResult {
		/**
		 * @return a {@link String}
		 */
		String run();
	}

	/**
	 * A runnable that returns a {@link List}.
	 */
	public interface ListResult {
		/**
		 * @return a {@link List}
		 */
		List run();
	}

	/** the display on which runnables must be executed. */
	protected final Display	display;
	/**
	 * A flag to denote if the runnable should execute asynchronously.
	 */
	private final boolean	async;

	/**
	 * Runs synchronously in the UI thread.
	 * 
	 * @param display The display to be used.
	 */
	private UIThreadRunnable(Display display) {
		this(display, false);
	}

	/**
	 * A private contructor use to create this object.
	 * 
	 * @param display The display to use.
	 * @param async if the thread should run asynchronously or not.
	 * @see Display#syncExec(Runnable)
	 * @see Display#asyncExec(Runnable)
	 */
	private UIThreadRunnable(Display display, boolean async) {
		this.display = display;
		this.async = async;
	}

	/**
	 * This method is intelligent to execute in the UI thread.
	 */
	public void run() {
		if ((display == null) || display.isDisposed())
			return;

		if (!isUIThread()) {
			if (async)
				display.asyncExec(runnable());
			else
				display.syncExec(runnable());
		} else
			doRun();
	}

	/**
	 * A runnable instance that is used internally.
	 * 
	 * @return The runnable instance.
	 */
	private Runnable runnable() {
		final Runnable runnable = new Runnable() {
			public void run() {
				doRun();
				dispatchAllEvents();
			}
		};
		return runnable;
	}

	/**
	 * This dispatched events in the UI thread.
	 * <p>
	 * This must be called in the UI thread only. This method does not execute in a syncexec/asyncexec block
	 * </p>
	 */
	private void dispatchAllEvents() {
		log.trace("Dispatching events");
		while (true)
			if (!display.readAndDispatch())
				break;
		log.trace("Finished dispatching");
	}

	/**
	 * Performs the run in the UI Thread.
	 * <p>
	 * This MUST be invoked in the UI thread.
	 * </p>
	 */
	protected abstract void doRun();

	/**
	 * Return true if the current thread is the UI thread.
	 * 
	 * @return <code>true</code> if this instance is running in the UI thread, <code>false</code> otherwise.
	 */
	public boolean isUIThread() {
		return isUIThread(display);
	}

	/**
	 * Return true if the current thread is the UI thread.
	 * 
	 * @param display the display
	 * @return <code>true</code> if the current thread is the UI thread, <code>false</code> otherwise.
	 */
	public static boolean isUIThread(Display display) {
		return display.getThread() == Thread.currentThread();
	}

	/**
	 * Executes the {@code toExecute} on the UI thread, and blocks the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @return the result of executing result on the UI thread.
	 * @since 1.0
	 */
	public static Object syncExec(final UIThreadRunnable.ObjectResult toExecute) {
		return syncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the display thread, and blocks the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 * @return the object result of execution on the UI thread.
	 */
	public static Object syncExec(Display display, final UIThreadRunnable.ObjectResult toExecute) {
		final Object wrapper[] = new Object[1];
		new UIThreadRunnable(display) {
			protected void doRun() {
				wrapper[0] = toExecute.run();
			}
		}.run();
		return wrapper[0];
	}

	/**
	 * Executes the {@code toExecute} on the UI thread, and blocks the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @return the integer result of execution on the UI thread.
	 * @since 1.0
	 */
	public static int syncExec(final UIThreadRunnable.IntResult toExecute) {
		return syncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the display thread, and blocks the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 * @return the integer result of execution on the UI thread.
	 */
	public static int syncExec(Display display, final UIThreadRunnable.IntResult toExecute) {
		final int wrapper[] = new int[1];
		new UIThreadRunnable(display) {
			protected void doRun() {
				wrapper[0] = toExecute.run();
			}
		}.run();
		return wrapper[0];
	}

	/**
	 * Executes the {@code toExecute} on the UI thread, and blocks the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @return the boolean result of execution on the UI thread.
	 * @since 1.0
	 */
	public static boolean syncExec(final UIThreadRunnable.BoolResult toExecute) {
		return syncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the display thread, and blocks the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 * @return the boolean result of execution on the UI thread.
	 */
	public static boolean syncExec(Display display, final UIThreadRunnable.BoolResult toExecute) {
		final boolean wrapper[] = new boolean[1];
		new UIThreadRunnable(display) {
			protected void doRun() {
				wrapper[0] = toExecute.run();
			}
		}.run();
		return wrapper[0];
	}

	/**
	 * Executes the {@code toExecute} on the UI thread, and blocks the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @return the {@link String} result of execution on the UI thread.
	 * @since 1.0
	 */
	public static String syncExec(final UIThreadRunnable.StringResult toExecute) {
		return syncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the display thread, and blocks the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 * @return the {@link String} result of execution on the UI thread.
	 */
	public static String syncExec(Display display, final UIThreadRunnable.StringResult toExecute) {
		final String wrapper[] = new String[1];
		new UIThreadRunnable(display) {
			protected void doRun() {
				wrapper[0] = toExecute.run();
			}
		}.run();
		return wrapper[0];
	}

	/**
	 * Executes the {@code toExecute} on the UI thread, and blocks the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @return the widget result of execution on the UI thread.
	 * @since 1.0
	 */
	public static Widget syncExec(final UIThreadRunnable.WidgetResult toExecute) {
		return syncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the display thread, and blocks the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 * @return the widget result of execution on the UI thread.
	 */
	public static Widget syncExec(Display display, final UIThreadRunnable.WidgetResult toExecute) {
		final Widget wrapper[] = new Widget[1];
		new UIThreadRunnable(display) {
			protected void doRun() {
				wrapper[0] = toExecute.run();
			}
		}.run();
		return wrapper[0];
	}

	/**
	 * Executes the {@code toExecute} on the UI thread, and blocks the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @return the list result of execution on the UI thread.
	 * @since 1.0
	 */
	public static List syncExec(final UIThreadRunnable.ListResult toExecute) {
		return syncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the display thread, and blocks the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 * @return the widget result of execution on the UI thread.
	 */
	public static List syncExec(Display display, final UIThreadRunnable.ListResult toExecute) {
		final List wrapper[] = new List[1];
		new UIThreadRunnable(display) {
			protected void doRun() {
				wrapper[0] = toExecute.run();
			}
		}.run();
		return wrapper[0];
	}

	/**
	 * Executes the {@code toExecute} on the UI thread, and blocks the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @since 1.0
	 */
	public static void syncExec(final UIThreadRunnable.VoidResult toExecute) {
		syncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the display thread, and blocks the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 */
	public static void syncExec(Display display, final UIThreadRunnable.VoidResult toExecute) {
		new UIThreadRunnable(display) {
			protected void doRun() {
				toExecute.run();
			}
		}.run();
	}

	/**
	 * Executes the {@code toExecute} on the UI thread asynchronously, and does not block the calling thread.
	 * 
	 * @param toExecute the runnable to execute.
	 * @since 1.0
	 */
	public static void asyncExec(final UIThreadRunnable.VoidResult toExecute) {
		asyncExec(SWTUtils.display(), toExecute);
	}

	/**
	 * Executes the {@code toExecute} on the UI thread asynchronously, and does not block the calling thread.
	 * 
	 * @param display the display on which toExecute must be executed.
	 * @param toExecute the runnable to execute.
	 */
	public static void asyncExec(Display display, final UIThreadRunnable.VoidResult toExecute) {
		new UIThreadRunnable(display, true) {
			protected void doRun() {
				toExecute.run();
			}
		}.run();
	}
}
