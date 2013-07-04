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
 * This is an exception that is thrown when a timeout occurs waiting for something (e.g. a condition) to complete.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: TimeoutException.java 848 2008-07-21 14:26:11Z kpadegaonkar $
 */
public class TimeoutException extends Exception {

	/** the serialVersionUID */
	private static final long	serialVersionUID	= -4097219100771019730L;

	/**
	 * Constructs the exception with the given message.
	 *
	 * @param message the message.
	 */
	public TimeoutException(String message) {
		super(message);
	}
}