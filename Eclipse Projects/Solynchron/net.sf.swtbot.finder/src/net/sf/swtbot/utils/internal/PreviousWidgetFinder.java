package net.sf.swtbot.utils.internal;

import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;

import org.eclipse.swt.widgets.Widget;

/**
 * This object finds the previous widget.
 * <p>
 * <b>NOTE: This finds all the siblings and finds the index of the previous widget among the siblings. This does not use
 * SWTUtils to find siblings and index for the widget that this instance wraps for performance reasons.</b>
 * </p>
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: PreviousWidgetFinder.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 * @see NextWidgetFinder
 * @see WidgetIndexFinder
 */
public final class PreviousWidgetFinder implements WidgetResult {
	/**
	 * The widget to use.
	 */
	private final Widget	w;

	/**
	 * Constructs the previous widget finder.
	 * 
	 * @param w the widget
	 */
	public PreviousWidgetFinder(Widget w) {
		this.w = w;
	}

	/**
	 * Runs the processing to find the previous widget.
	 * 
	 * @see net.sf.swtbot.finder.UIThreadRunnable.WidgetResult#run()
	 * @return The widget found or <code>null</code> if not found.
	 */
	public Widget run() {
		Widget[] siblings = (Widget[]) new SiblingFinder(w).run();
		int myIndex = new WidgetIndexFinder(w).run();
		return myIndex > 0 ? siblings[myIndex - 1] : null;
	}
}
