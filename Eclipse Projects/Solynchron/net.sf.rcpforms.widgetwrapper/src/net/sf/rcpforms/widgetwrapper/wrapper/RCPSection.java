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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

/**
 * wraps an RCPSection and automatically creates the section composite. The wrapped widget is the
 * section and {@link #getClientComposite()} returns the client composite which is the parent of
 * child RCPWidgets.
 * 
 * @author Marco van Meegen
 */
public class RCPSection extends RCPComposite
{

    public RCPSection(String labelText)
    {
        this(labelText, SWT.DEFAULT);
    }

    public RCPSection(String labelText, int style)
    {
        super(labelText, style);
    }

    private Composite clientComposite;

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        Section result;
        if (getStyle() == SWT.DEFAULT)
        {
            result = getFormToolkitEx().createSection(getSWTParent());
        }
        else
        {
            result = getFormToolkitEx().createSection(getSWTParent(), getStyle());
        }
        clientComposite = formToolkit.createComposite(result);
        result.setClient(clientComposite);
        result.setText(getLabel());
        return result;
    }

    public Section getSWTSection()
    {
        return getTypedWidget();
    }

    @Override
    public Composite getClientComposite()
    {
        return clientComposite;
    }

    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        hookListeners();
    }

    /**
     * listener needed to reflow the form if the section is expanded or closed
     */
    protected void hookListeners()
    {
        Section section = getSWTSection();
        if ((section.getExpansionStyle() & Section.TWISTIE) != 0
                || (section.getExpansionStyle() & Section.TREE_NODE) != 0)
        {
            final ExpansionAdapter expansionListener = new ExpansionAdapter()
            {
                @Override
                public void expansionStateChanged(ExpansionEvent e)
                {
                    ScrolledForm form = getScrolledForm();
                    if (form != null)
                    {
                        form.reflow(false);
                    }
                }
            };
            section.addExpansionListener(expansionListener);
            // unhook expansion listener on dispose; this is super-safe, should not be needed
            section.addDisposeListener(new DisposeListener()
            {
                public void widgetDisposed(DisposeEvent e)
                {
                    ((Section) e.widget).removeExpansionListener(expansionListener);
                }

            });

        }
    }

    @Override
    public void setLayout(Layout layout)
    {
        clientComposite.setLayout(layout);
    }
}
