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

package net.sf.rcpforms.emf;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.ConverterRegistry;
import net.sf.rcpforms.tablesupport.tables.TableUtil;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.swt.widgets.Display;

/**
 * Factory creating forms using EMF Databinding. A different Validation Manager is used for update
 * strategies and the model adapter for emf is installed when the singleton is created.
 * 
 * @author Marco van Meegen
 */
public class EMFFormFactory extends RCPFormFactory
{

    private static EMFFormFactory instance = null;

    public static EMFFormFactory getInstance()
    {
        if (instance == null)
        {
            instance = new EMFFormFactory();
            configureForEMF();
        }
        return instance;
    }

    /** configure rcpforms for usage with EMF bindings etc */
    private static void configureForEMF()
    {
        // register emf model adapter
        ModelAdapter.registerAdapter(new EMFModelAdapter());
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
        // TODO: use converter registry to extend bindings
        return new ValidationManager("Form " + title)
        {
            protected UpdateValueStrategy createModelToTargetUpdateStrategy()
            {
                return new EMFUpdateValueStrategy();
            }

            protected UpdateValueStrategy createTargetToModelUpdateStrategy()
            {
                // TODO: use central Converters and Validators factory with
                // defaults for
                // all widget subtypes
                return new EMFUpdateValueStrategy()
                {
                    @Override
                    protected IStatus doSet(final IObservableValue observableValue,
                                            final Object value)
                    {
                        final IStatus result = super.doSet(observableValue, value);
                        // update status asynchronously since this binding
                        // message has
                        // not yet been updated
                        Display.getCurrent().asyncExec(new Runnable()
                        {
                            public void run()
                            {
                                modelValueChanged(false, observableValue, value, result);
                            };
                        });
                        return result;
                    }
                };
            }
        };
    }
}
