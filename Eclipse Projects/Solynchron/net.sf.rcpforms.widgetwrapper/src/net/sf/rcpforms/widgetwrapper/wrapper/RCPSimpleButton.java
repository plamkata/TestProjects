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
 * wraps a button
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleButton extends RCPControl
{
    protected Image image = null;

    public RCPSimpleButton(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPSimpleButton(String label, Image image)
    {
        this(label, image, SWT.DEFAULT);
    }

    public RCPSimpleButton(String label, int style)
    {
        this(label, null, style);
    }

    public RCPSimpleButton(String label, Image image, int style)
    {
        super(label, style);
        this.image = image;
    }

    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Button result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createButton(getSWTParent(), getLabel());
        }
        else
        {
            result = formToolkit.createButton(getSWTParent(), getLabel(), getStyle());
        }
        setImage(result);
        return result;
    }

    /**
     * set the image passed in the constructor to the created button
     * 
     * @param button
     */
    protected final void setImage(Button button)
    {
        if (image != null)
        {
            button.setImage(image);
        }
    }

    public final Button getSWTButton()
    {
        return getTypedWidget();
    }
}
