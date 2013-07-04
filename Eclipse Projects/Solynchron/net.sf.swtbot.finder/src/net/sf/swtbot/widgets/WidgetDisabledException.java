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

import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.widgets.Widget;

/**
 * Thrown when you try to perform operations with controls that are not enabled.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: WidgetDisabledException.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 * @since 1.0
 */
public class WidgetDisabledException extends RuntimeException {

	/**
	 * The serialization version identifier.
	 */
	private static final long	serialVersionUID	= -653548903006602705L;

	/**
	 * Constructs the exception with the given message.
	 * 
	 * @param widget the widget that is disabled
	 */
	public WidgetDisabledException(Widget widget) {
		super("Widget " + SWTUtils.toString(widget) + " is disabled.");
	}

}
