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
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotRadio.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotRadio extends AbstractSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotRadio(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs an instance of this with the given widget.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotRadio(Button w) throws WidgetNotFoundException {
		super(w);
		Assert.isTrue(SWTUtils.hasStyle(w, SWT.RADIO), "Expecting a radio.");
	}

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.radioButtonMatcher(text);
	}

	/**
	 * Selects the radio button.
	 */
	public void click() {
		if (isSelected()) {
			if (log.isDebugEnabled())
				log.debug("Widget " + SWTUtils.toString(widget) + " is already selected, not clicking again.");
			return;
		}
		checkEnabled();
		if (log.isDebugEnabled())
			log.debug("Clicking on " + SWTUtils.getText(widget));
		asyncExec(new VoidResult() {
			public void run() {
				deselectOtherRadioButtons();
				if (log.isDebugEnabled())
					log.debug("Clicking on " + widget);
				((Button) widget).setSelection(true);
			}

			/**
			 * @see "http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet224.java?view=co"
			 */
			private void deselectOtherRadioButtons() {
				Button b = (Button) widget;
				if (hasStyle(b.getParent(), SWT.NO_RADIO_GROUP))
					return;
				Widget[] siblings = SWTUtils.siblings(widget);
				for (int i = 0; i < siblings.length; i++) {
					Widget widget = siblings[i];
					if ((widget instanceof Button) && hasStyle(widget, SWT.RADIO))
						((Button) widget).setSelection(false);
				}
			}
		});
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
		if (log.isDebugEnabled())
			log.debug("Clicked on " + SWTUtils.getText(widget));
	}

	/**
	 * Checks if the item is selected.
	 * 
	 * @return <code>true</code> if the radio button is selected. Otherwise <code>false</code>.
	 */
	public boolean isSelected() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return ((Button) widget).getSelection();
			}
		});
	}
}
