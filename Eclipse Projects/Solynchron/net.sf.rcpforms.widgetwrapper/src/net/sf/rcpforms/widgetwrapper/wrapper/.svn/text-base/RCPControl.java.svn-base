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

import java.lang.reflect.Field;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.staterenderer.StateRenderer;
import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * Control is the base class of all user interface widgets which live in a window and have bounds.
 * <p>
 * All Controls are optionally labelled, so they need the label which belongs to them. The label is
 * created in the builder.
 * <p>
 * 
 * @author Marco van Meegen
 */
public class RCPControl extends RCPWidget
{
    /**
     * if you make a control invisible, the wrappers automatically exclude them from layout, thus
     * their space is reused. If this flag is set, the standard swt behavior is kept, thus the
     * invisible control still takes up the space it had before.
     */
    private boolean keepSpaceIfInvisible = false;

    /** renderer used for widget state rendering */
    private StateRenderer stateRenderer = null;

    /**
     * holds the rcp parent for this widget; this need not correlate to the swt parent, this is NOT
     * always true: getTypedWidget().getParent() == getRCPParent().getTypedWidget(), e.g. for
     * sections
     */
    private RCPCompound rcpParent = null;

    /**
     * layout data to pass to widget; it is stored here, thus layout can be set if widget is not
     * created yet
     */
    private Object layoutData;

    private int controlState = EControlState.getFlags(EControlState.VISIBLE, EControlState.ENABLED);

    private ListenerList stateListenerList = new ListenerList();

    private StateListener stateListener;

    private EControlState lastFiredState = null;

    /**
     * control decoration if this is a required field
     */
    public ControlDecoration requiredDecoration = null;

    /**
     * control decoration for later purpose
     */
    public ControlDecoration otherDecoration;

    private static FieldDecoration otherFieldDeco;

    public RCPControl(String labelText, int style)
    {
        super(labelText, style);
        stateListener = new StateListener()
        {
            public void stateChanged(StateChangeEvent event)
            {
                updateState();
                // if itself a composite, propagate state
                if (isContainer())
                {
                    // add correct old/new state calculation since change
                    // of parent need not cause the the same state change in
                    // child
                    fireStateChange(event.getState(), lastFiredState, event.getNewState());
                    lastFiredState = event.getState();
                }
            }
        };
    }

    protected void addStateListener(StateListener listener)
    {
        stateListenerList.add(listener);
    }

    protected void removeStateListener(StateListener listener)
    {
        stateListenerList.remove(listener);
    }

    public void fireStateChange(EControlState state, Object oldValue, Object newValue)
    {
        StateChangeEvent event = new StateChangeEvent(this, state, oldValue, newValue);
        Object[] listeners = stateListenerList.getListeners();
        for (int i = 0; i < listeners.length; ++i)
        {
            ((StateListener) listeners[i]).stateChanged(event);
        }
    }

    /**
     * @return convenience method for {@link #getTypedWidget()} to return the widget as Control
     * @throws IllegalStateException if widget is not created or disposed
     */
    public final Control getSWTControl()
    {
        Control result = (Control) getWrappedWidget();
        WidgetUtil.ensureValid(this);
        return result;
    }

    /**
     * true if this widget is composed of other widgets, by swt composition or compound widgets.
     * RCPCompound redefines this to true.
     * <p>
     * This is different from {@link RCPCompound#isExtensible()} which defines who can compose it,
     * subclassers or clients.
     */
    public boolean isContainer()
    {
        return false;
    }

    public RCPCompound getRCPParent()
    {
        return rcpParent;
    }

    public void setRcpParent(RCPCompound anRcpParent)
    {
        this.rcpParent = anRcpParent;
        if (rcpParent != null)
        {
            rcpParent.removeStateListener(stateListener);
        }
        rcpParent = anRcpParent;
        if (rcpParent != null)
        {
            rcpParent.addStateListener(stateListener);
        }

    }

    /**
     * @return the swt parent
     * @throws IllegalStateException if not created or disposed
     */
    public Composite getSWTParent()
    {
        Validate.notNull(getRCPParent(), "RCP Parent must be set to retrieve swt parent"); //$NON-NLS-1$
        return getRCPParent().getClientComposite();
    }

    @Override
    protected void onDispose()
    {
        super.onDispose();
        if (rcpParent != null)
        {
            rcpParent.removeStateListener(stateListener);
        }
    }

    /**
     * modifies the given state of the widget and fires a state change event
     */
    public void setState(EControlState state, boolean value)
    {
        doSetState(state, value);
    }

    /**
     * if for a widget special state rendering is needed, a custom renderer can be set here; however
     * this is not recommended, since applicationwide state rendering should be consistent and to
     * customize rendering for a RCP Application this can be done by
     * {@link StateRenderer#registerStateRenderer(Class, StateRenderer) registering a specific State
     * Renderer}
     * 
     * @param renderer renderer to use or null if the default renderer registered for this widget
     *            class should be used
     */
    public void setStateRenderer(StateRenderer renderer)
    {
        this.stateRenderer = renderer;
        updateState();
    }

