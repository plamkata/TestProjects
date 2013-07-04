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
package net.sf.swtbot.widgets;

import java.util.Iterator;
import java.util.List;

import net.sf.swtbot.SWTBot;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.matcher.AllMatcher;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.MnemonicTextMatcher;
import net.sf.swtbot.utils.ClassUtils;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * Finds controls based on labels next to (or before) the controls. Can also find controls inside groups with the
 * specified label. This works by finding a widget with the specified text using {@link MnemonicTextMatcher}, and
 * finding a control of a given type after this widget.
 * <p>
 * For e.g. To find a {@link Text} control with the label "Username:", this object will find the label "Username:" and
 * find a {@link Text} control after the "Username:" label.
 * </p>
 * 
 * @see #nextWidgetType()
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: AbstractLabelSWTBot.java 809 2008-06-29 04:05:10Z kpadegaonkar $
 */
public abstract class AbstractLabelSWTBot extends AbstractSWTBot {

	/**
	 * Constructs a new instance with the given widget.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public AbstractLabelSWTBot(Widget w) throws WidgetNotFoundException {
		super(w);
	}

	/**
	 * Constructs a new instance with the given information.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public AbstractLabelSWTBot(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * This constructor calls {@link #findControls(IMatcher)} to find all controls matching {@link #matcher()}, in case
	 * a widget is not found, the constructor retries until a timeout occurs. The first such control is set into
	 * {@link #widget} the other controls, if any that match {@link #matcher()} are held in the {@link #similarWidgets}
	 * list.
	 * 
	 * @see SWTBot#waitUntil(net.sf.swtbot.wait.ICondition)
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @param index the index of the control in case multiple controls are found.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public AbstractLabelSWTBot(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}

	/**
	 * Gets the new widget.
	 * 
	 * @return the next widget after the label that this instance should hold
	 */
	protected abstract Class nextWidgetType();

	protected Widget findWidget(int index) throws WidgetNotFoundException {
		return getNextWidget(super.findWidget(index));
	}

	/**
	 * Attempts to get the next widget after the passed in widget.
	 * 
	 * @param widget find a widget of type {@link #nextWidgetType()} after the widget passed into the method.
	 * @return the next widget after flattening the UI hierarchy, this is useful to find the control next to a label in
	 *         the widget hierarchy.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @see #nextWidgetType()
	 */
	protected Widget getNextWidget(Widget widget) throws WidgetNotFoundException {
		if (log.isTraceEnabled())
			log.trace("Finding next widget after " + SWTUtils.toString(widget) + " that is of type "
					+ ClassUtils.simpleClassName(nextWidgetType()));

		Class nextWidget = nextWidgetType();
		if (!nextWidget.isInstance(widget)) {
			List allControls = findControls(new AllMatcher());
			int indexOf = allControls.indexOf(widget);
			for (Iterator iterator = allControls.listIterator(indexOf); iterator.hasNext();) {
				Object next = iterator.next();
				if (nextWidget.isInstance(next)) {
					if (log.isTraceEnabled())
						log.trace("Found widget " + SWTUtils.toString(display, next));
					return (Widget) next;
				}
			}
		}
		throw new WidgetNotFoundException("Could not find next widget of type " + ClassUtils.simpleClassName(nextWidget) + " after "
				+ SWTUtils.toString(widget));
	}

	protected IMatcher getMatcher() {
		return new MnemonicTextMatcher(text);
	}

	/**
	 * Click on the table at given coordinates
	 * 
	 * @param x the x co-ordinate of the click
	 * @param y the y co-ordinate of the click
	 * @since 1.2
	 */
	protected void clickXY(int x, int y) {
		if (log.isDebugEnabled())
			log.debug("Clicking on " + SWTUtils.getText(widget));
		notify(SWT.MouseEnter, createEvent());
		notify(SWT.MouseMove, createEvent());
		notify(SWT.Activate, createEvent());
		notify(SWT.FocusIn, createEvent());
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notify(SWT.MouseUp, createEvent());
		notify(SWT.Selection);
		notify(SWT.MouseHover, createEvent());
		notify(SWT.MouseMove, createEvent());
		notify(SWT.MouseExit, createEvent());
		notify(SWT.Deactivate, createEvent());
		notify(SWT.FocusOut, createEvent());
		if (log.isDebugEnabled())
			log.debug("Clicked on " + SWTUtils.getText(widget));
	}

	/**
	 * Double-click on the table at given coordinates
	 * 
	 * @param x the x co-ordinate of the click
	 * @param y the y co-ordinate of the click
	 * @since 1.2
	 */
	protected void doubleClickXY(int x, int y) {
		if (log.isDebugEnabled())
			log.debug("Double-clicking on " + SWTUtils.getText(widget));
		notify(SWT.MouseEnter, createEvent());
		notify(SWT.MouseMove, createEvent());
		notify(SWT.Activate, createEvent());
		notify(SWT.FocusIn, createEvent());
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notify(SWT.MouseUp, createEvent());
		notify(SWT.Selection);
		notify(SWT.MouseDoubleClick, createMouseEvent(x, y, 1, SWT.BUTTON1, 2));
		notify(SWT.MouseHover, createEvent());
		notify(SWT.MouseMove, createEvent());
		notify(SWT.MouseExit, createEvent());
		notify(SWT.Deactivate, createEvent());
		notify(SWT.FocusOut, createEvent());
		if (log.isDebugEnabled())
			log.debug("Double-clicked on " + SWTUtils.getText(widget));
	}

}
