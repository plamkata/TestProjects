/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.examples.app.multipageeditor;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.examples.complete.Sandbox2FormPart;
import net.sf.rcpforms.examples.complete.Sandbox3FormPart;
import net.sf.rcpforms.form.RCPForm;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;

public class SandboxAddressForm extends RCPForm
{

    public SandboxAddressForm(String title, RCPFormPart... parts)
    {
        super(title, parts);
    }

    @Override
    public void initializeUI()
    {
        // initializations which span multiple parts must be done here
        getSandbox3Part().getEnableButton().getSWTButton().addSelectionListener(
                new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent e)
                    {
                        getSandbox2Part().setState(EControlState.ENABLED,
                                !((Button) e.widget).getSelection());
                        getValidationManager().revalidate();
                    }
                });

        // initializations which span multiple parts must be done here
        getSandbox3Part().getVisibleButton().getSWTButton().addSelectionListener(
                new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent e)
                    {
                        getSandbox2Part().setState(EControlState.VISIBLE,
                                !((Button) e.widget).getSelection());
                        getValidationManager().revalidate();
                    }
                });
    }

    private Sandbox2FormPart getSandbox2Part()
    {
        return (Sandbox2FormPart) getPart(0);
    }

    private Sandbox3FormPart getSandbox3Part()
    {
        return (Sandbox3FormPart) getPart(1);
    }

}
