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

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.customwidgets.CustomButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wraps a custom button rendering an image and nothing but an image
 * 
 * @author Marco van Meegen
 */
public class RCPHyperlinkButton extends RCPControl
{
    protected Image image = null;

    public RCPHyperlinkButton(String label, Image image)
    {
        this(label, image, SWT.DEFAULT);
    }

    /**
     * no styles supported
     */
    private RCPHyperlinkButton(String label, Image image, int style)
    {
        super(label, style);
        Validate.notNull(image);
        this.image = image;
    }

    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        CustomButton result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createCustomButton(getSWTParent(), image);
        }
        else
        {
            throw new IllegalArgumentException("Styles are not supported by this control"); //$NON-NLS-1$
        }
        if (image != null)
        {
            result.setImage(image);
        }
        return result;
    }
}
