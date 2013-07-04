/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 *******************************************************************************/

package net.sf.rcpforms.widgetwrapper.wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import net.sf.rcpforms.common.util.NullValueUtil;
import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.builder.GridLayoutFactory;
import net.sf.rcpforms.widgetwrapper.wrapper.radio.RadioGroupManager;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * a Group widget with radio button management which offers a convenient way of creating and binding
 * radio groups. Do not add children explicitly, because this is thought to automatically manage the
 * children. If you need more flexibility, use {@link RCPGroup} and add children using builder and
 * {@link RadioGroupManager}.
 *<p>
 * It contains a {@link RadioGroupManager} which manages the association between radio buttons and
 * model values, i.e. the "value" of the RadioGroupManager is the model value associated with the
 * currently selected radio button. The widget can directly be bound to a model attribute of the
 * type of the passed model values, in rcpforms standard binding a
 * {@link net.sf.rcpforms.bindingvalidation.RadioGroupObservableValue} is used to perform the
 * binding in both directions.
 * <p>
 * If RCP Binding Layer is used, and if the model adapter provides a {@link IRangeAdapter} for the
 * attribute bound to the {@link RCPRadioGroup}, model values for radio buttons are taken from the
 * range adapter and the label provider is used to create the text representations for the labels.
 * This is very convenient for enum-like attributes.
 * <p>
 * There are various constructors passing radio button text and model elements, passing model
 * elements and a label provider which is used to get the text or not passing anything which enables
 * calling a method later to create the radio buttons; this can be used when the exact content of
 * the radio group is not known at ui creation time, but at bind time.
 * <p>
 * Usage Example for explicit radio button creation:
 * <p>
 * <code>
 *  RCPRadioGroup genderWithNull = new RCPRadioGroup("gender(with null):", true);
 *  List<Object> values = new ArrayList<Object>(Gender.MALE,Gender.FEMALE);
 *  m_radioGroup.createRadioButtons(new Object[]{Gender.MALE, Gender.FEMALE, NullValue.getInstance()}, {"male","female","-"});
 * </code>
 * 
 * @author Marco van Meegen
 */
public class RCPRadioGroup extends RCPGroup implements IPresentationConfiguration
{
    private static GridLayoutFactory layoutFactory = new GridLayoutFactory();

    /**
     * layout used for the group
     */
    private GridLayout layout = null;

    private boolean vertical = false;

    /** flag if a radio button for <null> value should be added */
    boolean hasNullValue = false;

    String nullValuePresentationKey = null;

    String presentationKey = null;

    /**
     * @return Returns the presentationKey.
     */
    public String getPresentationKey()
    {
        return presentationKey;
    }

    /**
     * @param presentationIndex The presentationIndex to set.
     */
    public void setPresentationIndex(String presentationIndex)
    {
        this.presentationKey = presentationIndex;
    }

    /** model elements associated with buttons in this radio group */
    private Object[] modelElements = null;

    private RadioGroupManager radioGroupManager = null;

    /**
     * @return Returns the radioGroupManager used by this group. One of the
     *         {@link #createRadioButtons(Object[], ILabelProvider, boolean))} methods must have
     *         been called before.
     */
    public RadioGroupManager getRadioGroupManager()
    {
        return radioGroupManager;
    }

    /**
     * @return Returns the index for the representation of the null value.
     */
    public String getNullValuePresentationKey()
    {
        return nullValuePresentationKey;
    }

    /**
     * if a representation for <null> should be displayed, this can select a number of available
     * representations registered in {@link NullValue}.
     * 
     * @param nullValuePresentationIndex The nullValuePresentationIndex to set.
     */
    public void setNullValuePresentationKey(String nullValuePresentationIndex)
    {
        this.nullValuePresentationKey = nullValuePresentationIndex;
    }

    /**
     * create a radio group with the given label and default style, radio buttons are placed
     * vertically in one column.
     * 
     * @param label group label
     */
    public RCPRadioGroup(String label)
    {
        this(label, SWT.DEFAULT, false);
    }

