package net.sf.swtbot.finder;

import java.util.List;

import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.resolvers.IChildrenResolver;
import net.sf.swtbot.resolvers.IParentResolver;

import org.eclipse.swt.widgets.Widget;

/**
 * Finds controls matching a particular matcher in the given parent widget.
 * 
 * @author Cedric Chabanois &lt;cchabanois [at] no-log [dot] org&gt;
 * @version $Id: ChildrenControlFinder.java 772 2008-06-22 04:19:08Z kpadegaonkar $
 * @since 1.0
 */
public class ChildrenControlFinder extends ControlFinder {
	/**
	 * The parent widget to begin searching for children.
	 * 
	 * @since 1.1
	 */
	protected final Widget	parentWidget;

	/**
	 * Constructs a child control finder widget using the given parent widget as its starting point.
	 * 
	 * @param parentWidget the parent widget in which controls should be found.
	 */
	public ChildrenControlFinder(Widget parentWidget) {
		super();
		this.parentWidget = parentWidget;
	}

	/**
	 * 
	 * Constructs the child control finder with the given parent widget as it's starting point and the set resolvers.
	 * 
	 * @param parentWidget the parent widget in which controls should be found.
	 * @param childrenResolver the resolver used to resolve children of a control.
	 * @param parentResolver the resolver used to resolve parent of a control.
	 */
	public ChildrenControlFinder(Widget parentWidget, IChildrenResolver childrenResolver, IParentResolver parentResolver) {
		super(childrenResolver, parentResolver);
		this.parentWidget = parentWidget;
	}

	/**
	 * Attempts to find the controls using the given matcher starting with the given parent widget. This will search
	 * recursively.
	 * 
	 * @param matcher the matcher used to find controls in the {@link #parentWidget}.
	 * @return all controls in the parent widget that the matcher matches.
	 */
	public List findControls(IMatcher matcher) {
		return findControls(parentWidget, matcher, true);
	}

}