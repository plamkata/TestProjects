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

package net.sf.rcpforms.widgetwrapper.wrapper.radio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

/**
 * This object decorates a bunch of SWT.RADIO buttons and provides saner selection semantics than
 * you get by default with those radio buttons.
 * <p>
 * It associates each button with an arbitrary value (model element) and automatically maps the
 * selected radio button to the model element and vice versa via the methods
 * {@link #setSelection(Object)}, {@link #getSelection()}.
 * <p>
 * Selections can be vetoed by using
 * {@link #addVetoableSelectionListener(VetoableSelectionListener)}.
 * <p>
 * based on jface databinding example.
 */
public class RadioGroupManager
{

    private final IRadioButton[] buttons;

    private final Object[] values;

    private List<VetoableSelectionListener> widgetChangeListeners = new LinkedList<VetoableSelectionListener>();

    private List<SelectionListener> widgetSelectedListeners = new ArrayList<SelectionListener>();

    private IRadioButton selectedButton = null;

    private IRadioButton potentialNewSelection = null;

    /**
     * (Non-API) Interface IRadioButton. A duck interface that is used internally by RadioGroup and
     * by RadioGroup's unit tests.
     */
    private static interface IRadioButton
    {
        void setData(String string, Object object);

        void addSelectionListener(SelectionListener selectionListener);

        void setSelection(boolean b);

        boolean getSelection();

        boolean isFocusControl();

        String getText();

        void setText(String string);

        void notifyListeners(int eventType, Event object);
    }

    /**
     * associates the given radio buttons with the given values (model objects). The Button objects
     * must have been created with the SWT.RADIO style bit set, and they must all be in the same
     * Composite.
     * 
     * @param radioButtons an array of {@link EWidgetType#BUTTON} widgets to wrap; controls must
     *            have been created already.
     * @param values Object[] an array of same length as radioButtons of model objects corresponding
     *            to the value of each radio button. values[i] will be returned by
     *            {@link #getSelection()} if button[i] was selected. No null values allowed.
     */
    public RadioGroupManager(RCPSimpleButton[] radioButtons, Object[] values)
    {
        IRadioButton[] buttons = new IRadioButton[radioButtons.length];
        Validate.isTrue(radioButtons.length > 0, "A RadioGroup must manage at least one Button"); //$NON-NLS-1$
        Validate.isTrue(radioButtons.length == values.length);
        for (RCPSimpleButton radioButton : radioButtons)
        {
            Validate.isTrue(radioButton != null);
        }
        for (int i = 0; i < buttons.length; i++)
        {
            Validate.isTrue(radioButtons[i].getTypedWidget() != null);
            assert (radioButtons[i].getStyle() & SWT.RADIO) != 0 : "RCPSimpleButton[" + i //$NON-NLS-1$
                    + "] has not style SWT.RADIO"; //$NON-NLS-1$
            if (!DuckType.instanceOf(IRadioButton.class, radioButtons[i].getTypedWidget()))
            {
                throw new IllegalArgumentException("A radio button was not passed"); //$NON-NLS-1$
            }
            buttons[i] = (IRadioButton) DuckType.implement(IRadioButton.class, radioButtons[i]
                    .getTypedWidget());
            buttons[i].setData(Integer.toString(i), new Integer(i));
            buttons[i].addSelectionListener(selectionListener);
        }
        this.buttons = buttons;
        this.values = values;
    }

    /**
     * Returns the model object corresponding to the currently-selected radio button or null if no
     * radio button is selected.
     * 
     * @return the object corresponding to the currently-selected radio button or null if no radio
     *         button is selected.
     */
    public Object getSelection()
    {
        int selectionIndex = getSelectionIndex();
        if (selectionIndex < 0)
            return null;
        return values[selectionIndex];
    }

    /**
     * Sets the selected radio button to the radio button whose model object equals() the object
     * specified by newSelection. If !newSelection.equals() any model object managed by this radio
     * group, deselects all radio buttons.
     * 
     * @param newSelection A model object corresponding to one of the model objects associated with
     *            one of the radio buttons.
     */
    public void setSelection(Object newSelection)
    {
        deselectAll();
        for (int i = 0; i < values.length; i++)
        {
            if (values[i].equals(newSelection))
            {
                setSelection(i);
                return;
            }
        }
    }

    private SelectionListener selectionListener = new SelectionListener()
    {
        public void widgetDefaultSelected(SelectionEvent e)
        {
            widgetSelected(e);
        }

        public void widgetSelected(SelectionEvent e)
        {
            potentialNewSelection = getButton(e);
            if (!potentialNewSelection.getSelection())
            {
                return;
            }
            if (potentialNewSelection.equals(selectedButton))
            {
                return;
            }

            if (fireWidgetChangeSelectionEvent(e))
            {
                selectedButton = potentialNewSelection;
                fireWidgetSelectedEvent(e);
            }
        }

        private IRadioButton getButton(SelectionEvent e)
        {
            // If the actual IRadioButton is a test fixture, then the test
            // fixture can't
            // set e.widget, so the button object will be in e.data instead and
            // a dummy
            // Widget will be in e.widget.
            if (e.data != null)
            {
                return (IRadioButton) e.data;
            }
            return (IRadioButton) DuckType.implement(IRadioButton.class, e.widget);
        }
    };