    /**
     * create a group with the given label, radio buttons are placed vertically in one column.
     * 
     * @param label group label
     * @param hasNullValue true if the null representation should be added to this group
     */

    public RCPRadioGroup(String label, boolean addNull)
    {
        this(label, SWT.DEFAULT, addNull);
    }

    /**
     * create a group with the given label and style, radio buttons are placed vertically in one
     * column.
     * 
     * @param label group label
     * @param style group style
     * @param hasNullValue true if the null representation should be added to this group
     */
    public RCPRadioGroup(String label, int style, boolean addNull)
    {
        this(label, style, addNull, true, false, 1);
    }

    /**
     * create a radio group with the given label and default style, radio buttons are placed in the
     * given directions into the given nr of columns. They are placed in order of model elements
     * vertically/horizontally into the nr of columns, e.g. vertically 8 buttons 3 columns: button
     * 0-2 into first column, button 3-5 into second column and button 6-7 into third column;
     * horizontally: button 0,3,6 into first column, 1,4,7 into second column, 2,5 into third
     * column.
     * 
     * @param label group label
     * @param style style
     * @param hasNullValue true if the null value should be added to the radio group
     * @param vertical true: top to bottom, false: left to right
     * @param sameSize true if all columns should be same size
     * @param columns nr of colums in the grid
     */
    public RCPRadioGroup(String label, int style, boolean hasNullValue, boolean vertical,
                         boolean sameSize, int columns)
    {
        super(label, style);
        Validate.isTrue(columns > 0);
        layout = (GridLayout) layoutFactory.getLayout(columns, sameSize);
        this.vertical = vertical;
        this.hasNullValue = hasNullValue;

    }

    public final void createRadioButtons(Object[] modelElements, ILabelProvider provider)
    {
        createRadioButtons(Arrays.asList(modelElements), provider);
    }

    /**
     * create radio buttons for the given model elements using the provider to create labels.
     * 
     * @param modelElements model elements to create radio buttons for
     * @param provider provider providing labels for radio buttons
     */
    public final void createRadioButtons(List<Object> modelElements, ILabelProvider provider)
    {
        List<String> labels = new ArrayList<String>();
        for (Object modelElement : modelElements)
        {
            labels.add(provider.getText(modelElement));
        }
        createRadioButtons(modelElements, labels);
    }

    public final void createRadioButtons(Object[] modelElements, String[] labels)
    {
        createRadioButtons(Arrays.asList(modelElements), Arrays.asList(labels), null);
    }

    /**
     * create radio buttons for the given model elements using the given labels for display
     * 
     * @param modelElements model elements which are written back to the bound attribute if the
     *            associated button is selected
     * @param labels labels to use for the buttons, modelElements[i] will be associated with radio
     *            button with label labels[i]
     */
    public final void createRadioButtons(List<Object> modelElements, List<String> labels)
    {
        createRadioButtons(modelElements, labels, null);
    }

    /**
     * may be called before or after createUI() phase.
     */
    protected void createRadioButtons(List<Object> modelElements, List<String> labels,
                                      List<Image> images)
    {
        // first create rcp buttons
        Validate.noNullElements(modelElements, "ModelElements must not be null"); //$NON-NLS-1$
        Validate.noNullElements(labels, "Labels must not be null"); //$NON-NLS-1$
        Validate.isTrue(modelElements.size() == labels.size(),
                "modelElements and labels must have same nr of elements"); //$NON-NLS-1$
        // LATER: find a nicer way of adding the null value automatically ?
        int nullValueCount = 0;
        for (Object modelElement : modelElements)
        {
            if (NullValueUtil.isNullValue(modelElement))
            {
                nullValueCount++;
            }
        }
        Validate
                .isTrue(
                        nullValueCount == (hasNullValue ? 1 : 0),
                        "If hasNullValue is true, you must make sure that the list of passed elements contains exactly one NullValue placeholder element. If not, there must not be any NullValue placeholders at all. Null Values found: " //$NON-NLS-1$
                                + nullValueCount);

        // permutate indices to reflect vertical arrangement if vertical is true
        int[] permutatedIndices = getCreationOrder(modelElements.size(), layout.numColumns,
                vertical);
        for (int i = 0; i < modelElements.size(); i++)
        {
            int permutated = permutatedIndices[i];
            RCPRadioButton button = new RCPRadioButton(labels.get(permutated),
                    images == null ? null : images.get(permutated), SWT.DEFAULT);
            button.setLayoutData(layoutFactory.getFloatLayoutData(1, 1, false));
            internalAdd(button);
        }
        this.modelElements = modelElements.toArray(new Object[modelElements.size()]);
        // was group created before ?
        // if not children will be created in createUI
        if (formToolkit != null)
        {
            // yes, create children now
            doCreateChildrenUI(formToolkit);
            radioGroupManager = new RadioGroupManager(getPermutatedChildren(), this.modelElements);
        }
    }

