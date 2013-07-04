/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * parts copied from swtbot unit tests
 * 
 * @author Marco van Meegen
 */
public class SWTThreadSetup
{
    private static SWTThreadSetup instanceSeparate;

    private static SWTThreadSetup instanceSame;

    public Display display;

    public Shell shell;

    public Thread uiThread;

    public static SWTThreadSetup getSameThreadInstance()
    {
        if (instanceSame == null)
        {
            instanceSame = new SWTThreadSetup();
            try
            {
                instanceSame.initialize(false);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Could not create example ");
            }
        }
        return instanceSame;
    }

    public static SWTThreadSetup getSeparateThreadInstance()
    {
        if (instanceSeparate == null)
        {
            instanceSeparate = new SWTThreadSetup();
            try
            {
                instanceSeparate.initialize(true);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Could not create example ");
            }
        }
        return instanceSeparate;
    }

    /**
     * @param hasSetup
     */
    private void createUIThread(final boolean[] hasSetup)
    {
        if (uiThread == null || !uiThread.isAlive())
        {
            uiThread = new Thread("UI Thread")
            {
                @Override
                public void run()
                {
                    _setup(hasSetup);
                    while (display != null && !display.isDisposed())
                        if (!display.readAndDispatch())
                            display.sleep();
                }
            };
            uiThread.start();
        }
        else
            hasSetup[0] = true;
    }

    /**
     * @param hasSetup
     */
    private void _setup(final boolean[] hasSetup)
    {
        createDisplay();
        createShell();
        openShells();
        hasSetup[0] = true;
    }

    protected void openShells()
    {
        shell.open();
    }

    /**
     * Subclasses may override to create a shell.
     */
    protected void createShell()
    {
        if (shell == null || shell.isDisposed())
        {
            shell = new Shell(display, SWT.SHELL_TRIM);
            shell.setLayout(new FillLayout());
            shell.setText("TITLE");
        }
    }

    /**
     * Subclasses may override to create a display.
     */
    protected void createDisplay()
    {
        if (display == null || display.isDisposed())
        {
            display = Display.getDefault();
            shell = null;
        }
    }

    /**
     * @param controls
     * @throws Exception
     */
    public void initialize(boolean shouldRunInSeparateThread) throws Exception
    {
        final boolean hasSetup[] = new boolean[]{false};

        if (shouldRunInSeparateThread)
            createUIThread(hasSetup);
        else
            _setup(hasSetup);
        while (!hasSetup[0])
            Thread.sleep(100);
    }

}