    protected boolean fireWidgetChangeSelectionEvent(SelectionEvent e)
    {
        for (Iterator<VetoableSelectionListener> listenersIter = widgetChangeListeners.iterator(); listenersIter
                .hasNext();)
        {
            VetoableSelectionListener listener = listenersIter.next();
            listener.canWidgetChangeSelection(e);
            if (!e.doit)
            {
                rollbackSelection();
                return false;
            }
        }
        return true;
    }

    private void rollbackSelection()
    {
        Display.getCurrent().asyncExec(new Runnable()
        {
            public void run()
            {
                potentialNewSelection.setSelection(false);
                if (selectedButton != null)
                {
                    selectedButton.setSelection(true);
                }
            }
        });
    }

    /**
     * Adds the listener to the collection of listeners who will be notified when the receiver's
     * selection is about to change, by sending it one of the messages defined in the
     * <code>VetoableSelectionListener</code> interface.
     * <p>
     * <code>widgetSelected</code> is called when the selection changes.
     * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
     * </p>
     * 
     * @param listener the listener which should be notified
     * @exception IllegalArgumentException <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * @exception SWTException <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     * @see VetoableSelectionListener
     * @see #removeVetoableSelectionListener
     * @see SelectionEvent
     */
    public void addVetoableSelectionListener(VetoableSelectionListener listener)
    {
        widgetChangeListeners.add(listener);
    }

    /**
     * Removes the listener from the collection of listeners who will be notified when the
     * receiver's selection is about to change.
     * 
     * @param listener the listener which should no longer be notified
     * @exception IllegalArgumentException <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * @exception SWTException <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     * @see VetoableSelectionListener
     * @see #addVetoableSelectionListener
     */
    public void removeVetoableSelectionListener(VetoableSelectionListener listener)
    {
        widgetChangeListeners.remove(listener);
    }

    protected void fireWidgetSelectedEvent(SelectionEvent e)
    {
        for (Iterator<SelectionListener> listenersIter = widgetSelectedListeners.iterator(); listenersIter
                .hasNext();)
        {
            SelectionListener listener = listenersIter.next();
            listener.widgetSelected(e);
        }
    }

    protected void fireWidgetDefaultSelectedEvent(SelectionEvent e)
    {
        fireWidgetSelectedEvent(e);
    }

    /**
     * Adds the listener to the collection of listeners who will be notified when the receiver's
     * selection changes, by sending it one of the messages defined in the
     * <code>SelectionListener</code> interface.
     * <p>
     * <code>widgetSelected</code> is called when the selection changes.
     * <code>widgetDefaultSelected</code> is typically called when an item is double-clicked.
     * </p>
     * 
     * @param listener the listener which should be notified
     * @exception IllegalArgumentException <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * @exception SWTException <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     * @see SelectionListener
     * @see #removeSelectionListener
     * @see SelectionEvent
     */
    public void addSelectionListener(SelectionListener listener)
    {
        widgetSelectedListeners.add(listener);
    }

    /**
     * Removes the listener from the collection of listeners who will be notified when the
     * receiver's selection changes.
     * 
     * @param listener the listener which should no longer be notified
     * @exception IllegalArgumentException <ul>
     *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
     *                </ul>
     * @exception SWTException <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     * @see SelectionListener
     * @see #addSelectionListener
     */
    public void removeSelectionListener(SelectionListener listener)
    {
        widgetSelectedListeners.remove(listener);
    }

    /**
     * Deselects all selected items in the receiver.
     * 
     * @exception SWTException <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     */
    public void deselectAll()
    {
        for (int i = 0; i < buttons.length; i++)
            buttons[i].setSelection(false);
        selectedButton = null;
    }

    /**
     * Returns the zero-relative index of the item which is currently selected in the receiver, or
     * -1 if no item is selected.
     * 
     * @return the index of the selected item or -1
     * @exception SWTException <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     */
    private int getSelectionIndex()
    {
        for (int i = 0; i < buttons.length; i++)
        {
            if (buttons[i].getSelection() == true)
            {
                return i;
            }
        }
        return -1;
    }

    /**
     * Selects the item at the given zero-relative index in the receiver. If the item at the index
     * was already selected, it remains selected. The current selection is first cleared, then the
     * new item is selected. Indices that are out of range are ignored.
     * 
     * @param index the index of the item to select
     * @exception SWTException <ul>
     *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created
     *                the receiver</li>
     *                </ul>
     * @see List#deselectAll()
     * @see List#select(int)
     */
    private void setSelection(int index)
    {
        if (index < 0 || index > buttons.length - 1)
        {
            return;
        }
        buttons[index].setSelection(true);
        selectedButton = buttons[index];

    }

}
