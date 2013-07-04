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

package net.sf.rcpforms.tablesupport.tables;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.converter.IPropertyChain;
import net.sf.rcpforms.modeladapter.util.Validate;
import net.sf.rcpforms.tablesupport.Activator;
import net.sf.rcpforms.tablesupport.Messages;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;

/**
 * TableLabelProvider and CellModifier for the generic table implementation. The label provider uses
 * the same converters as the UpdateStrategy for converting from model attribute representation to
 * string/cell editor representation.
 * <p>
 * Usually you will not need to instantiate or subclass this class, since it is configured via
 * {@link ColumnConfiguration}. If you need to subclass, e.g. to support images in columns or a
 * specific display format or more sophisticated cell editors, the following methods are important:
 * <ul>
 * <li> {@link #getColumnText(Object, int)} retrieves the text displayed in a cell if no cell editor
 * is active
 * <li> {@link #getValue(Object, String)} retrieves the object passed to the cell editor when it is
 * activated
 * <li>{@link #modify(Object, String, Object)} writes back cell editor value into the model. Usually
 * must be overridden if you override convertValueToString and the cell is editable. It converts the
 * cell editor value back to the model representation and stores it there.
 * <li>{@link #convertValueToString(Object, Object, String, int)} can be overridden to use a
 * specific conversion scheme for a column; the converter is used to display a value in a table cell
 * and if the column is editable it is used to put a value into the cell editor
 * <li>{@link #getColumnImage(Object, int)} may be overridden if you want a customized image for a
 * column
 * </ul>
 * The label provider fires a change event for a model element if modify changed an attribute.
 * <p>
 * TODO LATER: validation support
 * 
 * @author Marco van Meegen, lindoerfern
 */
