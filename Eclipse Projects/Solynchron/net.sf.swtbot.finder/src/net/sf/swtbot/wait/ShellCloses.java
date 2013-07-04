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
package net.sf.swtbot.wait;

import net.sf.swtbot.finder.UIThreadRunnable;
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.widgets.SWTBotShell;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Shell;

/**
 * A condition that waits until the specified shell is disposed/visible.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: ShellCloses.java 830 2008-07-05 20:26:55Z kpadegaonkar $
 * @since 1.2
 */
class ShellCloses extends DefaultCondition {

	private final SWTBotShell	shell;

	/**
	 * Creates a condition that evaluates to false until the shell is disposed or visible.
	 * 
	 * @param shell the shell to be monitored.
	 */
	ShellCloses(SWTBotShell shell) {
		Assert.isNotNull(shell, "The shell was null");
		this.shell = shell;
	}

	public String getFailureMessage() {
		return "The shell " + shell + " did not close.";
	}

	public boolean test() throws Exception {
		return UIThreadRunnable.syncExec(new BoolResult() {
			public boolean run() {
				return shell.widget.isDisposed() || !((Shell) shell.widget).isVisible();
			}
		});
	}

}
