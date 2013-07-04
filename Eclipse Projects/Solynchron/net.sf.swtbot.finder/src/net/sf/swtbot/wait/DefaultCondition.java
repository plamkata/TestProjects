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
 * This is an abstract implementation of the condition interface to simplify the implementing classes.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: DefaultCondition.java 830 2008-07-05 20:26:55Z kpadegaonkar $
 */
public abstract class DefaultCondition implements ICondition {

	/** the SWTBot instance that this instance may use to evaluate the test. */
	protected SWTBot	bot;

	/**
	 * Initializes the condition with the given {@link SWTBot}.
	 * 
	 * @see net.sf.swtbot.wait.ICondition#init(net.sf.swtbot.SWTBot)
	 * @param bot The bot to use. This should never be <code>null</code>.
	 */
	public void init(SWTBot bot) {
		this.bot = bot;
	}

}
