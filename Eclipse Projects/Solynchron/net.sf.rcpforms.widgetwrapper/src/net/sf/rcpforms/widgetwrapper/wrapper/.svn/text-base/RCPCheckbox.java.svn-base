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
// Created on 15.01.2008

package net.sf.rcpforms.widgetwrapper.wrapper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * convenience wrapper for a swt Button with {@link SWT#CHECK} style.
 * <p>
 * 
 * @author Marco van Meegen
 */
public class RCPCheckbox extends RCPSimpleButton
{
    public RCPCheckbox(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPCheckbox(String label, int style)
    {
        super(label, style);
    }

    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Button result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createCheckbox(getSWTParent(), getLabel());
        }
        else
        {
            result = formToolkit.createButton(getSWTParent(), getLabel(), getStyle() | SWT.CHECK);
        }
        return result;
    }
}
