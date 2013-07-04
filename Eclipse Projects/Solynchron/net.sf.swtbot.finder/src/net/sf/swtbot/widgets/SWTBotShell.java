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
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.wait.DefaultCondition;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotShell.java 834 2008-07-11 08:31:44Z kpadegaonkar $
 */
public class SWTBotShell extends AbstractSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param shellText the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotShell(Finder finder, String shellText) throws WidgetNotFoundException {
		super(finder, shellText);
	}

	/**
	 * Constructs an instance of this with the given shell.
	 * 
	 * @param shell the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotShell(Shell shell) throws WidgetNotFoundException {
		super(shell);
	}

	/**
	 * Constructs an instance of this with the given finder and text to search for. The index is used if multiples are
	 * found.
	 * 
	 * @param finder the finder used to find controls.
	 * @param shellText the text on the control.
	 * @param index the index of the shell in case multiple shells have the same text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotShell(Finder finder, String shellText, int index) throws WidgetNotFoundException {
		super(finder, shellText, index);
	}

	protected Widget findWidget(int index) throws WidgetNotFoundException {
		// could have used a matcher, but that would just slow down things
		Shell[] shells = finder.getShells();
		for (int i = 0; i < shells.length; i++) {
			Shell shell = shells[i];
			if (new SWTBotShell(shell).getText().equals(text))
				return shell;
		}
		throw new WidgetNotFoundException("Cound not find shell matching text:" + text);
	}

	/**
	 * Activates the shell.
	 * 
	 * @throws TimeoutException if the shell could not be activated
	 */
	public void activate() throws TimeoutException {
		// FIXME: this seems to be some issue on gtk where shells don't get activated. I'm running this in sync to see
		// if this could be an issue.
		setFocus();
		new SWTBot().waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "Timed out waiting for " + SWTUtils.toString(widget) + " to get activated";
			}

			public boolean test() throws Exception {
				syncExec(new VoidResult() {
					public void run() {
						getShell().forceActive();
						getShell().forceFocus();
					}
				});
				return isActive();
			}
		});
		notify(SWT.Activate);
	}

	/**
	 * Gets the shell.
	 * 
	 * @return the {@link #widget} casted into a {@link Shell}.
	 */
	protected Shell getShell() {
		return (Shell) widget;
	}

	/**
	 * Closes the shell
	 * 
	 * @throws TimeoutException if the shell does not close.
	 */
	public void close() throws TimeoutException {
		notify(SWT.Close);
		asyncExec(new VoidResult() {
			public void run() {
				getShell().close();
			}
		});
		new SWTBot().waitUntil(new DefaultCondition() {
			public boolean test() throws Exception {
				return !isOpen();
			}

			public String getFailureMessage() {
				return "Timed out waiting for " + SWTUtils.toString(widget) + " to close.";
			}
		});
	}

	/**
	 * Checks if the shell is open.
	 * 
	 * @return <code>true</code> if the shell is visible, <code>false</code> otherwise.
	 */
	public boolean isOpen() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return !getShell().isDisposed() && getShell().isVisible();
			}
		});
	}

	protected IMatcher getMatcher() {
		return null;
	}

	/**
	 * Checks if the shell is active.
	 * 
	 * @return <code>true</code> if the shell is active, <code>false</code> otherwise.
	 */
	public boolean isActive() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return display.getActiveShell() == widget;
			}
		});
	}

}
