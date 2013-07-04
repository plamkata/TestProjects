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
import org.eclipse.swt.widgets.Text;

/**
 * a compound of a label and a {@link RCPSimpleText}.
 * 
 * @author Marco van Meegen
 */
public class RCPText extends RCPLabeledControl<RCPSimpleText>
{
    public RCPText(String label)
    {
        this(label, SWT.DEFAULT);
    }

    public RCPText(String label, int style)
    {
        super(label, new RCPSimpleText(style));
    }

    public final Text getSWTText()
    {
        return getRCPControl().getSWTText();
    }

}
