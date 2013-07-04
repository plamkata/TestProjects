package net.sf.swtbot.utils.internal;

import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Finds the siblings of a widget.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public final class SiblingFinder implements ObjectResult {
	/**
	 * The widget to use.
	 */
	private final Widget	w;

	/**
	 * Constructs the sibling finder with the given widget.
	 * 
	 * @param w the widget
	 */
	public SiblingFinder(Widget w) {
		this.w = w;
	}

	/**
	 * Runs the process of finding the siblings.
	 * 
	 * @see net.sf.swtbot.finder.UIThreadRunnable.ObjectResult#run()
	 * @return The object found.
	 */
	public Object run() {
		Widget[] siblings = new Widget[] {};
		if (isControl(w))
			siblings = children(((Control) w).getParent());
		else if (isTabItem(w))
			siblings = ((TabItem) w).getParent().getItems();
		return siblings;
	}

	/**
	 * Gets the children widgets starting with the given composite.
	 * 
	 * @param parent The parent composite.
	 * @return The list of child widgets or an empty list if none.
	 */
	private Widget[] children(Composite parent) {
		if (parent == null)
			return new Widget[] {};
		Control[] children = parent.getChildren();
		return (children == null) ? new Widget[] {} : children;
	}

	/**
	 * Gets if this passed in widget is a control.
	 * 
	 * @param w The widget.
	 * @return <code>true</code> if it is a control. Otherwise <code>false</code>.
	 */
	private boolean isControl(Widget w) {
		return w instanceof Control;
	}

	/**
	 * Gets if this is a tab item widget.
	 * 
	 * @param w The widget.
	 * @return <code>true</code> if it is a tab item. Otherwise <code>false</code>.
	 */
	private boolean isTabItem(Widget w) {
		return w instanceof TabItem;
	}
}
