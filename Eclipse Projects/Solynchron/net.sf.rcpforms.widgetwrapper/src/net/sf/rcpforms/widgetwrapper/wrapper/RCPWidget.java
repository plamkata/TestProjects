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

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.builder.IFormToolkitEx;
import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * An RCPWidget wraps an SWT widget to enhance it by the following features:
 * <ul>
 * <li>two phase ui creation, widget hierarchy construction phase (constructor) is different from
 * control creation phase ({@link #createUI(FormToolkit)}
 * <li>Creation is rooted via a toolkit, thus by changing the toolkit you can change the overall
 * looks as colors, margins, fonts
 * <li>State Management: wrapped controls offer enhanced control states like READONLY, MANDATORY,
 * RECOMMENDED ({@link RCPControl#setState(EControlState, boolean)}, {@link EControlState} and
 * improve state rendering and behavior of swt states like VISIBLE,ENABLED
 * <li>state binding via a uniform interface; you can bind the widget state to a property and it
 * will automatically refresh on each change
 * <li>hierarchical state management: if a parents state is changed, children states will be updated
 * too, e.g. a disabled parent will not need required field decorations anymore
 * <li>decouple physical containment from logical containment, e.g. a child of a RCPSection will
 * automatically be added as a child to the Section Composite, significantly reducing programming
 * errors and easing enhancement with new widgets
 * <li>default style: if you pass {@link SWT#DEFAULT} as a style during construction of a RCPWidget,
 * a default style is taken from the FormToolkit passed; this avoids hardcoding defaults into the
 * application.
 * </ul>
 * 
 * @author Marco van Meegen
 */
public class RCPWidget
{
    private Widget wrappedWidget;

    private DisposeListener disposeListener = new DisposeListener()
    {
        public void widgetDisposed(DisposeEvent e)
        {
            // if swt widget is disposed, cleanup any listeners
            onDispose();
        }
    };

    /**
     * internal id of the control, internally used for debug logging, maybe used for GUI testing
     * tools
     */
    private String id;

    /**
     * text label to apply to the control; mainly used for control like Group, Button which include
     * a label, but also set on a {@link RCPLabeledControl} as the text of the label control.
     */
    private String label;

    private int style;

    /**
     * the toolkit used to create this display; colors and other resources are accessed via the
     * toolkit
     */
    protected FormToolkit formToolkit = null;

    public RCPWidget()
    {

    }

    /**
     * create a widget with the given labelText (if the widget has a text, e.g. a button)
     * 
     * @param labelText labelText to set for the widget, maybe be null if no label text needed or
     *            widget does not support a label text
     * @param style SWT style to create widget with; subclasses may add some style, e.g. RCPCheckbox
     *            will add SWT.CHECK style; maybe SWT.DEFAULT which calls the FormToolkit method
     *            without handling a style, thus enabling the Formtoolkit to define the default
     */
    public RCPWidget(String labelText, int style)
    {
        this.label = labelText;
        // TODO: add constructor with separate non-i18n id, for now use label as id
        this.id = labelText != null ? labelText : generateId();
        Validate.notNull(id);
        this.style = style;
    }

    /**
     * @return a unique id
     */
    private String generateId()
    {
        return super.toString();
    }

    protected Widget getWrappedWidget()
    {
        return wrappedWidget;
    }

    /**
     * simplified way for widget handling if exactly one swt control is wrapped; you only need to
     * override this method to create the control using the formtoolkit.
     * <p>
     * ATTENTION: Since RCPWidgets support SWT.DEFAULT as flag that no style was specified, but
     * FormToolkit does not, this must be handled here ! Do not call super(), it will throw an
     * exception.
     * 
     * @param formToolkit toolkit to use to create the widget
     * @return created widget, createUI will automatically initialize it correctly to work with the
     *         wrappers
     */
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Validate.isTrue(false, this.getClass().getName() + " must override createWrappedWidget()"); //$NON-NLS-1$
        return null;
    }

    /**
     * internal use only, if a widget is already existing and should be wrapped.
     */
    protected void setWrappedWidget(Widget widget)
    {
        this.wrappedWidget = widget;
    }

    @SuppressWarnings("unchecked")
    public final <T> T getTypedWidget()
    {
        return (T) getWrappedWidget();
    }

    /**
     * typed convenience method to retrieve the swt widget
     * 
     * @return swt widget
     * @throws IllegalStateException if widget is not available or disposed
     */
    public final Widget getSWTWidget()
    {
        return getTypedWidget();
    }

    /**
     *explicitly dispose the associated widget; do not do cleanup here !
     * <p>
     * cleanup will be done in {@link #onDispose()}
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose()
    {
        if (wrappedWidget != null)
        {
            wrappedWidget.dispose();
        }
    }

    /**
     * called when the associated swt widget is disposed; must clean up all listeners and local
     * resources
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    protected void onDispose()
    {
    }

    /**
     * @return the label text passed in the constructor, maybe null
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * @return the internal id passed in the constructor, always != null, should be unique for each
     *         control
     */
    public String getId()
    {
        return id;
    }

    /**
     * creates the swt control associated with this widget. Parent must have been set before.
     * 
     * @param formToolkit formToolkit to use for creating the swt control. For most widgets the
     *            FormToolkit must implement {@link IFormToolkitEx} interface which enhances the
     *            toolkit with all creation methods needed by RCPForms widgets.
     */
    public void createUI(FormToolkit formToolkit)
    {
        doCreateUI(formToolkit);
    }

    /**
     * does the pure widget creation using createWrappedWidget, initializes the formtoolkit but does
     * not care about children
     * 
     * @param formToolkit toolkit to use
     */
    protected void doCreateUI(FormToolkit formToolkit)
    {
        Validate.notNull(formToolkit);
        this.formToolkit = formToolkit;
        wrappedWidget = createWrappedWidget(formToolkit);
        WidgetUtil.ensureValid(this);
        if (wrappedWidget != null)
        {
            wrappedWidget.addDisposeListener(disposeListener);
        }
    }

    /**
     * number of layout cells (controls) occupied by this widget, cell spans are not accounted for.
     * usually 2 for labeled widgets, otherwise 1
     * 
     * @return number of layout cells
     */
    public int getNumberOfControls()
    {
        return 1;
    }

    /**
     * for some widgets there is no create method in FormToolkit. These need an extended formtoolkit
     * which can be retrieved with this method.
     * 
     * @return extended form toolkit
     * @throws IllegalArgumentException if the toolkit passed does not implement IFormToolkitEx
     */
    public final IFormToolkitEx getFormToolkitEx()
    {
        Validate.notNull(formToolkit,
                "createUI has not been called yet, thus form toolkit not available"); //$NON-NLS-1$
        Validate
                .isTrue(
                        formToolkit instanceof IFormToolkitEx,
                        "The widget you created needs an extended FormToolkit. Make sure your FormToolkit implements IFormToolkitEx."); //$NON-NLS-1$
        return (IFormToolkitEx) formToolkit;
    }

    /**
     * retrieves the formtoolkit this widget was created with. colors, fonts etc. must be maintained
     * in the formtoolkit.
     * 
     * @return form toolkit
     * @throws IllegalArgumentException if the toolkit passed does not implement IFormToolkitEx
     */
    protected final FormToolkit getFormToolkit()
    {
        Validate.notNull(formToolkit,
                "createUI has not been called yet, thus form toolkit not available"); //$NON-NLS-1$
        return formToolkit;
    }

    /**
     * @return swt style which was passed in constructor, ATTENTION: maybe SWT.DEFAULT; if you want
     *         the resolved default style, use getSWTWidget().getStyle()
     * @see org.eclipse.swt.widgets.Widget#getStyle()
     */
    public int getStyle()
    {
        return style;
    }

}
