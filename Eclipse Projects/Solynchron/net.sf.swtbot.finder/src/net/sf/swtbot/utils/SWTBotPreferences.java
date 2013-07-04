/*******************************************************************************
 *  Copyright 2007 SWTBot, http://swtbot.org/
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ******************************************************************************/
package net.sf.swtbot.utils;

/**
 * Holds the preferences for the SWT Bot.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotPreferences.java 811 2008-06-29 04:33:16Z kpadegaonkar $
 * @since 1.1
 */
public class SWTBotPreferences {

	/**
	 * Gets the timeout. To set use the system property {@code net.sf.swtbot.search.timeout}.
	 * 
	 * @return the timeout value.
	 */
	public static int getTimeout() {
		String timeoutValue = System.getProperty("net.sf.swtbot.search.timeout");
		Integer timeout = Integer.valueOf(timeoutValue);
		return timeout.intValue();
	}

	/**
	 * Gets the recorder file name. To set use the system property {@code net.sf.swtbot.recorder.file.name}.
	 * 
	 * @return the recorder file name.
	 */
	public static String recorderFileName() {
		return System.getProperty("net.sf.swtbot.recorder.file.name");
	}

	/**
	 * Gets the playback delay. This can be set using the system property {@code net.sf.swtbot.playback.delay}.
	 * 
	 * @return the playback delay.
	 */
	public static long playbackDelay() {
		String playbackDelay = System.getProperty("net.sf.swtbot.playback.delay");
		try {
			Long timeout = Long.valueOf(playbackDelay);
			return timeout.longValue();
		} catch (Exception e) {
			return 0;
		}
	}
}
