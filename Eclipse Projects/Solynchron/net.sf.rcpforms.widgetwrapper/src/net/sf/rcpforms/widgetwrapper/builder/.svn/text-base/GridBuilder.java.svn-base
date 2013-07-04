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

package net.sf.rcpforms.widgetwrapper.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCompound;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPHyperlinkButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPHyperlinkLabel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPLabeledControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleLabel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget;
import net.sf.rcpforms.widgetwrapper.wrapper.radio.RadioGroupManager;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * builder which builds standard grid layouts using a n (default 2) column grid with label/control,
 * supporting nested composites.
 * <p>
 * GridData are created based on the WrappedWidget type and the columns already used.
 * <p>
 *
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class GridBuilder extends AbstractBuilder
{
    private int columns;

    private int currentColumnCounter = 0;

    private GridLayoutFactory layoutFactory;

    // cache span values to recalculate column count on the folowing line
    private int verticalSpanCache = 0;

    private int horizontalSpanCache = 0;

    //initialisation with default values
    private int avgCharWidth = 6;
    private int fontHeight = 15;

    public GridBuilder(FormToolkit formToolkit, Composite composite, int columns)
    {
        this(formToolkit, new GridLayoutFactory(), composite, columns);
    }

    public GridBuilder(FormToolkit formToolkit, RCPComposite composite, int columns)
    {
        this(formToolkit, new GridLayoutFactory(), composite, columns);
    }

    public GridBuilder(FormToolkit formToolkit, GridLayoutFactory layoutFactory,
                       Composite composite, int columns)
    {
        this(formToolkit, layoutFactory, new RCPComposite(composite), columns);
    }

    public GridBuilder(FormToolkit formToolkit, GridLayoutFactory layoutFactory,
                       RCPComposite composite, int columns)
    {
        super(formToolkit);
        this.layoutFactory = layoutFactory;
        //TODO should only columns > 0 be allowed?
        this.columns = columns;
        this.currentColumnCounter = 0;
        // set rcp parent this builder will add to
        this.currentParent = composite;
        this.formToolkit = formToolkit;
        // set layout of the composite to control by builder
        layoutFactory.applyLayout(composite, columns, false);
        setDebugInfo(composite);
        this.initCharacterHints(composite.getSWTControl());
    }

    private void initCharacterHints(Control control)
    {
        GC gc = null;
        try{
            gc = new GC(control);
            gc.setFont(control.getFont());
            FontMetrics fm = gc.getFontMetrics();
            avgCharWidth = fm.getAverageCharWidth();
            fontHeight = fm.getHeight();
        }finally{
            if (gc != null)
                gc.dispose();
        }
    }
    
    /**
     * calculates the width hint depending on font and number of characters
     * 
     * @param numberOfChars number of charactes to be used
     * @return width hint for the number of characters. Calculation depends on the font set on the root control of this builder
     */
    public int getWidthHint(int numberOfChars)
    {
        return this.avgCharWidth * numberOfChars;
    }
    
    /**
     * calculates the height hint depending on font and number of rows
     * 
     * @param numberOfRows number of charactes to be used
     * @return height hint for the number of rows. Calculation depends on the font set on the root control of this builder
     */
    public int getHeightHint(int numberOfRows)
    {
        return this.fontHeight * numberOfRows;
    }

    public final AbstractBuilder add(RCPControl control)
    {
        return addSpan(control, SWT.DEFAULT, 1, 1);
    }

    /**
     * Applies customised GridData to the RCPControl. ATTENTION: If control is a composite which has
     * more than one child, the GridData is applied to the control itself an to every child. Use
     * {@link GridLayoutFactory#createDefaultLayoutData()} to get a proper initialised GridData
     * object.
     *
     * @param control RCPControl to which the layout data should be applied
     * @param gridData
     * @return
     */
    public final AbstractBuilder add(RCPControl control, GridData gridData)
    {
        createControl(control);
        // calculate horizontal span in dependency to the number of controls.
        int spaceLeft = columns - currentColumnCounter;
        // track vertical span to recalculate right column count in the following lines
        if (gridData.verticalSpan > 1)
        {
            verticalSpanCache = gridData.verticalSpan - 1;
            horizontalSpanCache = gridData.horizontalSpan;
        }
        int horizontalSpan = gridData.horizontalSpan;
        Validate.isTrue(gridData.horizontalSpan + currentColumnCounter <= columns,
                "Horizontal Span " + gridData.horizontalSpan //$NON-NLS-1$
                        + " is too large for the empty space on this line: " + spaceLeft); //$NON-NLS-1$
        int rest = Math.abs(gridData.horizontalSpan % control.getNumberOfControls());
        gridData.horizontalSpan = gridData.horizontalSpan / control.getNumberOfControls();
        this.applyCustomizedLayout(control, gridData);
        if (rest > 0)
            this.fill(rest);
        incrementColumnCount(horizontalSpan - rest);
        setDebugInfo(control);
        return this;
    }

    /**
     * apply the standard layout to the given control which might be a composite too
     *
     * @param control
     * @param minCharSize
     * @param hSpan
     * @param vSpan
     */
    private void applyStandardLayout(RCPControl control, int minCharSize, int hSpan, int vSpan)
    {
        // children are only layouted for compounds, since composites will always define their own
        // layouts:
        // - for extensible composites the client uses a composite builder to define the layout
        // - for non extensible composites (prefabricated custom controls) the composite will do its
        // own layout not using a builder at all
        if (control instanceof RCPCompound && !(control instanceof RCPComposite))
        {
            RCPCompound compound = (RCPCompound) control;
            // for compounds: apply an unparameterized layout to all children but the main control,
            // the main control is applied the parameterized layout
            for (RCPControl child : compound.getRcpChildren())
            {
                if (compound.getMainControl() != child)
                {
                    // TODO how to handle nested compounds?
                    if (child instanceof RCPCompound && !(child instanceof RCPComposite))
                        applyStandardLayout(child, minCharSize, hSpan, vSpan);
                    //apply layout only if composite is not extensible -> composite will layout itself
                    else //if(control instanceof RCPComposite && ((RCPComposite)control).isExtensible())
                        applyLayout(compound, child);
                }
            }
            control = compound.getMainControl();
        }

        // TODO how to handle nested compounds?
        // recurse into main control for layout
        if (control instanceof RCPCompound && !(control instanceof RCPComposite))
            applyStandardLayout(control, minCharSize, hSpan, vSpan);
        else if(control != null)
        {
            //apply layout data only if composite is not extensible -> composite will layout itself
//            if(control instanceof RCPComposite && ((RCPComposite)control).isExtensible())
                layoutFactory.applyFloatLayoutData(control, hSpan, vSpan, true, minCharSize);
        }
    }

    /**
     * apply the standard layout to the given control which might be a composite too
     *
     * @param control
     * @param minCharSize
     * @param hSpan
     * @param vSpan
     */
    private void applyFillLayout(RCPControl control, int hSpan, int vSpan)
    {
        if (control instanceof RCPCompound && !(control instanceof RCPComposite))
        {
            RCPCompound compound = (RCPCompound) control;
            // for compounds: apply an unparameterized layout to all children but the main control,
            // the main control is applied the parameterized layout
            for (RCPControl child : compound.getRcpChildren())
            {
                if (compound.getMainControl() != child)
                {
                    applyLayout(compound, child);
                }
            }
            control = compound.getMainControl();
        }
        layoutFactory.applyFillGrabLayoutData(control, hSpan, vSpan, true, false, true);
    }

    /**
     * apply the standard layout to the given control which might be a composite too
     *
     * @param control
     * @param gridData
     */
    private void applyCustomizedLayout(RCPControl control, GridData gridData)
    {
        if (control instanceof RCPCompound && !(control instanceof RCPComposite))
        {
            RCPCompound compound = (RCPCompound) control;
            // for compounds: apply the parameterized layout to all children but the main control
            for (RCPControl child : compound.getRcpChildren())
            {
                layoutFactory.applyLayoutData(child, gridData);
            }
        }
        layoutFactory.applyLayoutData(control, gridData);
    }

    /**
     * apply the default layout for the given control in the context of the given parent
     *
     * @param control
     */
    @Override
    protected void applyLayout(RCPControl parent, RCPControl control)
    {
        // apply default layout for control of compounds which are not the main control
        if (control instanceof RCPSimpleLabel || control instanceof RCPHyperlinkLabel)
        {
            layoutFactory.applyLabelLayoutData(control);
        }
        else if (control instanceof RCPSimpleButton || control instanceof RCPHyperlinkButton)
        {
            layoutFactory.applyFloatLayoutData(control, 1, false);
        }
    }

    /**
     * increments the current column counter modulo nr of columns
     *
     * @param i increment by i
     */
    private void incrementColumnCount(int i)
    {
        if(columns > 0)
            currentColumnCounter = (currentColumnCounter + i) % columns;
    }

    /**
     * @return the number of columns remaining in this row, this amount must be filled to finish the
     *         line. nrOfColumns if end of line is already reached; always > 0
     */
    private int getRemainingColumns()
    {
        return columns - currentColumnCounter;
    }

    /**
     * add a labeled control filling all remaining grid columns
     */
    public final AbstractBuilder addLine(RCPControl control)
    {
        return addLineSpan(control, SWT.DEFAULT);
    }

    /**
     * add a labeled control start with filling all remaining grid columns after "skip" columns
     */
    public final AbstractBuilder addSkipLeftLine(RCPControl control, int skip)
    {  
        this.fill(skip);
        return addLineSpan(control, SWT.DEFAULT);
    }

    /**
     * add a labeled control filling all remaining grid columns
     */
    public final AbstractBuilder addLine(RCPControl control, int minCharSize)
    {
        return addLineSpan(control, minCharSize);
    }

    /**
     * add a labelled control filling 1 (no label) or 2 (with label) grid columns, main control
     * grabs and fill space defined by parameters (horizontal and vertical span)
     *
     * @param control control to add
     * @param hSpan horizontal span
     * @param vSpan vertical span
     * @return this
     */
    public AbstractBuilder addLineGrabAndFill(RCPControl control, int hSpan, int vSpan)
    {
        createControl(control);
        applyFillLayout(control, hSpan, vSpan);
        if (vSpan > 1)
        {
            verticalSpanCache = vSpan - 1;
            horizontalSpanCache = hSpan;
        }
        incrementColumnCount(control.getNumberOfControls() + hSpan - 1);
        setDebugInfo(control);
        return this;
    }

    /**
     * add a labelled control filling 1 (no label) or 2 (with label) grid columns, main control
     * grabs and fill space defined by parameter hSpan
     *
     * @param control control to add
     * @param hSpan horizontal span
     * @return this
     */
    public AbstractBuilder addLineGrabAndFill(RCPControl control, int hSpan)
    {
        createControl(control);
        applyFillLayout(control, hSpan, 1);
        incrementColumnCount(control.getNumberOfControls() + hSpan - 1);
        setDebugInfo(control);
        return this;
    }
    

    /**
     * apply layout with minal spaces and layoutdata with no indent
     * @param comp
     */
    public void applyMinimalSpace(Control control)
    {
        layoutFactory.applyMinimalSpace(control);
    }

    public GridBuilder addAlignedContainer(RCPComposite composite)
    {
        GridBuilder nestedBuilder = this.addContainer(composite);
        layoutFactory.applyAlignedProperties(composite);
        
        return nestedBuilder;
    }
    
    public GridBuilder addAlignedContainer(RCPComposite composite, int columns)
    {
        GridBuilder nestedBuilder = this.addContainerSpan(composite, columns, 1, 1, false);
        layoutFactory.applyAlignedProperties(composite);
        return nestedBuilder;
    }
    
    public GridBuilder addAlignedContainer(RCPComposite composite, int columns, int hSpan)
    {
        GridBuilder nestedBuilder = this.addContainerSpan(composite, columns, hSpan, 1, false);
        layoutFactory.applyAlignedProperties(composite);
        return nestedBuilder;
    }
    
    /**
     * convenience method for building 2 column composites, one for labels, one for controls, see
     * {@link #addContainer(RCPComposite, int)}
     */
    public final GridBuilder addContainer(RCPComposite composite)
    {
        return addContainer(composite, 2);
    }

    /**
     * creates a nested composite
     *
     * @return composite builder for building the nested composite contents
     */
    public final GridBuilder addContainer(RCPComposite control, int columns)
    {
        return addContainerSpan(control, columns, 1, 1, false);

    }

    public GridBuilder addContainerSpan(RCPComposite control, int columns, int hSpan, int vSpan,
                                        boolean indent)
    {
        createControl(control);
        // composites need a layout
        layoutFactory.applyFillHorizontalGrabLayoutData(control, hSpan, vSpan, indent);
        incrementColumnCount(hSpan);
        GridBuilder nestedBuilder = new GridBuilder(formToolkit, layoutFactory, control, columns);
        return nestedBuilder;

    }

    /**
     * returns all controls created by this builder
     */
    public List<RCPWidget> getCreatedControls()
    {
        return Collections.unmodifiableList(createdControls);
    }

    /** create an invisible filler for the given horizontalSpan grid cells
     *  NOTE: this method will create an invisible widget to simulate the skip-effect.
     *  if you want do fill up the remaining space of the line, use {@link GridBuilder#fillLine()} instead.
     *  @param horizontalSpan number of rows which should be left out
     */
    public GridBuilder fill(int horizontalSpan)
    {
        //skip columns
        //TODO how to skip columns in grid layout without lacking swt resources?
        Label l = new Label(currentParent.getClientComposite(), SWT.NONE);
        l.setVisible(false);
        l.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, horizontalSpan, 1));
        this.incrementColumnCount(horizontalSpan);
        
