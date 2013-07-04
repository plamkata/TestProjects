/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.test;

import junit.framework.TestResult;
import net.sf.swtbot.SWTBotTestCase;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.wait.DefaultCondition;
import net.sf.swtbot.widgets.SWTBotShell;
import net.sf.swtbot.widgets.TimeoutException;
import net.sf.swtbot.widgets.WidgetNotFoundException;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * base test case for rcpforms. It initializes the realm, creates a shell, starts the test in the UI
 * thread and makes sure swt bot is initialized correctly.
 * 
 * @author Marco van Meegen
 */
public abstract class RCPFormBaseTestCase extends SWTBotTestCase
{
    /**
     * SWT thread and shell setup must be initialized statically since SWTBot is created directly in
     * SWTBotTestCase and needs a display
     */
    public static final SWTThreadSetup setup = SWTThreadSetup.getSameThreadInstance();

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        waitForDisplayToAppear(5000); // wait for the display to appear before you do anything
    }

    private void waitForDisplayToAppear(long timeOut) throws TimeoutException, InterruptedException
    {
        long endTime = System.currentTimeMillis() + timeOut;
        while (System.currentTimeMillis() < endTime)
        { // wait until timeout
            try
            {
                Display display = SWTUtils.display();
                if (display != null)
                    return;
            }
            catch (Exception e)
            {
                // did not find a display? no problems, try again
            }
            Thread.sleep(100); // sleep for a while and try again
        }
        throw new TimeoutException("timed out");
    }

    /**
     * overloaded to make sure the default realm is set to the default display, otherwise
     * WritableValue's cannot be created without a realm
     */
    @Override
    public void run(final TestResult result)
    {
        Realm.runWithDefault(SWTObservables.getRealm(getDisplay()), new Runnable()
        {
            public void run()
            {
                RCPFormBaseTestCase.super.run(result);
            }
        });
    }

    protected Shell getShell()
    {
        return setup.shell;
    }

    protected Display getDisplay()
    {
        return setup.display;
    }

    protected Thread getUIThread()
    {
        return setup.uiThread;
    }

    /**
     * activates the given shell and waits until it is active
     * 
     * @param shell shell to activate
     */

    protected void activateShell(Shell shell) throws WidgetNotFoundException, TimeoutException
    {
        final SWTBotShell botShell = new SWTBotShell(shell);
        botShell.activate();
        botShell.setFocus();
        bot.waitUntil(new DefaultCondition()
        {
            public boolean test() throws Exception
            {
                return botShell.isActive();
            }

            public String getFailureMessage()
            {
                return "Shell was not activated in activateShell()";
            }
        });
    }
}
