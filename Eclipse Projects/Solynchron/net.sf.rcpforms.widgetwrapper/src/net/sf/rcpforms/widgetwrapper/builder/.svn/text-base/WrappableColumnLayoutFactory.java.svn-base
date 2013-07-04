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

package net.sf.rcpforms.widgetwrapper.builder;

import net.sf.rcpforms.common.util.Validate;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;

/**
 * A factory that applies a columns layout. It wraps the columns if the available width is not
 * sufficient to display them in a row.
 * 
 * @author dietisheima
 */
public class WrappableColumnLayoutFactory
{

    /**
     * applies the columns layout using the number of columns indicated. Margins and spacing are set
     * to 0.
     * 
     * @param composite the parent composite that shall be layouted in the indicated number of
     *            columns
     */
    public static ColumnLayout applyLayout(final Composite composite, final int maxColumn)
    {
        ColumnLayout columnLayout = applyLayout(composite);
        columnLayout.minNumColumns = 1;
        columnLayout.maxNumColumns = maxColumn;
        return columnLayout;
    }

    /**
     * applies the 2-columns layout to the parent composite. Margins and spacing are set to 0.
     * 
     * @param composite the parent composite that shall be layouted in 2 columns
     */
    public static ColumnLayout applyLayout(final Composite composite)
    {
        ColumnLayout columnLayout = new ColumnLayout();
        columnLayout.minNumColumns = 1;
        columnLayout.maxNumColumns = 2;
        columnLayout.topMargin = 0;
        columnLayout.rightMargin = 0;
        columnLayout.bottomMargin = 0;
        columnLayout.leftMargin = 0;
        columnLayout.horizontalSpacing = 0;
        composite.setLayout(columnLayout);
        return columnLayout;
    }

    /**
     * applies the layout data to a column (child composite)
     * 
     * @param composite the child composite that is the left column. Width- and height-hints are
     *            only applied if > 0. The width-hint is further constrained by the fact that
     *            columnLayout always renders columns to equal width
     * @return
     */
    public static ColumnLayoutData applyLayoutData(final Composite composite, final int widthHint,
                                                   final int heightHint)
    {
        Validate.notNull(composite, "Control ctrl"); //$NON-NLS-1$
        ColumnLayoutData columnLayoutData = new ColumnLayoutData();
        columnLayoutData.horizontalAlignment = ColumnLayoutData.FILL;
        if (widthHint > 0)
        {
            columnLayoutData.widthHint = widthHint;
        }
        if (heightHint > 0)
        {
            columnLayoutData.heightHint = heightHint;
        }
        composite.setLayoutData(columnLayoutData);
        return columnLayoutData;
    }

}
