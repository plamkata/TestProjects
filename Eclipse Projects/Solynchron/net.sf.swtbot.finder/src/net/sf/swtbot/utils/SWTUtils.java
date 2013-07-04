/*******************************************************************************
 * Copyright 2007 SWTBot, http://swtbot.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sf.swtbot.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.swtbot.finder.UIThreadRunnable;
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.utils.internal.NextWidgetFinder;
import net.sf.swtbot.utils.internal.PreviousWidgetFinder;
import net.sf.swtbot.utils.internal.ReflectionInvoker;
import net.sf.swtbot.utils.internal.SiblingFinder;
import net.sf.swtbot.utils.internal.WidgetIndexFinder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTUtils.java 842 2008-07-14 12:32:11Z kpadegaonkar $
 */
public abstract class SWTUtils {

	/**
	 * The display used for the GUI.
	 */
	private static Display	display;

	/**
	 * @param w the widget
	 * @return the siblings of the widget, or an empty array, if there are none.
	 */
	public static Widget[] siblings(final Widget w) {
		if ((w == null) || w.isDisposed())
			return new Widget[] {};
		return (Widget[]) UIThreadRunnable.syncExec(w.getDisplay(), new SiblingFinder(w));
	}

	/**
	 * Gets the index of the given widget in its current container.
	 *
	 * @param w the widget
	 * @return -1 if the the widget is <code>null</code> or if the widget does not have a parent; a number greater than
	 *         or equal to zero indicating the index of the widget among its siblings
	 */
	public static int widgetIndex(final Widget w) {
		if ((w == null) || w.isDisposed())
			return -1;
		return UIThreadRunnable.syncExec(w.getDisplay(), new WidgetIndexFinder(w));
	}

	/**
	 * Gets the previous sibling of the passed in widget.
	 *
	 * @param w the widget
	 * @return the previous sibling of w
	 */
	public static Widget previousWidget(final Widget w) {
		if ((w == null) || w.isDisposed())
			return null;
		return UIThreadRunnable.syncExec(w.getDisplay(), new PreviousWidgetFinder(w));
	}

	/**
	 * Gets the next sibling of this passed in widget.
	 *
	 * @param w the widget.
	 * @return the sibling of the specified widget, or <code>null</code> if it has none.
	 */
	public static Widget nextWidget(Widget w) {
		if ((w == null) || w.isDisposed())
			return null;
		return UIThreadRunnable.syncExec(w.getDisplay(), new NextWidgetFinder(w));
	}

	/**
	 * Gets the text of the given object.
	 *
	 * @param obj the object which should be a widget.
	 * @return the result of invocation of Widget#getText()
	 */
	public static String getText(final Object obj) {
		if ((obj instanceof Widget) && !((Widget) obj).isDisposed()) {
			Widget widget = (Widget) obj;
			String text = UIThreadRunnable.syncExec(widget.getDisplay(), new ReflectionInvoker(obj, "getText"));
			text = text.replaceAll(Text.DELIMITER, "\n");
			return text;
		}
		return "";
	}

	/**
	 * Gets the tooltip text for the given object.
	 *
	 * @param obj the object which should be a widget.
	 * @return the result of invocation of Widget#getToolTipText()
	 * @since 1.0
	 */
	public static String getToolTipText(final Object obj) {
		if ((obj instanceof Widget) && !((Widget) obj).isDisposed()) {
			Widget widget = (Widget) obj;
			return UIThreadRunnable.syncExec(widget.getDisplay(), new ReflectionInvoker(obj, "getToolTipText"));
		}
		return "";
	}

	/**
	 * Converts the given widget to a string.
	 *
	 * @param w the widget.
	 * @return the string representation of the widget.
	 */
	public static String toString(final Widget w) {
		if ((w == null) || w.isDisposed())
			return "";
		return toString(w.getDisplay(), w);
	}

	/**
	 * Convers the display and object to a string.
	 *
	 * @param display the display on which the object should be evaluated.
	 * @param o the object to evaluate.
	 * @return the string representation of the object when evaluated in the display thread.
	 */
	public static String toString(Display display, final Object o) {
		return ClassUtils.simpleClassName(o) + " {" + trimToLength(getText(o), 20) + "}";

	}

	/**
	 * Trims the string to a given length, adds an ellipsis("...") if the string is trimmed.
	 *
	 * @param result The string to limit.
	 * @param maxLength The length to limit it to.
	 * @return The resulting string.
	 */
	private static String trimToLength(String result, int maxLength) {
		if (result.length() > maxLength)
			result = result.substring(0, maxLength) + "...";
		return result;
	}

