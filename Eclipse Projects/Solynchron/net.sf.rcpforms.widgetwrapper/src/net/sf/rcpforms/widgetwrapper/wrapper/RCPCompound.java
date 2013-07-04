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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.rcpforms.common.util.Validate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Class RCPCompound represents a grouping of widgets which are not embedded into a separate
 * composite, thus their component widgets can be placed in a layout grid separately.
 * <p>
 * State is maintained for the compound, thus the whole group can be enabled or disabled etc.
 * Grouping is relevant for layout; usually the builder will have to set layouts for all compounds
 * found.
 * <p>
 * There are two user stories for compounds:
 * <ol>
 * <li>to group widget arrangements which correspond to a single property in the model; e.g. a
 * label, a text field and a date picker button would constitute a widget arrangement for one date
 * field.
 * <p>
 * A variation is a group of properties strongly related to one conceptual value, e.g. a
 * "validity range" property might consist of a from date and a to date and only be active, if not
 * both dates are null. In UI you want to represent it as a checkbox if it is valid, and a label,
 * text field, date picker button for from and to dates each, ending up in 7 controls.
 * <p>
 * This usage is the main recommended usage for compounds, isExtensible() will return false since
 * such a compound is not meant to be extended by clients.
 * <p>
 * Usually UIs will have lots of these control arrangements and you should create your
 * project-specific compound widgets, register default layouts for these with the builder or enhance
 * the builder to create your widgets with proper layouts before you start coding forms.
 * <p>
 * <li>create reusable UI components which a have dependency on business artifacts and are reused in
 * different forms, e.g. displaying/editing an address. It is recommended to place these components
 * into a separate form part together with the binding code against the model and do not use a
 * compound.
 * </ol>
 * ATTENTION: Special treatment is needed for RCPCompound derived widgets:
 * <p>
 * <ul>
 * <li>overwrite {@link #setLayoutData(Object)} to do something useful for all components
 * <li>make sure the builder sets layouts as required for all children
 * <li>create an IObservableValue for the control in the binding factory if it is single-valued or
 * make sure you bind not against the control but against sub controls explicitly
 * </ul>
 *
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class RCPCompound extends RCPControl
{
    private List<RCPControl> children = new ArrayList<RCPControl>();

    /**
     * Constructor for RCPCompound
     */
    public RCPCompound()
    {
        // compound itself has no style and no label
        this(null, SWT.None);
    }

    protected RCPCompound(String labelText, int style)
    {
        // pass for derived classes
        super(labelText, style);
    }

    /**
     * Constructor for RCPCompound
     *
     * @param label
     */
    public RCPCompound(String label)
    {
        super(label, SWT.None);
    }

    /**
     * add a child to the compound widget; this is solely meant for clients which want to add
     * children to compounds designed to be extensible, e.g. Composite, Section, Group. If you want
     * to create a standard control composed of several widgets, e.g. DateSpan, please use
     * {@link #internalAdd(RCPControl)}.
     */
    public RCPCompound add(RCPControl widget)
    {
        Validate
                .isTrue(
                        isExtensible(),
                        "This compound is not meant to be extended by clients. If you want to add children in a subclass, use internalAdd()"); //$NON-NLS-1$
        return internalAdd(widget);
    }

    /**
     * @param widget
     * @return
     */
    protected RCPCompound internalAdd(RCPControl widget)
    {
        Validate.notNull(widget);
        widget.setRcpParent(this);
        children.add(widget);

        return this;
    }

    /**
     * get the swt controls for direct children of this compound; ATTENTION: usually NOT true:
     * getSwtComposite().getChildren() equals getChildren().
     * <p>
     *
     * @return ordered unmodifiable list of swt controls for the children of this compound
     */
    public List<Widget> getChildren()
    {
        List<Widget> result = new ArrayList<Widget>();
        for (RCPWidget child : children)
        {
            result.add(child.getSWTWidget());
        }
        return Collections.unmodifiableList(result);
    }

    public List<RCPControl> getRcpChildren()
    {
        return Collections.unmodifiableList(children);
    }

    /**
     * returns the composite which can be used as swt parent for contained widgets.
     * <p>
     * This need not to be the swt control corresponding to this control, e.g. for sections this is
     * not the section itself but the client composite.
     * <p>
     * Since compounds usually have no control, for compounds it is the composite of the parent, for
     * nested compounds the first parent which is a swt composite.
     *
     * @return
     */
    public Composite getClientComposite()
    {
        Validate.notNull(getRCPParent(), "RCPCompound must have a parent"); //$NON-NLS-1$
        return getRCPParent().getClientComposite();
    }

    /**
     * default is to set the state on all children
     */
    @Override
    public void setState(EControlState state, boolean value)
    {
        for (RCPWidget child : getRcpChildren())
        {
            if (child instanceof RCPControl)
            {
                ((RCPControl) child).setState(state, value);
            }
        }
    }

    /**
     * @return true if clients are allowed to add children, i.e. this compound is meant to be
     *         extensible, e.g. radio groups etc.., default: false
     */
    public boolean isExtensible()
    {
        return false;
    }

    /**
     * creates the UI for this compound. The UI for the children is not automatically created, this
     * is the task of the builder, since layout has to be assigned during recursive creation, but
     * layout is not a task of the widgets, but separated into the builder.
     */
    @Override
    public void createUI(FormToolkit formToolkit)
    {
        doCreateChildrenUI(formToolkit);
    }

    /**
     * creates children
     *
     * @param formToolkit form toolkit to use
     */
    protected final void doCreateChildrenUI(FormToolkit formToolkit)
    {
        // parent must be set and created first
        Validate.notNull(getRCPParent());
        Validate.notNull(getSWTParent());
        this.formToolkit = formToolkit;

        // now create children
        for (RCPControl ctrl : getRcpChildren())
        {
            Validate.notNull(formToolkit);
            ctrl.setRcpParent(this);
            ctrl.createUI(formToolkit);
        }
    }

    /**
     * compound is a container for widgets
     */
    @Override
    public boolean isContainer()
    {
        return true;
    }

    /**
     * (non-Javadoc)
     *
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPControl#getNumberOfControls()
     */
    @Override
    public int getNumberOfControls()
    {
        int result = 0;
        // if child is a rcpcompound: compound can have multiple children ; thus
        // a recursive call is done here
        for (RCPWidget child : getRcpChildren())
        {
            result += child.getNumberOfControls();
        }
        return result;
    }

    /**
     * for non-extensible compounds this returns the control which should be used by the builder for
     * parameterized layout. Since a builder usually passes a single amount of layout information,
     * this information by default should be used to manipulate the main control of a compound; all
     * other controls will be layouted using standard information, thus cannot be modified by a
     * builder.
     * <p>
     * The idea is that most compound controls are like label and text where the label has a fixed
     * layout and the text control gets parameters passed by the builder, e.g. if it should expand
     * or have a minimum amount of characters in width.
     *
     * @return the main control of the compound or null if paramters of builder should not be passed
     *         to any control layout.
     */
    public RCPControl getMainControl()
    {
        return null;
    }

    @Override
    public void dispose()
    {
        // dispose all children
        for (RCPWidget child : getRcpChildren())
        {
            child.dispose();
        }
        super.dispose();
    }
}
