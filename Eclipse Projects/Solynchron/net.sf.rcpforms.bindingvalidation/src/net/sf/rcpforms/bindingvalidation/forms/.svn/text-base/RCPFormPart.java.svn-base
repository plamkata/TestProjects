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

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * reusable unit of user interface elements living in a {@link RCPFormFactory}. A Form part contains
 * ui elements which will be bound to a presentation model. A Form part has a dirty state and a
 * validation state.
 * <p>
 * TODO: explain responsibility of RCPFormPart in form lifecycle, status and validation.
 * 
 * @author Marco van Meegen
 */
public abstract class RCPFormPart
{
    /**
     * subclass must bind widgets to the model using the given ValdidationManager
     */
    public abstract void bind(ValidationManager bm, Object modelBean);

    /**
     * create the UI for this formpart in the given parent; usually the parent is a form body, but
     * no assumptions must be made about this.
     */
    public abstract void createUI(FormToolkit toolkit, Composite parent);

    /**
     * parts should implement state handling methods, usually delegating to their top level control,
     * e.g. RCPSection or RCPComposite
     * 
     * @param state state to modify
     * @param value new value of the given state
     */
    public abstract void setState(EControlState state, boolean value);
}
