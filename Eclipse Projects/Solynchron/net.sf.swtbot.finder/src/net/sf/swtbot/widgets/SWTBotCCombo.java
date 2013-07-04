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

import java.util.Arrays;

import net.sf.swtbot.SWTBot;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.StringResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;

/**
 * This represents a {@link CCombo} widget.
 * 
 * @author Cedric Chabanois &lt;cchabanois [at] no-log [dot] org&gt;
 * @version $Id: SWTBotCCombo.java 822 2008-07-03 07:51:16Z kpadegaonkar $
 * @since 1.0
 */
public class SWTBotCCombo extends AbstractLabelSWTBot {

	/**
	 * Constructs an isntance of this with the given finder and the text to search for.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotCCombo(Finder finder, String text) throws WidgetNotFoundException {
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
	 */
	public SWTBotCCombo(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}

	/**
	 * Constructs an instance of this with the given widget.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotCCombo(CCombo w) throws WidgetNotFoundException {
		super(w);
	}

	protected Class nextWidgetType() {
		return CCombo.class;
	}
	
	/**
	 * Sets the text in the cCombo box.
	 * 
	 * @param text the text to set.
	 */
	public void setText(final String text) {
		if (log.isDebugEnabled())
			log.debug("Setting text on widget " + SWTUtils.getText(widget) + " to " + text);
		checkEnabled();
		if (hasStyle(widget, SWT.READ_ONLY))
			throw new RuntimeException("This combo box is read-only.");
		asyncExec(new VoidResult() {
			public void run() {
				getCCombo().setText(text);
			}
		});
		notify(SWT.Modify);
	}

	/**
	 * Returns the maximum number of characters that the receiver's text field is capable of holding. If this has not
	 * been changed by <code>setTextLimit()</code>, it will be the constant <code>Combo.LIMIT</code>.
	 * 
	 * @return the text limit
	 */
	public int textLimit() {
		return syncExec(new IntResult() {
			public int run() {
				return getCCombo().getTextLimit();
			}
		});
	}

	/**
	 * Set the selection to the specified text.
	 * 
	 * @param text the text to set into the combo.
	 */
	public void setSelection(final String text) {
		if (log.isDebugEnabled())
			log.debug("Setting selection on " + SWTUtils.getText(widget) + " to " + text);
		_setSelection(text);
		notify(SWT.Selection);
		if (log.isDebugEnabled())
			log.debug("Set selection on " + SWTUtils.getText(widget) + " to " + text);
	}

	/**
	 * Sets the selection to the given text.
	 * 
	 * @param text The text to use.
	 */
	private void _setSelection(final String text) {
		checkEnabled();
		final int indexOf = syncExec(new IntResult() {
			public int run() {
				String[] items = getCCombo().getItems();
				return Arrays.asList(items).indexOf(text);
			}
		});
		if (indexOf == -1)
			throw new RuntimeException("Item `" + text + "' not found in combo box.");
		asyncExec(new VoidResult() {
			public void run() {
				getCCombo().select(indexOf);
			}
		});
	}

	/**
	 * Gets the {@link CCombo} widget.
	 * 
	 * @return The {@link CCombo}.
	 */
	private CCombo getCCombo() {
		return (CCombo) widget;
	}

	/**
	 * Gets the current selection in the combo.
	 * 
	 * @return the current selection in the combo box or null if no item is selected.
	 */
	public String selection() {
		return syncExec(new StringResult() {
			public String run() {
				int selectionIndex = getCCombo().getSelectionIndex();
				if (selectionIndex == -1)
					return null;
				return getCCombo().getItem(selectionIndex);
			}
		});
	}

	/**
	 * Gets the current selection index.
	 * 
	 * @return the zero based index of the current selection.
	 */
	public int selectionIndex() {
		return syncExec(new IntResult() {
			public int run() {
				return getCCombo().getSelectionIndex();
			}
		});
	}

	/**
	 * Sets the selection to the specified index.
	 * 
	 * @param index the zero based index.
	 */
	public void setSelection(final int index) {
		checkEnabled();
		int itemCount = itemCount();
		if (index > itemCount)
			throw new RuntimeException("The index (" + index + ") is more than the number of items (" + itemCount + ") in the combo.");

		asyncExec(new VoidResult() {
			public void run() {
				getCCombo().select(index);
			}
		});
	}

	/**
	 * Gets the number of items in the combo box.
	 * 
	 * @return the number of items in the combo box.
	 */
	public int itemCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getCCombo().getItemCount();
			}
		});
	}

	/**
	 * Returns an array of <code>String</code>s which are the items in the receiver's list.
	 * 
	 * @return the items in the receiver's list
	 */
	public String[] items() {
		return (String[]) syncExec(new ObjectResult() {
			public Object run() {
				return getCCombo().getItems();
			}
		});
	}

}
