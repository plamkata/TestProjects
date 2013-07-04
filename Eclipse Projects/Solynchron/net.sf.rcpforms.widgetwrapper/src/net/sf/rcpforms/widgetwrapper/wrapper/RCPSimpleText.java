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

package net.sf.rcpforms.widgetwrapper.wrapper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wraps a Text with a label. a Text control can be additionally configured with constraint
 * information about the text entered, wrapped in a TextConfiguration.
 * <p>
 * TODO: support TextConfiguration from "metainformation provider" (provide input validation for
 * regexp, formatted text,) -> see task 152105
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleText extends RCPControl
{
    /**
     * creates a text control with the given text as content
     */
    public RCPSimpleText()
    {
        this(SWT.DEFAULT);
    }

    public RCPSimpleText(int style)
    {
        super(null, style);
    }

    public final Text getSWTText()
    {
        return getTypedWidget();
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Text result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkit().createText(getSWTParent(), ""); //$NON-NLS-1$
        }
        else
        {
            result = getFormToolkit().createText(getSWTParent(), "", getStyle()); //$NON-NLS-1$
        }
        return result;
    }
}
