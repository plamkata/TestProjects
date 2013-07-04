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

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.StringResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotCombo.java 822 2008-07-03 07:51:16Z kpadegaonkar $
 */
public class SWTBotCombo extends AbstractLabelSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotCombo(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs an instance of this using the given finder and text to search for.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @param index the index of the control in case multiple controls are found.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotCombo(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}
	
	/**
	 * Constructs an instance of this with the given combo box.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 1.0
	 */
	public SWTBotCombo(Combo w) throws WidgetNotFoundException {
		super(w);
	}
	
	protected Class nextWidgetType() {
		return Combo.class;
	}

	/**
	 * Set the selection to the specified text.
	 * 
	 * @param text the text to set into the combo.
	 */
	public void setSelection(final String text) {
		if (log.isDebugEnabled())
			log.debug("Setting selection on " + SWTUtils.getText(widget) + " to " + text);
		checkEnabled();
		_setSelection(text);
		notify(SWT.Selection);
		if (log.isDebugEnabled())
			log.debug("Set selection on " + SWTUtils.getText(widget) + " to " + text);
	}

	/**
	 * Sets the selection to the given text.
	 * 
	 * @param text The text to select.
	 */
	private void _setSelection(final String text) {
		final int indexOf = syncExec(new IntResult() {
			public int run() {
				String[] items = getCombo().getItems();
				return Arrays.asList(items).indexOf(text);
			}
		});
		if (indexOf == -1)
			throw new RuntimeException("Item `" + text + "' not found in combo box.");
		asyncExec(new VoidResult() {
			public void run() {
				getCombo().select(indexOf);
			}
		});
	}

	/**
	 * Gets the combo widget.
	 * 
	 * @return the {@link Combo}.
	 */
	private Combo getCombo() {
		return (Combo) widget;
	}

	/**
	 * Attempts to select the current item.
	 * 
	 * @return the current selection in the combo box.
	 */
	public String selection() {
		return syncExec(new StringResult() {
			public String run() {
				return getCombo().getItem(getCombo().getSelectionIndex());
			}
		});
	}

	/**
	 * Sets the selection to the given index.
	 * 
	 * @return the zero based index of the current selection.
	 */
	public int selectionIndex() {
		return syncExec(new IntResult() {
			public int run() {
				return getCombo().getSelectionIndex();
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
				getCombo().select(index);
			}
		});
	}

	/**
	 * Gets the item count in the combo box.
	 * 
	 * @return the number of items in the combo box.
	 */
	public int itemCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getCombo().getItemCount();
			}
		});
	}

	/**
	 * Returns an array of <code>String</code>s which are the items in the receiver's list.
	 * 
	 * @return the items in the receiver's list
	 * @since 1.0
	 */
	public String[] items() {
		return (String[]) syncExec(new ObjectResult() {
			public Object run() {
				return getCombo().getItems();
			}
		});
	}

	/**
	 * Sets the text of the combo box.
	 * 
	 * @param text the text to set.
	 * @since 1.0
	 */
	public void setText(final String text) {
		if (log.isDebugEnabled())
			log.debug("Setting text on " + SWTUtils.getText(widget) + " to " + text);
		checkEnabled();

		if (hasStyle(widget, SWT.READ_ONLY))
			throw new RuntimeException("This combo box is read-only.");

		asyncExec(new VoidResult() {
			public void run() {
				getCombo().setText(text);
			}
		});
		notify(SWT.Modify);
		if (log.isDebugEnabled())
			log.debug("Set text on " + SWTUtils.getText(widget) + " to " + text);
	}

}