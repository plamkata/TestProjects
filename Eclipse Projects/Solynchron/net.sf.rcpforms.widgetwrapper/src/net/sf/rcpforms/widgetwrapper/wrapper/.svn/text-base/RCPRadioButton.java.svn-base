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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * convenience wrapper for a swt Button with {@link SWT#RADIO} style.
 * <p>
 * 
 * @author Marco van Meegen
 */
public class RCPRadioButton extends RCPSimpleButton
{
    public RCPRadioButton(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPRadioButton(String label, int style)
    {
        this(label, null, style);
    }

    public RCPRadioButton(String label, Image image, int style)
    {
        super(label, image, style);
    }

    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Button result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createRadioButton(getSWTParent(), getLabel());
        }
        else
        {
            result = formToolkit.createButton(getSWTParent(), getLabel(), getStyle() | SWT.RADIO);
        }
        return result;
    }
}
