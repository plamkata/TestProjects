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

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotCheckBox.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotCheckBox extends AbstractSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param checkBoxText the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotCheckBox(Finder finder, String checkBoxText) throws WidgetNotFoundException {
		super(finder, checkBoxText);
	}

	/**
	 * Constructs an instance of this object with the given button (Checkbox)
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 * @since 1.0
	 */
	public SWTBotCheckBox(Button w) throws WidgetNotFoundException {
		super(w);
		Assert.isTrue(SWTUtils.hasStyle(w, SWT.CHECK), "Expecting a checkbox.");
	}

	/**
	 * Click on the checkbox, toggle it.
	 */
	public void click() {
		if (log.isDebugEnabled())
			log.debug("Clicking on " + SWTUtils.getText(widget));
		toggle();
		if (log.isDebugEnabled())
			log.debug("Clicked on " + SWTUtils.getText(widget));
	}

	/**
	 * Deselect the checkbox.
	 */
	public void deselect() {
		if (log.isDebugEnabled())
			log.debug("Deselecting " + SWTUtils.toString(widget));
		checkEnabled();
		if (!isChecked()) {
			if (log.isDebugEnabled())
				log.debug("Widget " + SWTUtils.toString(widget) + " already deselected, not deselecting again.");
			return;
		}
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled())
					log.debug("Deselecting " + widget);
				((Button) widget).setSelection(false);
			}
		});
		notifyListeners();
	}

	/**
	 * Select the checkbox.
	 */
	public void select() {
		if (log.isDebugEnabled())
			log.debug("Selecting " + SWTUtils.toString(widget));
		checkEnabled();
		if (isChecked()) {
			if (log.isDebugEnabled())
				log.debug("Widget " + SWTUtils.toString(widget) + " already selected, not selecting again.");
			return;
		}
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled())
					log.debug("Selecting " + widget);
				((Button) widget).setSelection(true);
			}
		});
		notifyListeners();
	}

	/**
	 * Toggle the checkbox.
	 */
	protected void toggle() {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				Button b = (Button) widget;
				if (log.isDebugEnabled())
					log.debug("Toggling state on " + b + ". Setting state to " + (!b.getSelection() ? "selected" : "unselected"));
				b.setSelection(!b.getSelection());
			}
		});
		notifyListeners();
	}

	/**
	 * notify listeners about checkbox state change.
	 */
	protected void notifyListeners() {
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown);
		notify(SWT.MouseUp);
		notify(SWT.Selection);
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
	}

	/**
	 * Gets if the checkbox button is checked.
	 * 
	 * @return <code>true</code> if the checkbox is checked. Otherwise <code>false</code>.
	 */
	public boolean isChecked() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return ((Button) widget).getSelection();
			}
		});
	}

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.checkBoxMatcher(text);
	}
}
