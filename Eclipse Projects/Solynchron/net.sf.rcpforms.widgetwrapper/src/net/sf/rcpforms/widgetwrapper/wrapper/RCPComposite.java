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

import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class RCPComposite extends RCPCompound
{

    public RCPComposite()
    {
        super(null, SWT.None);
    }

    public RCPComposite(String labelText, int style)
    {
        super(labelText, style);
    }

    /** wrap a RCPComposite around any existing composite, e.g. a shell */
    public RCPComposite(Composite parent)
    {
        super(null, parent.getStyle());
        setWrappedWidget(parent);

    }

    /**
     * @return the layout set to this composite
     * @see org.eclipse.swt.widgets.Composite#getLayout()
     */
    public Layout getLayout()
    {
        return getClientComposite().getLayout();
    }

    /**
     * @param layout layout to set to this composite
     */
    public void setLayout(Layout layout)
    {
        getClientComposite().setLayout(layout);
    }

    /**
     * @return the composite which can be used as parent for contained widgets, this need not to be
     *         the swt control corresponding to this control, e.g. for CGroups this is not the group
     *         itself but the composite contained in the CGroup.
     */
    @Override
    public Composite getClientComposite()
    {
        WidgetUtil.ensureValid(this);
        return getTypedWidget();
    }

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Composite result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = formToolkit.createComposite(getSWTParent());
        }
        else
        {
            result = formToolkit.createComposite(getSWTParent(), getStyle());
        }
        return result;
    }

    /**
     * override to enable control state management for composites to be like for controls. Rest of
     * state management will be inherited. Take care: not all states make sense for composites.
     */
    @Override
    public void setState(EControlState state, boolean value)
    {
        doSetState(state, value);
    }

    /**
     * returns 1, since an rcp composite occupies one grid cell
     */
    @Override
    public int getNumberOfControls()
    {
        return 1;
    }

    /**
     * by default it is allowed for the client to add children to a composite; if this is false, the
     * derived composite will add its children in {@link #createUI(FormToolkit)} and manage layout
     * itself, thus it can only be used as a whole and no children can be added by a client using a
     * composite builder.
     */
    @Override
    public boolean isExtensible()
    {
        return true;
    }

    /**
     * overridden to restore behavior of RCPControl to create the embedded composite, since compound
     * does not create any control. ATTENTION: children of composites will be created automatically,
     * if {@link #isExtensible()} is false
     */
    @Override
    public void createUI(FormToolkit formToolkit)
    {
        doCreateUI(formToolkit);
        // if the composite is not meant to be extended by a client,
        // it will have created its rcp controls in createUI and then will call super.createUI()
        // which will in turn create the swt controls
        if (!isExtensible())
        {
            doCreateChildrenUI(formToolkit);
        }
    }
}