    /**
     * @return child radio buttons in the order of the passed model elements, not the permutated
     *         grid fill order
     */
    private RCPSimpleButton[] getPermutatedChildren()
    {
        int[] permutatedIndices = getCreationOrder(modelElements.length, layout.numColumns,
                vertical);
        List<RCPControl> children = getRcpChildren();
        RCPSimpleButton[] result = new RCPSimpleButton[children.size()];
        for (int i = 0; i < result.length; i++)
        {
            Validate.isTrue(result[permutatedIndices[i]] == null,
                    "Internal error: Permutation is not bijective"); //$NON-NLS-1$
            result[permutatedIndices[i]] = (RCPSimpleButton) children.get(i);
        }
        return result;
    }

    /**
     * calculates the creation order how element associated radio buttons are filled into the grid.<BR>
     * TODO: this is buggy, fix ! There might be filler necessary in grid, e.g. 4 elements in 3
     * columns -> last element in first row should not be filled
     * 
     * @param size number of elements to fill in
     * @param numColumns number of columns in the grid
     * @param vertical if true, a permutation of elements is returned so that the permutated
     *            elements filled from left to right into the grid will appear in top-to-bottom
     *            order; if false, no permutation needs to be done, returned is result[i]:=i
     * @return permutated indices, array has length size
     */
    public static int[] getCreationOrder(int size, int numColumns, boolean vertical)
    {
        int[] result = new int[size];
        for (int i = 0; i < size; i++)
        {
            int elementsPerColumn = (int) ((size + numColumns - 1) / numColumns);
            if (vertical)
            {
                int y = i % numColumns;
                int x = (int) (i / numColumns);
                result[i] = x + y * elementsPerColumn;
            }
            else
            {
                // horizontal is natural grid order
                result[i] = i;
            }

        }
        // validate result has all indices
        BitSet set = new BitSet();
        for (int i = 0; i < size; i++)
        {
            int permutedIndex = result[i];
            Validate.isTrue(!set.get(permutedIndex), "Duplicate permutated index " + permutedIndex //$NON-NLS-1$
                    + " at index " + i); //$NON-NLS-1$
            Validate.isTrue(permutedIndex >= 0 && permutedIndex < size,
                    "Permuted index out of range: " + permutedIndex + " at index " + i); //$NON-NLS-1$ //$NON-NLS-2$
            set.set(permutedIndex);
        }
        Validate.isTrue(set.cardinality() == size);
        return result;
    }

    /**
     * (non-Javadoc)
     * 
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPGroup#createUI(org.eclipse.ui.forms.widgets.FormToolkit)
     */
    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        Validate.notNull(layout); // should have been set in constructors
        getSwtGroup().setLayout(layout);
        doCreateChildrenUI(formToolkit);
        if (modelElements != null)
        {
            radioGroupManager = new RadioGroupManager(getPermutatedChildren(), this.modelElements);
        }
        getSwtGroup().layout(true, true);
    }

    /**
     * radio group automatically manages its children, if this is not wished, please use
     * {@link RCPGroup}.
     */
    @Override
    public boolean isExtensible()
    {
        return false;
    }

    /**
     * @return true if this radio group should support a null value
     */
    public boolean hasNullValue()
    {
        return hasNullValue;
    }
}
