/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.widgetwrapper.wrapper;

import junit.framework.TestCase;

/**
 * tests creation order for radio group
 * 
 * @author Marco van Meegen
 */
public class RCPRadioGroup_Test extends TestCase
{

    /**
     * Test method for
     * {@link net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioGroup#getCreationOrder(int, int, boolean)}
     * .
     */
    public void testGetCreationOrder()
    {
        // if not vertical, nothing has to be permuted
        int[] result = RCPRadioGroup.getCreationOrder(3, 2, false);
        assertArrayEquals(new int[]{0, 1, 2}, result);

        // for vertical with remainder it should be rearranged
        result = RCPRadioGroup.getCreationOrder(5, 2, true);
        assertArrayEquals(new int[]{0, 3, 1, 4, 2}, result);

        // for vertical with no remainder it should be rearranged
        result = RCPRadioGroup.getCreationOrder(4, 2, true);
        assertArrayEquals(new int[]{0, 2, 1, 3}, result);

        // for vertical with one column nothing to do
        result = RCPRadioGroup.getCreationOrder(3, 1, true);
        assertArrayEquals(new int[]{0, 1, 2}, result);
    }

    private void assertArrayEquals(int[] expected, int[] result)
    {
        assertEquals(expected.length, result.length);
        for (int i = 0; i < result.length; i++)
        {
            assertEquals("Difference in Array at index " + i, expected[i], result[i]);
        }
    }

}
