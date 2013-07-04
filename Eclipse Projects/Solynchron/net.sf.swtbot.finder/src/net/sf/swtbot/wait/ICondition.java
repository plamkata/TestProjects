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
package net.sf.swtbot.wait;

import net.sf.swtbot.SWTBot;

/**
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: ICondition.java 811 2008-06-29 04:33:16Z kpadegaonkar $
 * @since 1.2
 */
public interface ICondition {

	/**
	 * Tests if the condition has been met.
	 * 
	 * @return <code>true</code> if the condition is satisfied, <code>false</code> otherwise.
	 * @throws Exception if the test encounters an error while processing the check.
	 */
	boolean test() throws Exception;

	/**
	 * Initializes the condition with the given {@link SWTBot} instance. This should never be <code>null</code>.
	 * 
	 * @param bot the SWTBot instance that this instance may use to evaluate the test.
	 */
	void init(SWTBot bot);

	/**
	 * Gets the failure message when a test fails (returns <code>false</code>).
	 * 
	 * @return the failure message to show in case the test fails.
	 */
	String getFailureMessage();
}
