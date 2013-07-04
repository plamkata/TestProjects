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

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.rcpforms.modeladapter.Messages;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.util.Validate;

import org.eclipse.core.runtime.IStatus;

/**
 * Validator for validation multiline texts with the different line length on the rows
 * 
 * @author Remo Loetscher
 */

public class AsymmetricMultilineValidator extends MethodValidator
{
    private String property;
    private int[] dimensions;

    /**
     * Provides a validator which will validate each lines length and number of all lines.
     * Line can have different lengths.
     * @param property flat property which should be validated
     * @param dimensions array length means number of rows and integer number in the array means allowed characters per line
     */
    
    public AsymmetricMultilineValidator(String property, int[] dimensions)
    {
        Validate.notNull(property);
        Validate.notNull(dimensions);
        Validate.isTrue(dimensions.length > 0);

        this.property = property;
        this.dimensions = dimensions;
    }
    
    @Override
    public IStatus validate(Object model)
    {
        IStatus result = ok();
        int numberOfNewlines = 0;
        String text = getTextFromModel(model);
        String[] lines = text == null ? new String[]{} : text.split(System.getProperty("line.separator")); //$NON-NLS-1$

        if(text != null)
            numberOfNewlines = newLineCounter(text);
        
        StringBuilder errorMessage = new StringBuilder();
        if(numberOfNewlines > dimensions.length - 1)
        {
            errorMessage.append(MessageFormat.format(Messages.getString("AsymmetricMultilineValidator.TooManyLines"), dimensions.length, numberOfNewlines + 1)); //$NON-NLS-1$
        }else
        {
            int row = 0;
            for(String lineText : lines)
            {
                int currentLineLength = lineText.length();
                if(currentLineLength > dimensions[row])
                {
                  //add newline if it's not the first line
                    if(errorMessage.length() > 0)
                    {
                        errorMessage.append("\n");//$NON-NLS-1$
                    }
                    
                    errorMessage.append(MessageFormat.format(Messages.getString("AsymmetricMultilineValidator.TooManyCharacterPerLine"), row + 1, currentLineLength, dimensions[row], currentLineLength - dimensions[row])); //$NON-NLS-1$
                    
                }
                ++row;
            }
        }
        //create Error message!
        if(errorMessage.length() > 0)
        {
            result = error(errorMessage.toString());                    
        }
        
        return result;
    }

    
    /**
     * counts newlines occurrence in a text
     * @param text
     * @return
     */
    private int newLineCounter(String text)
    {
        Pattern p = Pattern.compile(System.getProperty("line.separator")); //$NON-NLS-1$
        Matcher m = p.matcher(text);
        int count = 0;
        while(m.find())
        {
            ++count;
        }
        return count;
    }

    private String getTextFromModel(Object model)
    {
        String result = null;
        ModelAdapter modelAdapter = ModelAdapter.getAdapterForInstance(model);
        for (int i = 0; i < getProperties().length; i++)
        {
            String prop = (String) getProperties()[i];
            Object value = modelAdapter.getValue(model, prop);
            result = (String) value;
        }
        return result;
    }

    @Override
    public Object[] getProperties()
    {
        return new Object[]{property};
    }
    

    
}