    /**
     * @param state
     * @param value
     */
    protected void doSetState(EControlState state, boolean value)
    {
        int oldState = controlState;
        this.controlState = EControlState.modifyState(state, value, this.controlState);
        if (oldState != controlState)
        {
            updateState();
            fireStateChange(state, !value, value);
        }
    }

    /**
     * @return true if this widget is enabled and the known parent chain is enabled
     */
    public final boolean isChainEnabled()
    {
        return getState(EControlState.ENABLED)
                && (getRCPParent() == null || getRCPParent().isChainEnabled());
    }

    /**
     * @return true if this widget is visible and the known parent chain is visible
     */
    public final boolean isChainVisible()
    {
        return getState(EControlState.VISIBLE)
                && (getRCPParent() == null || getRCPParent().isChainVisible());
    }

    /**
     * reflows the form the widget is contained in (if existing)
     */
    public void reflowForm()
    {
        Control parent = getSWTParent();
        while (parent != null && !(parent instanceof ScrolledForm))
        {
            parent = parent.getParent();
        }
        if (parent != null)
        {
            ((ScrolledForm) parent).reflow(true);
        }
    }

    public final boolean getState(EControlState state)
    {
        return EControlState.isMember(state, controlState);
    }

    /** convenience state getter */
    public final boolean isVisible()
    {
        return getState(EControlState.VISIBLE);
    }

    /** convenience state getter */
    public final boolean isEnabled()
    {
        return getState(EControlState.ENABLED);
    }

    /** convenience state getter */
    public final boolean isMandatory()
    {
        return getState(EControlState.MANDATORY);
    }

    /** convenience state setter */
    public final void setVisible(boolean newState)
    {
        setState(EControlState.VISIBLE, newState);
    }

    /** convenience state setter */
    public final void setEnabled(boolean newState)
    {
        setState(EControlState.ENABLED, newState);
    }

    /** convenience state setter */
    public final void setMandatory(boolean newState)
    {
        setState(EControlState.MANDATORY, newState);
    }

    private StateRenderer getStateRenderer()
    {
        return stateRenderer != null ? stateRenderer : StateRenderer.getInstance(getClass());
    }

    /**
     * updateState may be called in states where the control is not created yet; implementors must
     * take care of this !
     */
    protected void updateState()
    {
        getStateRenderer().updateState(this, controlState);
    }

    public boolean hasState(EControlState state)
    {
        return EControlState.isMember(state, this.controlState);
    }

    @Override
    public void createUI(FormToolkit formToolkit)
    {
        doCreateUI(formToolkit);
    }

    /**
     * @param formToolkit
     */
    @Override
    protected void doCreateUI(FormToolkit formToolkit)
    {
        Validate.notNull(getRCPParent(), "RCPParent must be set before createUI() is called"); //$NON-NLS-1$
        Validate.notNull(getSWTParent(),
                "createUI() of parent must have been called before createUI() is called"); //$NON-NLS-1$
        super.doCreateUI(formToolkit);
        // set the layout if it was set into the wrapper before creation
        if (layoutData != null)
        {
            getSWTControl().setLayoutData(layoutData);
        }
        // init control state
        updateState();
    }

    /**
     * @return the nearest parent of type ScrolledForm or null if not living in a scrolled form
     */
    protected ScrolledForm getScrolledForm()
    {
        Composite parent = this.getSWTParent();
        while (parent != null && !(parent instanceof ScrolledForm))
        {
            parent = parent.getParent();
        }
        return (ScrolledForm) parent;
    }

    /**
     * number of layout cells (controls) occupied by this widget, usually 1. for compounds the nr of
     * children is returned.
     * 
     * @return number of layout cells
     */
    @Override
    public int getNumberOfControls()
    {
        return 1;
    }

    /**
     * set layout data to widget, this works even if the widget is not created yet
     * 
     * @param layoutData layout data to set to the swt control
     */
    public void setLayoutData(Object layoutData)
    {
        this.layoutData = layoutData;
        if (WidgetUtil.isValid(this))
        {
            getSWTControl().setLayoutData(layoutData);
        }
    }

    /**
     * @param keepSpaceIfInvisible The keepSpaceIfInvisible to set.
     */
    public void setKeepSpaceIfInvisible(boolean keepSpaceIfInvisible)
    {
        this.keepSpaceIfInvisible = keepSpaceIfInvisible;
        updateState();
    }

    /**
     * @return Returns the keepSpaceIfInvisible.
     */
    public boolean isKeepSpaceIfInvisible()
    {
        return keepSpaceIfInvisible;
    }

    /**
     * @param state indicates the state decoration
     * @return decoration to the desired state or NULL if no decoration is avalable
     */
    public ControlDecoration getDecoration(EControlState state)
    {
        ControlDecoration decoration = null;
        switch (state)
        {
            case OTHER:
                decoration = otherDecoration;
                break;
            default:
                break;
        }

        return decoration;
    }

    /** NON-API; for unit testing only */
    public boolean isRequiredDecorationVisible() throws Exception
    {
        boolean result = false;
        if (requiredDecoration != null)
        {
            // hack to test if it is really visible
            Field f = requiredDecoration.getClass().getDeclaredField("visible"); //$NON-NLS-1$
            f.setAccessible(true);
            result = f.getBoolean(requiredDecoration);
        }
        return result;
    }
}