//        RCPSimpleLabel rcpLabel = new RCPSimpleLabel("", SWT.NONE); //$NON-NLS-1$
//        rcpLabel.setRcpParent(currentParent);
//        rcpLabel.createUI(formToolkit);
//        rcpLabel.setState(EControlState.VISIBLE, false);
//        rcpLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, horizontalSpan, 1));
//        rcpLabel.setKeepSpaceIfInvisible(true);
//        incrementColumnCount(horizontalSpan);
//        setDebugInfo(rcpLabel);
        return this;
    }

    /**
     * Fills up the current line only if it is not empty.
     * Note: Will not create any widget or rousources as placeholders for skipping columns.
     */
    public void fillLine()
    {
        if (getRemainingColumns() > 0 && getRemainingColumns() != columns)
        {
//            this.fill(getRemainingColumns());
            if(!createdControls.isEmpty())
            {
                int remainingColumns = getRemainingColumns();
                //get last added element
                RCPWidget widget = this.createdControls.get(createdControls.size() - 1);
                Widget swtWidget = widget.getSWTWidget();
                if(swtWidget instanceof Control)
                {
                    GridData gd = (GridData)((Control)swtWidget).getLayoutData();
                    gd.horizontalSpan += remainingColumns;
                    incrementColumnCount(remainingColumns);
                }
            }
        }
    }

    /**
     * Fills up the current line or adds a new line if the current line is empty.
     */
    public void newLine()
    {
        this.fill(getRemainingColumns());
    }

    public GridData getInitialGridData()
    {
        return null;
    }

    /**
     * for each value create a radio button with the label the label provider delivers for the value
     * and associate it with the given model value via a RadioGroupManager.
     *
     * @return radio group manager managing the radio buttons
     */
    public RadioGroupManager addRadiogroup(RCPGroup group, Object[] values,
                                           LabelProvider labelProvider, int hSpan)
    {

        java.util.List<RCPSimpleButton> widgets = new ArrayList<RCPSimpleButton>();
        GridBuilder radioFactory = addContainerSpan(group, 1, hSpan, 1, true);
        layoutFactory.applyFillLayoutData(group, hSpan);
        for (Object value : values)
        {
            String label = labelProvider.getText(value);
            RCPSimpleButton button = new RCPSimpleButton(label, SWT.RADIO);
            radioFactory.add(button);
            widgets.add(button);
        }

        RadioGroupManager result = new RadioGroupManager(widgets
                .toArray(new RCPSimpleButton[widgets.size()]), values.clone());
        return result;
    }

    /**
     * add a labeled control filling 1 (no label) or 2 (with label) grid columns, make labeled
     * control minimum size
     *
     * @param control control to add
     * @param widthHintInChars control will be minimum avgDialogCharSize*minCharSize pixel;
     *            SWT.DEFAULT: no hint
     * @return this
     */
    public AbstractBuilder addSpan(RCPControl control, int minCharSize, int hSpan, int vSpan)
    {
        createControl(control);
        applyStandardLayout(control, minCharSize, hSpan, vSpan);
        incrementColumnCount(control.getNumberOfControls() + hSpan - 1);
        setDebugInfo(control);
        return this;
    }

    /**
     * add a labeled control filling all remaining grid columns, make labeled control minimum size
     *
     * @param control control to add
     * @param widthHintInChars control will be minimum avgDialogCharSize*minCharSize pixel;
     *            SWT.DEFAULT: no hint
     * @return this
     */
    public AbstractBuilder addLineSpan(RCPControl control, int minCharSize)
    {
        // FIXME problem with compounds here: the control.getNumberOfControls() normally returns '0'
        // for customized compounds; compound and its children will be created after this
        // validation: "createControl(control);"
        Validate.isTrue(control.getNumberOfControls() <= getRemainingColumns(), "The control " //$NON-NLS-1$
                + control.getId() + " has " + control.getNumberOfControls() //$NON-NLS-1$
                + " controls, which is more than fit the remaining " + getRemainingColumns() //$NON-NLS-1$
                + " columns in the grid"); //$NON-NLS-1$
        createControl(control);
        int hSpan = 1 + getRemainingColumns() - control.getNumberOfControls();
        applyStandardLayout(control, minCharSize, hSpan, 1);
        incrementColumnCount(hSpan + control.getNumberOfControls() - 1);
        setDebugInfo(control);
        Validate.isTrue(currentColumnCounter == 0,
                "Internal Error in GridBuilder: currentColumnCounter = " + currentColumnCounter //$NON-NLS-1$
                        + " != 0 after addLine()"); //$NON-NLS-1$
        return this;
    }
    
    /**
     * Add a labeled control filling all columns of this row.
     * If useEfficientLayout flag is set to true  an efficient layout will be used. This means that the label will be set on top of the control (not on the left side).
     * If flag is set to false the function has the same behaviour as {@link GridBuilder#addLineSpan(RCPControl, SWT.DEFAULT)}
     * Note: this method should only be used on empty lines!
     *
     * @param control labeled control to add
     * @param useEfficientLayout indicates where to put the label. true: put it on top of the main control, false: put it on the left of the main control
     * @return this
     */
    public AbstractBuilder addLineSpan(RCPLabeledControl<?> control, boolean useEfficientLayout)
    {
        if(!useEfficientLayout)
            this.addLineSpan(control, SWT.DEFAULT);
        else{
            Validate.isTrue(currentColumnCounter == 0, "Error: efficient layout can only applied to labeled controls if they were added to a completely empty line!\nCurrently " + currentColumnCounter + " columns are alredy used in this line!");
            createControl(control);
            GridData gd = GridLayoutFactory.createDefaultLayoutData();
            gd.horizontalAlignment = GridData.BEGINNING;
            gd.horizontalSpan = columns;
//            gd.horizontalIndent = GridLayoutFactory.INDENT_FOR_DECORATORS;
            control.getRCPHyperlink().getSWTControl().setLayoutData(gd);
            gd = GridLayoutFactory.createDefaultLayoutData();
            gd.horizontalAlignment = GridData.BEGINNING;
            gd.horizontalSpan = columns;
            if(control.getMainControl().getSWTControl() instanceof Composite)
                gd.horizontalIndent = 0;
            control.getMainControl().getSWTControl().setLayoutData(gd);
        }
        
        return this;
    }

}
