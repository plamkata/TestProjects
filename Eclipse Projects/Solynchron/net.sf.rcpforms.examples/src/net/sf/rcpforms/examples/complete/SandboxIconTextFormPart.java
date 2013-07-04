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

package net.sf.rcpforms.examples.complete;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.customwidgets.IconText;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPIconText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * FormPart for IconText in sandbox example.
 * 
 * @author Remo Loetscher
 */
public class SandboxIconTextFormPart extends RCPFormPart
{
    private RCPSection mainSection;

    private RCPIconText iconText;

    private RCPSimpleButton resetTitle, addTitle, hideTitle;

    private RCPSimpleButton resetText, addText, hideText;

    private String title = "RCPForms provides \"IconText\"";

    private String text = "Lorem ipsum dolor sit amet, consectetuer sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    /**
     * Constructor for KontodatenViewModel.
     */
    public SandboxIconTextFormPart()
    {
    }

    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // create wrappers

        mainSection = new RCPSection("iconified text sample");

        resetTitle = new RCPSimpleButton("Reset Title");
        addTitle = new RCPSimpleButton("Append Title");
        hideTitle = new RCPSimpleButton("Hide Title");
        resetText = new RCPSimpleButton("Reset Text");
        addText = new RCPSimpleButton("Append Text");
        hideText = new RCPSimpleButton("Hide Text");

        iconText = new RCPIconText(Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION));

        // build layout
        GridBuilder builder = new GridBuilder(toolkit, parent, 1);
        GridBuilder widgetBuilder = builder.addContainer(mainSection, 3);

        widgetBuilder.add(resetTitle);
        widgetBuilder.add(addTitle);
        widgetBuilder.add(hideTitle);
        widgetBuilder.add(resetText);
        widgetBuilder.add(addText);
        widgetBuilder.add(hideText);
        widgetBuilder.addLine(iconText);

        this.initListener();
        IconText it = iconText.getTypedWidget();
        it.setTitle(title);
        it.setText(text);
    }

    private void initListener()
    {
        resetTitle.getSWTButton().addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                IconText it = iconText.getTypedWidget();
                it.setTitle(title);
            }

        });
        addTitle.getSWTButton().addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                IconText it = iconText.getTypedWidget();
                it.setTitle(title + " _-*-_-*-_-*-_-*-_ " + title);
            }

        });

        hideTitle.getSWTButton().addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                IconText it = iconText.getTypedWidget();
                it.setTitle("");
            }

        });

        resetText.getSWTButton().addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                IconText it = iconText.getTypedWidget();
                it.setText(text);
            }

        });
        addText.getSWTButton().addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                IconText it = iconText.getTypedWidget();
                it.setText(text + text);
            }

        });

        hideText.getSWTButton().addSelectionListener(new SelectionAdapter()
        {

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                IconText it = iconText.getTypedWidget();
                it.setText("");
            }

        });

    }

    public void bind(ValidationManager context, Object dataModel)
    {
        // nothing to bind here
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        mainSection.setState(state, value);

    }

    public void setFocus()
    {
        mainSection.getSWTControl().setFocus();
    }

    public static void main(String[] args)
    {

        Object model = new SimpleBean();
        RCPFormPart part = new SandboxIconTextFormPart();

        RCPFormFactory.getInstance().startTestShell("SandboxIconTextFormPart", part, model);

    }
}