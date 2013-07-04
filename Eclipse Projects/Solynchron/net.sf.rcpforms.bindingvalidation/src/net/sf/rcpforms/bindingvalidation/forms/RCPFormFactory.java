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
// Created on 27.04.2008

package net.sf.rcpforms.bindingvalidation.forms;

import java.util.HashMap;
import java.util.Map;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.toolkit.RCPTableFormToolkit;
import net.sf.rcpforms.common.util.Validate;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.internal.forms.MessageManager;

/**
 * some helper methods to init and start a form without having an explicit form concept
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class RCPFormFactory
{

    private static RCPFormFactory instance = null;

    /** manages form toolkits per display */
    private Map<Display, FormToolkit> toolkitMap = new HashMap<Display, FormToolkit>();

    public static RCPFormFactory getInstance()
    {
        if (instance == null)
        {
            instance = new RCPFormFactory();
        }
        return instance;
    }

    /**
     * @param display display to retrieve toolkit for
     * @return the singleton FormToolkit used to create SWT widgets for the given display
     */
    public FormToolkit getToolkit(Display display)
    {
        FormToolkit toolkit = toolkitMap.get(display);
        if (toolkit == null)
        {
            toolkit = new RCPTableFormToolkit(display);
            toolkitMap.put(display, toolkit);
        }
        return toolkit;
    }

    /**
     * create a validation manager; this must be overridden if you use specific update strategies in
     * your project
     * 
     * @param title title of the form, used in ValidationManager log entries to indicate which form
     *            it manages
     * @return ValidationManager useful for Beans
     */
    public ValidationManager createBindingAndValidationManager(String title)
    {
        return new ValidationManager("Form " + title); //$NON-NLS-1$
    }

    /**
     * convenience method to create a form displaying a form part and binding it to a model
     */
    public ManagedForm createForm(String title, Composite parent, RCPFormPart formPart,
                                  final Object model)
    {
        return createForm(title, parent, new RCPFormPart[]{formPart}, new Object[]{model}, null,
                null);
    }

    /**
     * convenience method to create a form displaying a form part and binding it to a model. NOTE:
     * This form is not supposed to have a header section -> no title area available. Markers were
     * only applied to the fields as decorations but not in the header.
     */
    public ScrolledForm createSimpleForm(Composite parent, RCPFormPart formPart, final Object model)
    {
        return createSimpleForm(parent, new RCPFormPart[]{formPart}, new Object[]{model}, null,
                null);
    }

    /**
     * convenience method to create a form displaying n form parts and binding it to n corresponding
     * models
     * 
     * @param validationManager binding and validation manager to use for this form
     * @param initializer will be called after all form parts are created and bound
     */
    public ManagedForm createForm(final String title, final Composite parent,
                                  final RCPFormPart[] formParts, final Object[] models,
                                  final ValidationManager validationManager,
                                  final Runnable initializer)
    {
        Validate.isTrue(parent != null);
        Validate.isTrue(formParts != null);
        Validate.isTrue(models != null);
        Validate.isTrue(formParts.length == models.length,
                "FormParts and Models must correspond 1:1"); //$NON-NLS-1$
        ManagedForm managedForm = null;
        try
        {
            managedForm = createManagedForm(parent, title, getToolkit(Display.getCurrent()));
            final FormToolkit toolkit = getToolkit(parent.getDisplay());
            final ManagedForm mf = managedForm;
            Realm.runWithDefault(Realm.getDefault(), new Runnable()
            {
                public void run()
                {
                    ValidationManager vm;
                    if (validationManager == null)
                    {
                        vm = createBindingAndValidationManager(title);
                    }
                    else
                    {
                        vm = validationManager;
                    }
                    vm.initialize(mf.getMessageManager());
                    for (int i = 0; i < formParts.length; i++)
                    {
                        formParts[i].createUI(toolkit, mf.getForm().getBody());
                        vm.bindPart(formParts[i], models[i]);
                    }
                    // call initializer if one was passed
                    if (initializer != null)
                    {
                        initializer.run();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return managedForm;
    }

    /**
     * convenience method to create a form displaying n form parts and binding it to n corresponding
     * models
     * 
     * @param validationManager binding and validation manager to use for this form
     * @param initializer will be called after all form parts are created and bound
     */
    @SuppressWarnings("restriction")
    public ScrolledForm createSimpleForm(final Composite parent, final RCPFormPart[] formParts,
                                         final Object[] models,
                                         final ValidationManager validationManager,
                                         final Runnable initializer)
    {
        Validate.isTrue(parent != null);
        Validate.isTrue(formParts != null);
        Validate.isTrue(models != null);
        Validate.isTrue(formParts.length == models.length,
                "FormParts and Models must correspond 1:1"); //$NON-NLS-1$
        ScrolledForm scrolledForm = null;
        try
        {
            scrolledForm = createScrolledForm(parent, null, getToolkit(Display.getCurrent()));
            final FormToolkit toolkit = getToolkit(parent.getDisplay());
            final ScrolledForm sf = scrolledForm;
            final MessageManager mm = new MessageManager(sf);
            Realm.runWithDefault(Realm.getDefault(), new Runnable()
            {
                public void run()
                {
                    ValidationManager vm;
                    if (validationManager == null)
                    {
                        vm = createBindingAndValidationManager(sf.toString());
                    }
                    else
                    {
                        vm = validationManager;
                    }
                    vm.initialize(mm);
                    for (int i = 0; i < formParts.length; i++)
                    {
                        formParts[i].createUI(toolkit, sf.getForm().getBody());
                        vm.bindPart(formParts[i], models[i]);
                    }
                    // call initializer if one was passed
                    if (initializer != null)
                    {
                        initializer.run();
                    }
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return scrolledForm;
    }

    public ManagedForm createManagedForm(Composite parent, String title, FormToolkit toolkit)
    {
        ManagedForm managedForm = null;
        ScrolledForm form1 = new ScrolledForm(parent, SWT.V_SCROLL | SWT.H_SCROLL
                | Window.getDefaultOrientation())
        {
            // HACK to avoid loading of images which needs osgi
            // runtime in Eclipse 3.1, so this can be used with swt too.
            // in 3.4 this bug was fixed
            @Override
            public void setMessage(String newMessage, int newType, IMessage[] messages)
            {
                try
                {
                    super.setMessage(newMessage, newType, messages);
                }
                catch (RuntimeException ex)
                {
                    // if this does not work, set message without image
                    super.setMessage(newMessage, 0, messages);
                }
                catch (NoClassDefFoundError ex)
                {
                    // using 3.3 libraries "standalone" a NoClassDefFoundError
                    // could be thrown
                    super.setMessage(newMessage, 0, messages);
                }
                catch (ExceptionInInitializerError eiie)
                {
                    // using 3.3 libraries "standalone" an
                    // ExceptionInInitializeError could be thrown
                    super.setMessage(newMessage, 0, messages);
                }
            }
        };
        form1.setExpandHorizontal(true);
        form1.setExpandVertical(true);
        form1.setBackground(toolkit.getColors().getBackground());
        form1.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        form1.setFont(JFaceResources.getHeaderFont());
        final ScrolledForm form = form1;
        managedForm = new ManagedForm(toolkit, form);
        toolkit.decorateFormHeading(form.getForm());
        form.setBackground(toolkit.getColors().getBackground());
        form.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        form.setFont(JFaceResources.getHeaderFont());
        if (title != null)
        {
            form.setText(title);
        }
        return managedForm;
    }

    public ScrolledForm createScrolledForm(Composite parent, String title, FormToolkit toolkit)
    {
        ScrolledForm form = new ScrolledForm(parent, SWT.V_SCROLL | SWT.H_SCROLL
                | Window.getDefaultOrientation())
        {
            // HACK to avoid loading of images which needs osgi
            // runtime in Eclipse 3.1, so this can be used with swt too.
            // in 3.4 this bug was fixed
            @Override
            public void setMessage(String newMessage, int newType, IMessage[] messages)
            {
                try
                {
                    super.setMessage(newMessage, newType, messages);
                }
                catch (RuntimeException ex)
                {
                    // if this does not work, set message without image
                    super.setMessage(newMessage, 0, messages);
                }
                catch (NoClassDefFoundError ex)
                {
                    // using 3.3 libraries "standalone" a NoClassDefFoundError
                    // could be thrown
                    super.setMessage(newMessage, 0, messages);
                }
                catch (ExceptionInInitializerError eiie)
                {
                    // using 3.3 libraries "standalone" an
                    // ExceptionInInitializeError could be thrown
                    super.setMessage(newMessage, 0, messages);
                }
            }
        };
        form.setExpandHorizontal(true);
        form.setExpandVertical(true);
        form.setBackground(toolkit.getColors().getBackground());
        form.setForeground(toolkit.getColors().getColor(IFormColors.TITLE));
        form.setFont(JFaceResources.getHeaderFont());
        if (title != null)
        {
            form.setText(title);
        }
        return form;
    }

    public void startTestShell(final String title, final RCPFormPart part, final Object model)
    {
        startTestShell(title, new RCPFormPart[]{part}, new Object[]{model});
    }

    public void startTestShell(final String title, final RCPFormPart[] parts, final Object[] models)
    {
        startTestShell(title, parts, models, null);
    }

    /**
     * convenience test method to start a shell with a form part
     */
    public void startTestShell(final String title, final RCPFormPart[] parts,
                               final Object[] models, final Runnable initializer)
    {

        final Display display = Display.getDefault();
        final Shell shell = new Shell(display);
        // shell.setSize(900, 600);
        shell.setLayout(new FillLayout());

        org.eclipse.core.databinding.observable.Realm.runWithDefault(SWTObservables
                .getRealm(Display.getDefault()), new Runnable()
        {
            public void run()
            {
                try
                {
                    // create simple form without a managed form and no
                    // header section which displays errors.
                    // createSimpleForm(shell, parts, models, null, initializer);
                    createForm(title, shell, parts, models, null, initializer);
                    shell.open();
                    shell.pack(true);
                }
                catch (Throwable ex)
                {
                    ex.printStackTrace();
                }
                while (!shell.isDisposed())
                {
                    if (!display.readAndDispatch())
                        display.sleep();
                }

                display.dispose();
            }
        });

    }
}