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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.modeladapter.converter.RequiredValidator;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPToggleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.radio.RadioGroupManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * FormPart for Section <code>Sandbox3</code> of the Sandbox example.
 * <p>
 * Demonstrates:
 * <ul>
 * <li>a more complex layout
 * <li>state binding binding of controls
 * <li>using the builder to build nested composites
 * <li>using validators for date from < date to validation
 * <li>hierarchical state management: disabling a composite disables all nested fields and removes
 * required decoration
 * </ul>
 * 
 * @author Marco van Meegen
 */
public class Sandbox3FormPart extends RCPFormPart
{
    private RCPSection mainSection = new RCPSection("Sandbox3Section");

    private static final String BUTTON_DESC = " other sections";

    private RCPToggleButton m_enableButton = new RCPToggleButton("disable"
            + Sandbox3FormPart.BUTTON_DESC);

    private RCPToggleButton m_visibleButton = new RCPToggleButton("hide"
            + Sandbox3FormPart.BUTTON_DESC);

    /**
     * @return Returns the enableButton.
     */
    public RCPToggleButton getEnableButton()
    {
        return m_enableButton;
    }

    /**
     * @return Returns the enableButton.
     */
    public RCPToggleButton getVisibleButton()
    {
        return m_visibleButton;
    }

    private RCPRadioButton m_WieDomizilRadio = new RCPRadioButton("wie Domizil");

    private RCPRadioButton m_SeparateKorrespondenzadresseRadio = new RCPRadioButton(
            "neue Korrespondenzadresse");

    private RCPText m_Plz = new RCPText("PLZ (nested prop):");

    private RCPText m_City = new RCPText("Stadt (nested prop):");

    private RCPText m_Country = new RCPText("Land (nested prop):");

    private RCPText m_Von = new RCPText("von:");

    private RCPText m_Bis = new RCPText("bis:");

    private RCPText m_Strasse = new RCPText("Strasse/Hausnr:");

    private RCPSimpleText m_Hausnummer = new RCPSimpleText();

    private RCPComposite m_SeparateKorrespondenzAdresseComposite = new RCPComposite();

    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // build layout

        m_Country.setState(EControlState.MANDATORY, true);

        GridBuilder builder = new GridBuilder(toolkit, parent, 1);
        GridBuilder widgetFactory = builder.addContainer(mainSection, 1);

        // this layout uses a bit more complex grid layout, which mixes labeled widgets with
        // non-labeled ones.

        // this is not a named radio, since named radios create a filler eating up one grid cell,
        // which we dont want here
        widgetFactory.add(m_enableButton);
        m_enableButton.getSWTButton().addSelectionListener(new SelectionListener()
        {

            public void widgetDefaultSelected(SelectionEvent e)
            {
                // DoNothinHere
            }

            public void widgetSelected(SelectionEvent e)
            {
                ((Button) e.widget).setText((((Button) e.widget).getSelection() ? "Enable"
                                                                               : "Disable")
                        + Sandbox3FormPart.BUTTON_DESC);
            }

        });
        widgetFactory.add(m_visibleButton);
        m_visibleButton.getSWTButton().addSelectionListener(new SelectionListener()
        {

            public void widgetDefaultSelected(SelectionEvent e)
            {
                // DoNothinHere
            }

            public void widgetSelected(SelectionEvent e)
            {
                ((Button) e.widget).setText((((Button) e.widget).getSelection() ? "Show" : "Hide")
                        + Sandbox3FormPart.BUTTON_DESC);
            }

        });
        widgetFactory.add(m_WieDomizilRadio);
        widgetFactory.add(m_SeparateKorrespondenzadresseRadio);
        // you need a new widget factory for a composite since it uses a nested grid layout
        GridBuilder compositeFactory = widgetFactory.addContainer(
                m_SeparateKorrespondenzAdresseComposite, 5);
        compositeFactory.addSpan(m_Plz, SWT.DEFAULT, 4, 1);
        compositeFactory.addLine(m_City);
        compositeFactory.addLine(m_Country);
        compositeFactory.add(m_Von);
        m_Von.setState(EControlState.MANDATORY, true);
        compositeFactory.addSpan(m_Bis, SWT.DEFAULT, 2, 1);
        compositeFactory.addSpan(m_Strasse, SWT.DEFAULT, 2, 1);
        // no label needed so this is only a DlamWidget, not a labeled DlamWidget
        compositeFactory.add(m_Hausnummer);
    }

    @Override
    public void bind(ValidationManager context, Object dataModel)
    {
        AddressModel dm = (AddressModel) dataModel;
        context.bindValue(m_Plz, dataModel, AddressModel.P_NestedAddress,
                NestedAddressModel.P_ZipCode);
        context.bindValue(m_City, dataModel, AddressModel.P_NestedAddress,
                NestedAddressModel.P_City);
        context.bindValue(m_Country, dataModel, AddressModel.P_NestedAddress,
                NestedAddressModel.P_NestedCountry, NestedCountryModel.P_Country);
        context.bindValue(m_Von, dataModel, AddressModel.P_ValidFrom);
        context.bindValue(m_Bis, dataModel, AddressModel.P_ValidTo);
        context.bindValue(m_Strasse, dataModel, AddressModel.P_Street);
        context.bindValue(m_Hausnummer, dataModel, AddressModel.P_HouseNumber);

        // use special bind method to bind yes/no radio to boolean
        context.bindRadioToBoolean(m_SeparateKorrespondenzadresseRadio, m_WieDomizilRadio, dm, AddressModel.P_DifferentPostAddress);

        // bind enabled state to data model attribute separateKorrespondenzAdresse
        context.bindState(m_SeparateKorrespondenzAdresseComposite, EControlState.ENABLED, dm,
                AddressModel.P_DifferentPostAddress);

        context.addValidator(this, new RequiredValidator(AddressModel.P_NestedAddress + "."
                + NestedAddressModel.P_NestedCountry + "." + NestedCountryModel.P_Country));
    }

    public static void main(String[] args)
    {

        final AddressModel model = new AddressModel();
        final RCPFormPart part = new Sandbox3FormPart();
        model.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model changed: " + model);
            }
        });
        RCPFormFactory.getInstance().startTestShell("Sandbox3FormPart", part, model);
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        mainSection.setState(state, value);

    }

}
