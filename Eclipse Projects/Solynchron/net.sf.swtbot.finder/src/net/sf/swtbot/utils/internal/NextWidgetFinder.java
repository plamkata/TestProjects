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
package net.sf.swtbot.utils.internal;

import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;

import org.eclipse.swt.widgets.Widget;

/**
 * This object is used to find the next widget.
 * <p>
 * <b>NOTE: This finds all the siblings and finds the index of the next widget among the siblings. This does not use
 * SWTUtils to find siblings and index for the widget that this instance wraps for performance reasons.</b>
 * </p>
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: NextWidgetFinder.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 * @see PreviousWidgetFinder
 * @see WidgetIndexFinder
 */
public class NextWidgetFinder implements WidgetResult {

	/**
	 * The widget to use.
	 */
	private final Widget	w;

	/**
	 * Constructs the next widget finder.
	 * 
	 * @param w the widget
	 */
	public NextWidgetFinder(Widget w) {
		this.w = w;
	}

	/**
	 * Runs the processing to find the next widget.
	 * 
	 * @see net.sf.swtbot.finder.UIThreadRunnable.WidgetResult#run()
	 * @return The next widget or <code>null</code> if not found.
	 */
	public Widget run() {
		Widget[] siblings = (Widget[]) new SiblingFinder(w).run();
		int widgetIndex = new WidgetIndexFinder(w).run();
		if (widgetIndex < siblings.length - 1)
			return siblings[widgetIndex + 1];
		return null;
	}
}
