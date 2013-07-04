/*******************************************************************************
 * Copyright (c) 2008 Siemens AG
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kai Toedter - initial API and implementation
 ******************************************************************************/

package net.sf.rcpforms.comparison.plain;

import java.util.HashMap;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.ISWTObservable;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BindingDecorationExample2 {

    private static HashMap<Control, ControlDecoration> decoratorMap = new HashMap<Control, ControlDecoration>();

    private static class StringRequiredValidator implements
            IValidator {

        private final String errorText;

        public StringRequiredValidator(String errorText) {
            super();
            this.errorText = errorText;
        }

        public IStatus validate(Object value) {
            if (value instanceof String) {
                String text = (String) value;
                if (text.trim().length() == 0) {
                    return ValidationStatus
                            .error(errorText);
                }
            }
            return Status.OK_STATUS;
        }
    }

    private static void createControlDecoration(
            Control control, String hoverText) {
        ControlDecoration controlDecoration = new ControlDecoration(
                control, SWT.LEFT | SWT.TOP);
        controlDecoration.setDescriptionText(hoverText);
        FieldDecoration fieldDecoration = FieldDecorationRegistry
                .getDefault().getFieldDecoration(
                        FieldDecorationRegistry.DEC_ERROR);
        controlDecoration.setImage(fieldDecoration
                .getImage());
        decoratorMap.put(control, controlDecoration);
    }

    public static void main(String[] args) {
        final Display display = new Display();
        Realm.runWithDefault(SWTObservables
                .getRealm(display), new Runnable() {
            public void run() {
                Shell shell = new Shell(display);
                shell
                        .setText("Data Binding Validation with Decorators");

                GridLayout layout = new GridLayout(2, false);
                layout.horizontalSpacing = 10;
                shell.setLayout(layout);

                Person person = new Person("Kai", "Toedter");

                Label firstNamelabel = new Label(shell,
                        SWT.NONE);
                firstNamelabel.setText("First Name:");
                Text firstNameText = new Text(shell,
                        SWT.BORDER);
                firstNameText.setLayoutData(new GridData(
                        GridData.FILL_HORIZONTAL));

                createControlDecoration(firstNameText,
                        "Please enter your first name");

                Label lastNamelabel = new Label(shell,
                        SWT.NONE);
                lastNamelabel.setText("Last Name:");

                Text lastNameText = new Text(shell,
                        SWT.BORDER);
                lastNameText.setLayoutData(new GridData(
                        GridData.FILL_HORIZONTAL));
                createControlDecoration(lastNameText,
                        "Please enter your last name");

                new Label(shell, SWT.NONE)
                        .setText("Validation Error:");

                Label validationErrorLabel = new Label(
                        shell, SWT.NONE);
                validationErrorLabel
                        .setLayoutData(new GridData(
                                GridData.FILL_HORIZONTAL));

                GridDataFactory.swtDefaults().hint(150,
                        SWT.DEFAULT).applyTo(
                        validationErrorLabel);

                final DataBindingContext dataBindingContext = new DataBindingContext();

                dataBindingContext
                        .bindValue(
                                SWTObservables.observeText(
                                        firstNameText,
                                        SWT.Modify),
                                PojoObservables
                                        .observeValue(
                                                person,
                                                "firstName"),
                                new UpdateValueStrategy()
                                        .setAfterConvertValidator(new StringRequiredValidator(
                                                "Please enter first name")),
                                null);

                dataBindingContext
                        .bindValue(
                                SWTObservables.observeText(
                                        lastNameText,
                                        SWT.Modify),
                                PojoObservables
                                        .observeValue(
                                                person,
                                                "lastName"),
                                new UpdateValueStrategy()
                                        .setAfterConvertValidator(new StringRequiredValidator(
                                                "Please enter last name")),
                                null);

                AggregateValidationStatus aggregateValidationStatus = new AggregateValidationStatus(
                        dataBindingContext.getBindings(),
                        AggregateValidationStatus.MAX_SEVERITY);

                aggregateValidationStatus
                        .addChangeListener(new IChangeListener() {
                            public void handleChange(
                                    ChangeEvent event) {
                                for (Object o : dataBindingContext
                                        .getBindings()) {
                                    Binding binding = (Binding) o;
                                    IStatus status = (IStatus) binding
                                            .getValidationStatus()
                                            .getValue();
                                    Control control = null;
                                    if (binding.getTarget() instanceof ISWTObservable) {
                                        ISWTObservable swtObservable = (ISWTObservable) binding
                                                .getTarget();
                                        control = (Control) swtObservable
                                                .getWidget();
                                    }
                                    ControlDecoration decoration = decoratorMap
                                            .get(control);
                                    if (decoration != null) {
                                        if (status.isOK()) {
                                            decoration
                                                    .hide();
                                        } else {
                                            decoration
                                                    .setDescriptionText(status
                                                            .getMessage());
                                            decoration
                                                    .show();
                                        }
                                    }
                                }
                            }
                        });

                dataBindingContext.bindValue(SWTObservables
                        .observeText(validationErrorLabel),
                        aggregateValidationStatus, null,
                        null);

                shell.pack();
                shell.open();
                while (!shell.isDisposed()) {
                    if (!display.readAndDispatch()) {
                        display.sleep();
                    }
                }
            }
        });
        display.dispose();
    }

}