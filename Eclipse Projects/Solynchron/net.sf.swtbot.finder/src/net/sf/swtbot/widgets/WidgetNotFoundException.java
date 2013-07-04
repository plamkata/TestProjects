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

/**
 * An exception that is thrown when a widget can not be found on the given display/shell.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: WidgetNotFoundException.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class WidgetNotFoundException extends Exception {

	/** the serialVersionUID */
	private static final long	serialVersionUID	= -4097219100771019730L;

	/**
	 * Constructs an exception with the given message.
	 * 
	 * @param message the message.
	 */
	public WidgetNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructs this exception with the given message and cause.
	 * 
	 * @param message the message.
	 * @param cause the underlying cause.
	 */
	public WidgetNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
