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

import java.util.Calendar;
import java.util.Date;

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotDateTime.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotDateTime extends AbstractLabelSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotDateTime(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs an instance of this object with the given widget.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotDateTime(Widget w) throws WidgetNotFoundException {
		super(w);
	}

	protected Class nextWidgetType() {
		return DateTime.class;
	}

	/**
	 * Gets the date of this widget.
	 * 
	 * @return the date/time set into the widget.
	 */
	public Date getDate() {
		return (Date) syncExec(new ObjectResult() {
			public Object run() {
				int year = getControl().getYear();
				int month = getControl().getMonth();
				int day = getControl().getDay();

				int hours = getControl().getHours();
				int minutes = getControl().getMinutes();
				int seconds = getControl().getSeconds();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(0);
				calendar.set(year, month, day, hours, minutes, seconds);
				return calendar.getTime();
			}

		});
	}

	/**
	 * Gets the control.
	 * 
	 * @return The {@link DateTime}.
	 */
	private DateTime getControl() {
		return (DateTime) widget;
	}

	/**
	 * Sets the date.
	 * 
	 * @param toSet the date to set into the control.
	 */
	public void setDate(final Date toSet) {
		if (log.isDebugEnabled())
			log.debug("Setting date on control: " + SWTUtils.toString(widget) + " to " + toSet);
		checkEnabled();
		syncExec(new VoidResult() {
			public void run() {
				getControl().setYear(toSet.getYear() + 1900);
				getControl().setMonth(toSet.getMonth());
				getControl().setDay(toSet.getDate());

				getControl().setHours(toSet.getHours());
				getControl().setMinutes(toSet.getMinutes());
				getControl().setSeconds(toSet.getSeconds());
			}
		});
		notify(SWT.Selection);
	}
}
