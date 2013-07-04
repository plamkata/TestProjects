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
// Created on 08.02.2008

package net.sf.rcpforms.widgetwrapper.wrapper;

/**
 * enumeration listing all available rcp control states.
 * <p>
 * Helper methods to encode the state bitwise into an integer.
 * 
 * @author Marco van Meegen
 */
public enum EControlState {
    VISIBLE(1), ENABLED(2), READONLY(4), MANDATORY(8), RECOMMENDED(16), OTHER(32);

    private int stateBit;

    EControlState(int flag)
    {
        this.stateBit = flag;
    }

    public static boolean isMember(EControlState controlState, int stateBits)
    {
        return (controlState.stateBit & stateBits) != 0;
    }

    public static int getFlags(EControlState... controlStates)
    {
        int result = 0;
        for (EControlState controlState : controlStates)
        {
            result |= controlState.stateBit;
        }
        return result;
    }

    /**
     * modifies the state in the given stateBits
     * 
     * @param state state to modify
     * @param set true: set the state, false: reset the state
     * @param stateBits stateBits to modify
     * @return modified stateBits
     */
    public static int modifyState(EControlState state, boolean set, int stateBits)
    {
        int result;
        if (set)
        {
            result = stateBits | state.stateBit;
        }
        else
        {
            result = (~state.stateBit) & stateBits;
        }
        return result;
    }
}
