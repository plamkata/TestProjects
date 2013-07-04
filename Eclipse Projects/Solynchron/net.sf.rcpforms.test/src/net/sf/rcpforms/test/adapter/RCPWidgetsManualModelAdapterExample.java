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

package net.sf.rcpforms.test.adapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.test.RCPWidgetsManualTest;
import net.sf.rcpforms.test.adapter.TestModel.Gender;

import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

/**
 * example form part demonstrating the use of different model adapters. UI is inherited from
 * standard example, but main method uses a different model to bind against.
 * 
 * @author Marco van Meegen
 */
public class RCPWidgetsManualModelAdapterExample extends RCPWidgetsManualTest
{

    public RCPWidgetsManualModelAdapterExample()
    {

    }

    @Override
    protected void createValidators(ValidationManager bm)
    {
        // do not create validators because parents validator needs a TestModel, not a map
    }

    public static void main(String[] args)
    {
        ModelAdapter.registerAdapter(new MapAdapter());
        org.eclipse.core.databinding.observable.Realm.runWithDefault(SWTObservables
                .getRealm(Display.getDefault()), new Runnable()
        {
            public void run()
            {

                final WritableMap model = new WritableMap();
                model.put(TestModel.P_Name, "Mustermann");
                model.put(TestModel.P_Age, 55);
                model.put(TestModel.P_Gender, Gender.MALE);
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.set(1980, 11, 24);
                model.put(TestModel.P_BirthDate, calendar.getTime());

                final RCPFormPart part = new RCPWidgetsManualModelAdapterExample();
                model.addMapChangeListener(new IMapChangeListener()
                {
                    public void handleMapChange(MapChangeEvent event)
                    {
                        System.out.println("Model changed: " + event);
                    }
                });
                RCPFormFactory.getInstance().startTestShell("RCPWidgtsManualModelAdapterExample",
                        part, model);
            }
        });

    }
}
