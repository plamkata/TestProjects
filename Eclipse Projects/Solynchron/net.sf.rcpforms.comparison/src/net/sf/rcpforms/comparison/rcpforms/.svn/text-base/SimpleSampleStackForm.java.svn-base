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

package net.sf.rcpforms.comparison.rcpforms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.form.RCPForm;

/**
 * This is the main class of a simple example, demonstrating an overview of a small list of features
 * available in RCPForms. It is a recommended starting point to get an idea how to creat ui using
 * RCPForms.
 * <p>
 * 
 * @author Remo Loetscher
 */
public class SimpleSampleStackForm extends RCPForm
{

    /**
     * Constructor for SandboxStackForm
     */
    public SimpleSampleStackForm()
    {
        this(null);
    }

    public SimpleSampleStackForm(ValidationManager manager)
    {
        // create form with the given title and form parts
        super("RCPForms", manager, new PersonFormPart());
    }

    @Override
    public void initializeUI()
    {
        // in initializeUI the form is created,
        // input-independent listeners, validators and stuff should be
        // initialized here
    }

    /**
     * creates the models needed for the form and attaches listeners which echo changes to stdout
     * 
     * @return created models
     */
    public static Object[] createModels()
    {
        // and models
        final PersonDataModel personModel = new PersonDataModel();

        // add some listeners to check how databinding works
        personModel.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Person model changed: " + personModel);
            }
        });

        // set models as input
        // either create ui first and then set input, or if we use
        // startTestShell(),
        // we must first set the input
        Object[] models = new Object[]{personModel};
        return models;
    }

    /**
     * start the form as SWT application
     * 
     * @param args ignored
     */
    public static void main(String[] args)
    {
        // create form
        Object[] models = createModels();
        // create the form, no ui is created yet
        final SimpleSampleStackForm rcpForm = new SimpleSampleStackForm(new ValidationManager(SimpleSampleStackForm.class.getName()));
        // set input, since form is not created the input is not bound yet, but
        // saved for createUI()
        rcpForm.setInput(models);

        // convenience method, creates a shell and creates the form ui in the
        // shell
        // since an input has been set before, the form is bound to the model
        // and ready to go
        rcpForm.startTestShell();
    }

}
