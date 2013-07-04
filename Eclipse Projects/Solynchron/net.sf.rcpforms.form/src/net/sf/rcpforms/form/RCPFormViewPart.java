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

package net.sf.rcpforms.form;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * Class RCPFormViewPart embeds an RCPForm into an Eclipse View.
 * <p>
 * Usage:
 * 
 * <pre>
 * public class View extends RCPFormViewPart&lt;SandboxStackForm&gt;
 * {
 *     public static final String ID = &quot;net.sf.rcpforms.examples.app.view&quot;;
 * 
 *     public View()
 *     {
 *         super(new SandboxStackForm());
 *     }
 * }
 * </pre>
 * 
 * You need to subclass the RCPFormViewPart for being able to declare an extension point.
 * 
 * @author Marco van Meegen
 */
public class RCPFormViewPart<T extends RCPForm> extends ViewPart
{
    private T form = null;

    /**
     * @return Returns the form.
     */
    public T getForm()
    {
        return form;
    }

    /**
     * creates a view part around a form
     */
    public RCPFormViewPart(T form)
    {
        this.form = form;
    }

    /**
     * create the form
     */
    public void createPartControl(Composite parent)
    {
        form.createUI(parent);
    }

    /**
     * Pass the focus request to the form
     */
    public void setFocus()
    {
        form.setFocus();
    }

    public void setInput(Object input)
    {
        form.setInput(input);
    }

    public Object getInput()
    {
        return form.getInput();
    }
}