	/**
	 * Checks if the widget has the given style.
	 *
	 * @param w the widget.
	 * @param style the style.
	 * @return <code>true</code> if the widget has the specified style bit set. Otherwise <code>false</code>.
	 */
	public static boolean hasStyle(final Widget w, final int style) {
		if ((w == null) || w.isDisposed())
			return false;
		return UIThreadRunnable.syncExec(w.getDisplay(), new BoolResult() {
			public boolean run() {
				return (w.getStyle() & style) != 0;
			}
		});
	}

	/**
	 * Sleeps for the given number of milliseconds.
	 *
	 * @param millis the time in milliseconds to sleep.
	 */
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException("Could not sleep", e);
		}
	}

	/**
	 * Gets all the thread in the VM.
	 *
	 * @return all the threads in the VM.
	 */
	public static Thread[] allThreads() {
		ThreadGroup threadGroup = primaryThreadGroup();

		Thread[] threads = new Thread[64];
		int enumerate = threadGroup.enumerate(threads, true);

		Thread[] result = new Thread[enumerate];
		System.arraycopy(threads, 0, result, 0, enumerate);

		return result;
	}

	/**
	 * Gets the primary thread group.
	 *
	 * @return the top level thread group.
	 */
	public static ThreadGroup primaryThreadGroup() {
		ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
		while (threadGroup.getParent() != null)
			threadGroup = threadGroup.getParent();
		return threadGroup;
	}

	/**
	 * Caches the display for later use.
	 *
	 * @return the display.
	 */
	public static Display display() {
		if ((display == null) || display.isDisposed()) {
			display = null;
			Thread[] allThreads = allThreads();
			for (int i = 0; i < allThreads.length; i++) {
				Thread thread = allThreads[i];
				Display d = Display.findDisplay(thread);
				if (d != null)
					display = d;
			}
			if (display == null)
				throw new IllegalStateException("Could not find a display");
		}
		return display;
	}

	/**
	 * Checks if the widget text is an empty string.
	 *
	 * @param w the widget
	 * @return <code>true</code> if the widget does not have any text set on it. Othrewise <code>false</code>.
	 */
	// TODO recommend changing the name to isEmptyText since null isn't being check and if getText returned a null an
	// exception would be thrown.
	public static boolean isEmptyOrNullText(Widget w) {
		return getText(w).trim().equals("");
	}

	/**
	 * Invokes the specified methodName on the object, and returns the result, or <code>null</code> if the method
	 * returns void.
	 *
	 * @param object the object
	 * @param methodName the method name
	 * @return the result of invoking the method on the object
	 * @throws NoSuchMethodException if the method methodName does not exist.
	 * @throws IllegalAccessException if the java access control does not allow invocation.
	 * @throws InvocationTargetException if the method methodName throws an exception.
	 * @see Method#invoke(Object, Object[])
	 * @since 1.0
	 */
	public static Object invokeMethod(final Object object, String methodName) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		final Method method = object.getClass().getMethod(methodName, new Class[0]);
		Widget widget = null;
		final Object result;
		if (object instanceof Widget) {
			widget = (Widget) object;
			result = UIThreadRunnable.syncExec(widget.getDisplay(), new ObjectResult() {
				public Object run() {
					try {
						return method.invoke(object, new Object[0]);
					} catch (Exception niceTry) {
					}
					return null;
				}
			});
		} else
			result = method.invoke(object, new Object[0]);

		return result;
	}

	/**
	 * This captures a screen shot and saves it to the given file.
	 *
	 * @param fileName the filename to save screenshot to.
	 * @return <code>true</code> if the screenshot was created and saved, <code>false</code> otherwise.
	 * @since 1.0
	 */
	public static boolean captureScreenshot(final String fileName) {
		return UIThreadRunnable.syncExec(new BoolResult() {
			public boolean run() {
				return captureScreenshotInternal(fileName);
			}
		});
	}

	/**
	 * Captures a screen shot. Used internally.
	 * <p>
	 * NOTE: This method is not thread safe. Clients must ensure that they do invoke this from a UI thread.
	 * </p>
	 *
	 * @param fileName the filename to save screenshot to.
	 * @return <code>true</code> if the screenshot was created and saved, <code>false</code> otherwise.
	 * @since 1.1
	 */
	protected static boolean captureScreenshotInternal(final String fileName) {
		try {
			GC gc = new GC(display);
			Rectangle bounds = display.getBounds();
			int width = bounds.width;
			int height = bounds.height;

			Image image = new Image(display, width, height);
			gc.copyArea(image, 0, 0);
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { image.getImageData() };
			imageLoader.save(fileName, SWT.IMAGE_PNG);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
