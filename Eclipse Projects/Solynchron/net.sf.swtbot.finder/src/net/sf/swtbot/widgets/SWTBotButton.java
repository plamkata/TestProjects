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

import net.sf.swtbot.SWTBot;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

/**
 * This represents a {@link Button} widget.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotButton.java 809 2008-06-29 04:05:10Z kpadegaonkar $
 */
public class SWTBotButton extends AbstractSWTBot {

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
	public SWTBotButton(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}

	/**
	 * Click on the button.
	 */
	public void click() {
		if (log.isDebugEnabled())
			log.debug("Clicking on " + SWTUtils.getText(widget));
		checkEnabled();
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

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.buttonMatcher(text);
	}

}
