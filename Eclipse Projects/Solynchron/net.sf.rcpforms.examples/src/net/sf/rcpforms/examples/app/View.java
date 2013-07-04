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

package net.sf.rcpforms.examples.app;

import net.sf.rcpforms.examples.complete.SandboxStackForm;
import net.sf.rcpforms.form.RCPFormViewPart;

public class View extends RCPFormViewPart<SandboxStackForm>
{
    public static final String ID = "net.sf.rcpforms.examples.app.view";

    public View()
    {
        // create
        super(new SandboxStackForm());
        // and set input
        setInput(SandboxStackForm.createModels());
    }
}