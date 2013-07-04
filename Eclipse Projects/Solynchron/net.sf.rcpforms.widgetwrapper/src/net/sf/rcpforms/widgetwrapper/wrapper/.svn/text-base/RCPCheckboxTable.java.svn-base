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

import org.eclipse.swt.SWT;

/**
 * a compound of a label and a {@link RCPSimpleCheckboxTable}.
 * 
 * @author Marco van Meegen
 */
public class RCPCheckboxTable extends RCPTable
{
    public RCPCheckboxTable(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPCheckboxTable(String label, int style)
    {
        super(label, new RCPSimpleCheckboxTable(style));
    }
}
