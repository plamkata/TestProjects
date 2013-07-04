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

import net.sf.rcpforms.common.NullValue;

import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * wraps a CCombo; a Combo is not supported since it does not support editable true/false at
 * runtime.
 * <p>
 * Since using JFace viewers is recommended, automatically a viewer is created with the control
 * 
 * @author Marco van Meegen
 */
public class RCPSimpleCombo extends RCPControl implements IViewer, IPresentationConfiguration
{
    private AbstractListViewer viewer = null;

    /**
     * flag if combo should support selection of null value; actually this should be handled in the
     * content provider, but for automatic creation this parameter is passed here.
     */
    private boolean hasNullValue = false;

    /**
     * presentation Key to use; this is useful for enum values to choose between different
     * representations
     */
    private String presentationKey = null;

    /**
     * presentation Key to use for null value; sometimes combos should display a different null
     * value in different forms, this can be used to handle the case.
     */
    private String nullValuePresentationKey = NullValue.GENERIC_NULL_VALUE_REPRESENTATION_EMPTY;

    public RCPSimpleCombo(String label)
    {
        this(label, SWT.DEFAULT, false);
    }

    public RCPSimpleCombo(String label, boolean hasNullValue)
    {
        this(label, SWT.DEFAULT, hasNullValue);
    }

    public RCPSimpleCombo(String label, int style, boolean hasNullValue)
    {
        super(label, style);
        this.hasNullValue = hasNullValue;
    }

    /**
     * create a CCombo to enable update dynamic setting of editable style
     */
    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        CCombo result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createCombo(getSWTParent());
        }
        else
        {
            result = getFormToolkitEx().createCombo(getSWTParent(), getStyle());
        }
        result.setEditable(false);
        // create the viewer too
        // own implementation of combo viewer needed to handle null value
        viewer = new RCPComboViewer(result);
        ((RCPComboViewer)viewer).setNullValuePresentationKey(this.getNullValuePresentationKey());
        return result;
    }

    public final CCombo getSWTCombo()
    {
        return getTypedWidget();
    }

    public ContentViewer getViewer()
    {
        return viewer;
    }

    /**
     * @return true if this combo should support a null value; no functionality behind this, since
     *         content provider is responsible for that. just convenience to pass it here since with
     *         RangeAdapters the ValidationManager will check this flag and pass it to the
     *         auto-created content provider.
     */
    public boolean hasNullValue()
    {
        return hasNullValue;
    }

    /**
     * @return Returns the presentationKey.
     */
    public String getPresentationKey()
    {
        return presentationKey;
    }

    /**
     * @param presentationKey The presentationKey to set.
     */
    public void setPresentationKey(String presentationKey)
    {
        this.presentationKey = presentationKey;
    }

    /**
     * @return Returns the nullValuePresentationKey.
     */
    public String getNullValuePresentationKey()
    {
        return nullValuePresentationKey;
    }

    /**
     * @param nullValuePresentationKey The nullValuePresentationKey to set.
     */
    public void setNullValuePresentationKey(String nullValuePresentationKey)
    {
        this.nullValuePresentationKey = nullValuePresentationKey;
    }
}
