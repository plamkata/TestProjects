/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.widgetwrapper.builder;

import org.eclipse.ui.forms.widgets.ColumnLayout;

public class ColumnLayoutFactory
{

    /**
     * TODO refactor to enable customizing with a different factory.
     * 
     * @return default layout for forms
     */
    public static ColumnLayout createFormLayout()
    {
        ColumnLayout layout = new ColumnLayout();
        layout.horizontalSpacing = 25;
        layout.verticalSpacing = 25;
        return layout;
    }

}