public class PropertyLabelProviderAndCellModifier extends LabelProvider
    implements ITableLabelProvider, ICellModifier, ITableColorProvider, IFontProvider
{

    private boolean showErrorDialog = false;

    private static final Logger LOG = Logger.getLogger(PropertyLabelProviderAndCellModifier.class
            .getName());

    private static Color DISABLED_RGB_BG_COLOR = null;

    private static Color DISABLED_RGB_FG_COLOR = null;

    /** rgb bg color for disabled rows, light gray */
    private static final RGB DISABLED_RGB_BG = new RGB(170, 170, 170);

    private static final RGB DISABLED_RGB_FG = new RGB(50, 50, 50);

    private static final String CHECKED_KEY = "net.sourceforge.rcpforms.checked"; //$NON-NLS-1$

    private static final String UNCHECKED_KEY = "net.sourceforge.rcpforms.unchecked"; //$NON-NLS-1$

    private static final String CHECKED_KEY_DEACTIVATED = "net.sourceforge.rcpforms.checked_deactivated"; //$NON-NLS-1$

    private static final String UNCHECKED_KEY_DEACTIVATED = "net.sourceforge.rcpforms.unchecked_deactivated"; //$NON-NLS-1$

    /** converter map key */
    private static final class ConverterKey
    {
        private Object fromType;

        private Object toType;

        /**
         * Constructor for ConverterKey
         * 
         * @param fromType fromType
         * @param toType toType
         */
        public ConverterKey(Object fromType, Object toType)
        {
            super();
            Validate.notNull(fromType);
            Validate.notNull(toType);
            this.fromType = fromType;
            this.toType = toType;
        }

        @Override
        public boolean equals(Object obj)
        {
            ConverterKey conv = (ConverterKey) obj;
            return fromType.equals(conv.fromType) && toType.equals(conv.toType);
        }

        @Override
        public int hashCode()
        {
            return fromType.hashCode() + 23 * toType.hashCode();
        }

        @Override
        public String toString()
        {
            return "(" + fromType.toString() + "->" + toType.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
    }

    /** the column configurations to use */
    private ColumnConfiguration[] columnConfigurations;

    /** cached converters */
    private Map<ConverterKey, IConverter> converterCache = new HashMap<ConverterKey, IConverter>();

    private Object rowElementMetaClass;

    /**
     * flag if the table is readonly, i.e. cannot be modified and private boolean readonly; /**
     */
    private boolean readonly;

    /**
     * create an unitialised PropertyLabelProvider. PAY ATTENTION: if using this constructor you
     * have to make sure that the object is properly initialised calling init(ColumnConfiguration[],
     * Class<?>) on it.
     */
    public PropertyLabelProviderAndCellModifier()
    {
    }

    /**
     * create a PropertyLabelProvider for the columns defined in the given array of column
     * configurations.
     * 
     * @param columnConfigurations column i will be defined by the columnConfigurations[i]
     * @param rowElementMetaClass row element meta class, a Class for beans, an EClass for EMF
     *            objects
     */
    public PropertyLabelProviderAndCellModifier(ColumnConfiguration[] columnConfigurations,
                                                Object rowElementMetaClass)
    {
        init(columnConfigurations, rowElementMetaClass);
        initImages();
    }

    /** set if error dialog should be shown on exception in modify of cell editor */
    public void setShowErrorDialog(boolean newValue)
    {
        this.showErrorDialog = newValue;
    }

    public void init(ColumnConfiguration[] columnConfigurations, Object rowElementClass)
    {
        this.columnConfigurations = columnConfigurations;
        this.rowElementMetaClass = rowElementClass;
        Validate.noNullElements(columnConfigurations);
        Validate.notNull(rowElementClass);
        validatePropertiesAndFillEnumComboValues();
        // FIXME if color has to be created -> dispose it (and ONLY then)
        if (getDisabledRGBBGColor() == null)
        {
            DISABLED_RGB_BG_COLOR = new Color(Display.getCurrent(), DISABLED_RGB_BG);
        }
        if (getDisabledRGBFGColor() == null)
        {
            DISABLED_RGB_FG_COLOR = new Color(Display.getCurrent(), DISABLED_RGB_FG);
        }
    }

    private void initImages()
    {
        if (JFaceResources.getImageRegistry().getDescriptor(CHECKED_KEY) == null)
        {
            JFaceResources.getImageRegistry().put(UNCHECKED_KEY, makeShot(false, true));
            JFaceResources.getImageRegistry().put(CHECKED_KEY, makeShot(true, true));
            JFaceResources.getImageRegistry()
                    .put(UNCHECKED_KEY_DEACTIVATED, makeShot(false, false));
            JFaceResources.getImageRegistry().put(CHECKED_KEY_DEACTIVATED, makeShot(true, false));
        }
    }

    private Image makeShot(boolean type, boolean activated)
    {
        // Hopefully no platform uses exactly this color because we'll make
        // it transparent in the image.
        Color greenScreen = new Color(Display.getCurrent(), 222, 223, 224);

        Shell shell = new Shell(Display.getCurrent(), SWT.NO_TRIM);

        // otherwise we have a default gray color
        shell.setBackground(greenScreen);

        Button button = new Button(shell, SWT.CHECK | SWT.FLAT);
        button.setEnabled(activated);
        button.setBackground(greenScreen);
        button.setSelection(type);

        // otherwise an image is located in a corner
        button.setLocation(1, 1);
        Point bsize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);

        // otherwise an image is stretched by width
        bsize.x = Math.max(bsize.x - 1, bsize.y - 1);
        bsize.y = Math.max(bsize.x - 1, bsize.y - 1);
        button.setSize(bsize);
        shell.setSize(bsize);

        shell.open();
        GC gc = new GC(shell);
        Image image = new Image(Display.getCurrent(), bsize.x, bsize.y);
        gc.copyArea(image, 0, 0);
        gc.dispose();
        shell.close();

        image.getImageData().palette.getPixel(greenScreen.getRGB());

        return image;
    }

    /**
     * get a converter for the given columnindex, fromType, toType. Converters are cached and
     * reused.
     * 
     * @return converter to use for the given column and type
     */
    protected IConverter getConverter(ModelAdapter adapter, Object fromType, Object toType)
    {
        ConverterKey key = new ConverterKey(fromType, toType);
        IConverter converter = converterCache.get(key);
        if (converter == null)
        {
            converter = adapter.getConverterRegistry().getConverter(fromType, toType);
            converterCache.put(key, converter);
        }
        return converter;
    }

    /**
     * retrieves the property of element defined by the columnIndex-th columnConfiguration, converts
     * it using the default converter and returns it
     * 
     * @return string to display in table cell row 'element', column 'columnIndex'
     */
    public String getColumnText(Object element, int columnIndex)
    {
        String result = ""; //$NON-NLS-1$
        String property = columnConfigurations[columnIndex].getProperty();
        if (property != null)// && columnConfigurations[columnIndex].getCellEditorType() !=
        // ECellEditorType.CHECK)
        {
            ModelAdapter adapter = getModelAdapter(element);
            Object value = getProperty(element, property);
            result = convertValueToString(adapter, value, columnConfigurations[columnIndex]);
        }
        return result;
    }

    /**
     * Returns the value which is set into the cell editor.
     * 
     * @param element data model to retrieve property from
     * @param property property to retrieve from element
     * @return value of the property, converted to cell editor representation
     */
    public Object getValue(Object element, String property)
    {
        Object value = null;
        try
        {
            ModelAdapter adapter = getModelAdapter(element);
            ColumnConfiguration conf = getColumnConfiguration(property);
            value = getProperty(element, property);
            if (value != null)
            {
                IConverter converter = getConverter(adapter, value.getClass(), conf
                        .getCellEditorType().getValueType());
                value = converter.convert(value);
            }

            switch (conf.getCellEditorType())
            {

                case COMBO:
                    int index = -1;
                    for (int i = 0; i < conf.getComboItems().length; i++)
                    {
                        if (value != null && value.equals(conf.getComboItems()[i]))
                        {
                            index = i;
                            break;
                        }
                    }
                    // combo wants index into item array
                    value = new Integer(index);
                    break;
                case CHECK:
                    if (value == null)
                    {
                        value = Boolean.FALSE;
                    }
                    // if value is null it will be initialised with false -> why validation needed?
                    Validate.notNull(value, "Null Value received in " //$NON-NLS-1$
                            + this.getClass().getSimpleName()
                            + ". Is converter from null to Boolean missing ?"); //$NON-NLS-1$
                    Validate
                            .isTrue(value instanceof Boolean,
                                    "Value for CheckboxCellEditor is not of type Boolean. Is converter from null to Boolean missing ?"); //$NON-NLS-1$
                    break;
                case TEXT:
                    if (value == null)
                    {
                        value = ""; //$NON-NLS-1$
                    }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            String message = "Error in Provider: " + getClass().getName() //$NON-NLS-1$
                    + " getting cell editor value for property " + property + ": " //$NON-NLS-1$ //$NON-NLS-2$
                    + ex.getMessage();
            LOG.severe(message);
            throw new RuntimeException(message, ex);
        }

        // cell editors do NOT like null values !
        Validate.notNull(value);
        return value;
    }

    /**
     * if a value is edited by a cell editor, this method is used to set the edited value back to
     * the model. The value is converted from cell editor representation to model representation and
     * then set to the given property of the given element.
     * <p>
     * A {@link LabelProviderChangedEvent} must be fired on modification, otherwise the table will
     * not show the changes.
     * <p>
     * ATTENTION: combo cell editors use an Integer index into the combo list as their value
     */
    public void modify(Object element, String property, Object value)
    {
        try
        {
            ModelAdapter adapter = getModelAdapter(element);
            if (element instanceof Item)
            {
                element = ((Item) element).getData();
            }
            ColumnConfiguration conf = getColumnConfiguration(property);
            Object comboValue = value;
            if (conf.getCellEditorType() == ECellEditorType.COMBO)
            {
                Integer in = (Integer) value;
                if (in != -1)
                {
                    comboValue = conf.getComboItems()[in];
                }
                else
                {
                    comboValue = null;
                }
            }
            // convert back to model
            // TODO: get correct from type
            Object result = null;
            if (comboValue != null)
            {
                Object propertyType = getPropertyType(element, property);
                IConverter converter = getConverter(adapter, conf.getCellEditorType()
                        .getValueType(), propertyType);
                result = converter.convert(comboValue);
                // convert NullValue placeholder to real null
                if (NullValue.isNullValue(result))
                {
                    result = null;
                }
            }
            setProperty(element, property, result);

        }
        catch (Exception ex)
        {
            String message = MessageFormat.format(Messages
                    .getString("PropertyLabelProviderAndCellModifier.CellModificationError") //$NON-NLS-1$
                    + ex.getMessage(), getClass().getName(), property);
            LOG.severe(message);
            if (showErrorDialog)
            {
                new ErrorDialog(
                        Display.getCurrent().getActiveShell(),
                        Messages
                                .getString("PropertyLabelProviderAndCellModifier.CellModificationErrorTitle"), MessageFormat.format(Messages.getString("PropertyLabelProviderAndCellModifier.CellModificationErrorDetail"), value, property), //$NON-NLS-1$ //$NON-NLS-2$
                        new Status(Status.ERROR, Activator.PLUGIN_ID, ex.getMessage(), ex),
                        IStatus.ERROR).open();
            }
            else
            {
                throw new IllegalArgumentException(message);
            }
        }
    }

    private Object getPropertyType(Object modelInstance, String propertyPath)
    {
        ModelAdapter modelAdapter = getModelAdapter(modelInstance);
        IPropertyChain propertyChain = modelAdapter.getPropertyChain(modelAdapter
                .getMetaClass(modelInstance), propertyPath);
        Object result = propertyChain.getType();
        return result;
    }

    private ModelAdapter getModelAdapter(Object modelInstance)
    {
        return ModelAdapter.getAdapterForInstance(modelInstance);
    }

    /**
     * override to support images which depend on the given element. images are only provided for
     * {@link ECellEditorType#CHECK} columns}
     * 
     * @return image to use for the given element in the given column
     */
    public Image getColumnImage(Object element, int columnIndex)
    {
        ColumnConfiguration conf = columnConfigurations[columnIndex];
        Image returnImage = null;
        if (columnIndex > 0 // use for the first column only SWT.CHECK style for checkboxes in
                // tables!
                && conf.getCellEditorType() == ECellEditorType.CHECK)
        {
            Boolean booleanValue = this.getBooleanPropertyValue(element, conf.getProperty());
            if (booleanValue != null)
            {
                if (element instanceof ITableSelectable
                        && !((ITableSelectable) element).getIsSelectable())
                {
                    returnImage = JFaceResources.getImageRegistry().get(
                            booleanValue.booleanValue() ? CHECKED_KEY_DEACTIVATED
                                                       : UNCHECKED_KEY_DEACTIVATED);
                }
                else
                {
                    returnImage = JFaceResources.getImageRegistry().get(
                            booleanValue.booleanValue() ? CHECKED_KEY : UNCHECKED_KEY);
                }

            }
        }
        return returnImage;
    }

    /**
     * @param element data model
     * @param property the property to get boolean value from. not null
     * @return boolean or null if either element or property were not applicable
     */
    private Boolean getBooleanPropertyValue(Object element, String property)
    {
        Boolean returnValue = null;
        try
        {
            PropertyDescriptor pd = new PropertyDescriptor(property, element.getClass());
            Method getterMethod = pd.getReadMethod();
            if (getterMethod.getReturnType() != Boolean.class
                    && getterMethod.getReturnType() != Boolean.TYPE)
                throw new IllegalArgumentException("Getter method for property " + property //$NON-NLS-1$
                        + " on " + element.getClass().toString() + " does not return a boolean!"); //$NON-NLS-1$ //$NON-NLS-2$
            returnValue = (Boolean) getterMethod.invoke(element, new Object[]{});

        }
        catch (IntrospectionException e)
        {
            LOG.severe(e.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            LOG.severe(e.getMessage());
        }
        catch (IllegalAccessException e)
        {
            LOG.severe(e.getMessage());
        }
        catch (InvocationTargetException e)
        {
            LOG.severe(e.getMessage());
        }

        return returnValue;
    }

    /**
     * converts the given model value to string representation
     * 
     * @param value model value, usually a Java type like String, Date, Integer, Boolean
     * @param conf column for which to convert the value
     * @return string representation for the given value, must not be null
     */
    public String convertValueToString(ModelAdapter adapter, Object value, ColumnConfiguration conf)
    {
        String result = ""; //$NON-NLS-1$
        if (value != null)
        {
            IConverter converter = getConverter(adapter, value.getClass(), String.class);
            result = (String) converter.convert(value);
        }
        return result;
    }

    /**
     * Returns the property of the model object using the ModelAdapter
     * 
     * @param modelInstance the model for the row to retrieve the property from
     * @param propertyPath The property name of bean, nested properties are supported
     * @return value of the given property
     * @throws IllegalArgumentException if property cannot be read
     */
    private Object getProperty(Object modelInstance, String propertyPath)
    {
        ModelAdapter modelAdapter = getModelAdapter(modelInstance);
        Object result = modelAdapter.getValue(modelInstance, propertyPath);
        return result;
    }

    /**
     * Adds a value to a bean property by calling the apropriate setter method by introspection
     * 
     * @param element the object where the value should be set
     * @param property the field where the value should be set
     * @param value the value to be set
     */
    private void setProperty(Object modelInstance, String propertyPath, Object value)
    {
        ModelAdapter modelAdapter = getModelAdapter(modelInstance);
        modelAdapter.setValue(modelInstance, propertyPath, value);
    }

    /**
     * Decides if a certain property of a bean can be modified. A property is represented as column
     * inside a table.
     * 
     * @param element The bean
     * @param property The bean property
     * @return True if property is modifiable
     */
    public boolean canModify(Object element, String property)
    {
        boolean result = false;
        // System.err.println("dlamWidget: " + dlamWidget + ", chainEnabled: "
        // + (dlamWidget != null ? dlamWidget.isChainEnabled() : ""));
        if (!isReadonly())
        {
            for (ColumnConfiguration config : columnConfigurations)
            {
                if (property != null && property.equals(config.getProperty())
                        && config.getCellEditorType() != null)
                {
                    result = true;
                    if (element instanceof ITableSelectable)
                    {
                        ITableSelectable dlam = (ITableSelectable) element;
                        if (!dlam.getIsSelectable())
                        {
                            result = false;
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    /**
     * gets the readonly style for the label provider; if it is set to true, no editing is possible.
     * 
     * @param readonly
     */
    public boolean isReadonly()
    {
        return readonly;
    }

    /**
     * sets the readonly style for the label provider; if it is set to true, no editing is possible.
     * 
     * @param readonly
     */
    public void setReadonly(boolean readonly)
    {
        this.readonly = readonly;
    }

    /**
     * Returns the column configuration associated with the bean property
     * 
     * @param property the bean property
     * @return Column configuration for the column associated with the bean property, null if
     *         property not found
     */
    private ColumnConfiguration getColumnConfiguration(String property)
    {
        ColumnConfiguration result = null;
        for (ColumnConfiguration conf : columnConfigurations)
        {
            if (property.equals(conf.getProperty()))
            {
                result = conf;
                break;
            }
        }
        return result;
    }

    /**
     * @return true if label is affected by a change of the given property
     */
    @Override
    public boolean isLabelProperty(Object element, String property)
    {
        return getColumnConfiguration(property) != null;
    }

    /**
     * checks if all column properties exist in the given class and have a write method if they are
     * editable.
     * 
     * @throws IllegalArgumentException if properties are not available
     */
    private void validatePropertiesAndFillEnumComboValues()
    {
        String message = ""; //$NON-NLS-1$
        Class<?> clazz = null;
        for (int i = 0; i < columnConfigurations.length; i++)
        {
            ColumnConfiguration conf = columnConfigurations[i];
            String msgHead = "Column " + i + ": Property " + conf.getProperty(); //$NON-NLS-1$ //$NON-NLS-2$
            ModelAdapter adapter = ModelAdapter.getAdapterForMetaClass(rowElementMetaClass);
            adapter.validatePropertyPath(rowElementMetaClass, conf.getProperty(), conf
                    .getCellEditorType() != null);
            if (conf.getCellEditorType() == ECellEditorType.COMBO && conf.getComboItems() == null)
            {
                // TODO LATER: try to get range adapter for filling the combo
                // // fill enum values
                // conf.setComboItems(IRangeAdapter.getInput());
                // }
            }
        }
        if (message.length() > 0)
        {
            throw new IllegalArgumentException("Properties of class " + rowElementMetaClass //$NON-NLS-1$
                    + " not matching column configuration:\n" + message); //$NON-NLS-1$
        }
    }

    /**
     * overridden to render disabled rows in gray
     * 
     * @param element model
     * @param columnIndex index of the column to retrieve foreground for
     * @return color or null if no specific color is needed
     */
    public Color getForeground(Object element, int columnIndex)
    {
        // disable rows which implement ITableSelectable and are not selectable
        if (element != null && element instanceof ITableSelectable
                && !((ITableSelectable) element).getIsSelectable())
        {
            return getDisabledRGBFGColor();
        }
        return null;
    }

    /**
     * See {@link ITableColorProvider#getBackground(Object, int)}
     * 
     * @param element
     * @param columnIndex
     * @return
     */
    public Color getBackground(Object element, int columnIndex)
    {
        // if (!isReadonly())
        // {
        // // see bug report #220 -> returning a color automatically enables customdraw in tables
        // // -> return null.
        // // if table is disabled emulate original disabled state
        // return null;
        //
        // }
        if (element != null && element instanceof ITableSelectable
                && !((ITableSelectable) element).getIsSelectable())
        {
            return getDisabledRGBBGColor();
        }
        return null;
    }

    /**
     * TODO LATER: non-static way to determine color
     * 
     * @return color to use as bg for disabled rows
     */
    private Color getDisabledRGBBGColor()
    {
        return DISABLED_RGB_BG_COLOR;
    }

    /**
     * TODO LATER: non-static way to determine color
     * 
     * @return color to use as foreground for disabled rows
     */
    private Color getDisabledRGBFGColor()
    {
        return DISABLED_RGB_FG_COLOR;
    }

    public ColumnConfiguration[] getColumnConfigurations()
    {
        return columnConfigurations;
    }

    public Font getFont(Object element)
    {
        return JFaceResources.getDialogFont();
    }

    @Override
    public void dispose()
    {
        super.dispose();
        // free allocated resources
        // TODO remsy add color to a colorregistry.
        // disposing at this time will cause an error if someone closes an editor with the form
        // inside.
        // reopening the editor will rise an exception because of the color is already disposed
        // if(getDisabledRGBBGColor() != null)
        // DISABLED_RGB_BG_COLOR.dispose();
        // if(getDisabledRGBFGColor() != null)
        // DISABLED_RGB_FG_COLOR.dispose();
    }
}
