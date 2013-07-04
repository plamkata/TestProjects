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

package net.sf.rcpforms.examples.app;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the actions added to
 * a workbench window. Each window will be populated with new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{

    // Actions - important to allocate these only in makeActions, and then use
    // them
    // in the fill methods. This ensures that the actions aren't recreated
    // when fillActionBars is called with FILL_PROXY.
    private IWorkbenchAction exitAction, saveAction, saveAllAction;

    private final IWorkbenchWindow m_workbenchWindow;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
    {
        super(configurer);
        m_workbenchWindow = configurer.getWindowConfigurer().getWindow();
    }

    protected void makeActions(final IWorkbenchWindow window)
    {
        // Creates the actions and registers them.
        // Registering is needed to ensure that key bindings work.
        // The corresponding commands keybindings are defined in the plugin.xml
        // file.
        // Registering also provides automatic disposal of the actions when
        // the window is closed.

        saveAction = ActionFactory.SAVE.create(window);
        register(saveAction);
        saveAllAction = ActionFactory.SAVE_ALL.create(window);
        register(saveAllAction);
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
    }

    protected void fillMenuBar(IMenuManager menuBar)
    {
        MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
        menuBar.add(fileMenu);
        fileMenu.add(saveAction);
        fileMenu.add(saveAllAction);
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);
        menuBar.add(createShortlistMenu());
    }

    protected MenuManager createShortlistMenu()
    {
        MenuManager menuManager = new MenuManager("Show &View", "showView");
        IContributionItem item = ContributionItemFactory.VIEWS_SHORTLIST.create(m_workbenchWindow);
        menuManager.add(item);
        return menuManager;
    }

}
