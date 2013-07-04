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

/**
 * represents a control state change in an rcp control. Used for parent chaining control states
 * 
 * @author Marco van Meegen
 */
public class StateChangeEvent
{
    private RCPControl source;

    private EControlState state;

    private Object oldState;

    private Object newState;

    /**
     * Constructor for StateChangeEvent
     * 
     * @param source source widget
     * @param state the state which changed
     * @param oldState old state
     * @param newState new state
     */
    StateChangeEvent(RCPControl source, EControlState state, Object oldState, Object newState)
    {
        this.source = source;
        this.state = state;
        this.oldState = oldState;
        this.newState = newState;
    }

    public RCPControl getSource()
    {
        return source;
    }

    public EControlState getState()
    {
        return state;
    }

    public Object getOldState()
    {
        return oldState;
    }

    public Object getNewState()
    {
        return newState;
    }

}
