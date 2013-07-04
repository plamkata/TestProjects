/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 ******************************************************************************
 */
package net.sf.rcpforms.modeladapter.converter;

/**
 * Validator for validation multiline texts with the same line length on all rows
 * 
 * @author Remo Loetscher
 */
public class SymmetricMultilineValidator extends AsymmetricMultilineValidator
{

    /**
     * Provides a validator which will validate each lines length and number of all lines.
     * all lines have the same length.
     * @param property flat property which should be validated
     * @param numberOfRows indicates how many 
     * @param numberOfColumns
     */
    
    public SymmetricMultilineValidator(String property, int numberOfRows, int numberOfColumns)
    {
        super(property, createArray(numberOfRows, numberOfColumns));
    }
    
    private static int[] createArray(int numberOfRows, int numberOfColumns)
    {
        int[] result = new int[numberOfRows];
        for(int i = 0; i < numberOfRows; ++i)
        {
            result[i] = numberOfColumns;
        }
        return result;
    }
}
