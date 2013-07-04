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

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;

/**
 * a compound of a label and a {@link RCPSimpleCombo}.
 * 
 * @author Marco van Meegen
 */
public class RCPCombo extends RCPLabeledControl<RCPSimpleCombo>
    implements IViewer, IPresentationConfiguration
{
    public RCPCombo(String label)
    {
        this(label, false);
    }

    public RCPCombo(String label, boolean hasNullValue)
    {
        this(label, SWT.DEFAULT, hasNullValue);
    }

    public RCPCombo(String label, int style, boolean hasNullValue)
    {
        super(label, new RCPSimpleCombo(label, style, hasNullValue));
    }

    public final CCombo getSWTCombo()
    {
        return getRCPControl().getSWTCombo();
    }

    public ContentViewer getViewer()
    {
        return getRCPControl().getViewer();
    }

    /**
     * @return
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleCombo#getNullValuePresentationKey()
     */
    public String getNullValuePresentationKey()
    {
        return getRCPControl().getNullValuePresentationKey();
    }

    /**
     * @return
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleCombo#getPresentationKey()
     */
    public String getPresentationKey()
    {
        return getRCPControl().getPresentationKey();
    }

    /**
     * @return
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleCombo#hasNullValue()
     */
    public boolean hasNullValue()
    {
        return getRCPControl().hasNullValue();
    }

    /**
     * @param nullValuePresentationKey
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleCombo#setNullValuePresentationKey(String)
     */
    public void setNullValuePresentationKey(String nullValuePresentationKey)
    {
        getRCPControl().setNullValuePresentationKey(nullValuePresentationKey);
    }

    /**
     * @param presentationKey
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleCombo#setPresentationKey(String)
     */
    public void setPresentationKey(String presentationKey)
    {
        getRCPControl().setPresentationKey(presentationKey);
    }

}
