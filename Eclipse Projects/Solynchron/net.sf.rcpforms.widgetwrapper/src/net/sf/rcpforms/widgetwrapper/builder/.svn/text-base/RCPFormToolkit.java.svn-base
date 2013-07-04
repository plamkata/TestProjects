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

import java.lang.reflect.Field;
import java.util.logging.Logger;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.customwidgets.CustomButton;
import net.sf.rcpforms.widgetwrapper.customwidgets.HyperlinkLabel;
import net.sf.rcpforms.widgetwrapper.customwidgets.ICompositeImageHyperLink;
import net.sf.rcpforms.widgetwrapper.customwidgets.IconText;
import net.sf.rcpforms.widgetwrapper.util.SWTResourceUtil;
import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;

/**
 * A Widget factory that creates widgets and ensures that all widgets created through it have a
 * common look (ex. identical font size, etc.). Extends the FormToolkit to support more create
 * methods.
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class RCPFormToolkit extends RCPThemeFormToolkit implements IFormToolkitEx
{
    private static final Logger LOG = Logger.getLogger(RCPFormToolkit.class.getName());

    private static final String READONLY_MARKER = "net.sf.rcpforms.widgetwrapper.readonly"; //$NON-NLS-1$

    private PaintListener m_readonlyBorderPaintListener;

    private static final RGB MANDATORY_COLOR_RGB = new RGB(255, 255, 225);

    private static final RGB MANDATORYM2_COLOR_RGB = new RGB(238, 217, 217);

    /** disabled field rgb bg color for widgets, light gray */
    public static final RGB DISABLED_RGB_BG = new RGB(240, 240, 240);

    // initialize all needed colors.
    static
    {
        ColorRegistry colReg = JFaceResources.getColorRegistry();
        colReg.put(IColorConstants.DEFAULT_BG, new RGB(255, 255, 255));
        colReg.put(IColorConstants.DEFAULT_FG, new RGB(0, 0, 0));
        colReg.put(IColorConstants.DISABLED_BG, DISABLED_RGB_BG);
        colReg.put(IColorConstants.DISABLED_BG, new RGB(240, 240, 240));
        colReg.put(IColorConstants.DISABLED_FG, Display.getCurrent().getSystemColor(
                SWT.COLOR_DARK_GRAY).getRGB());
        colReg.put(IColorConstants.ERROR_BG, new RGB(255, 55, 55));
        colReg.put(IColorConstants.MANDATORY_BG, MANDATORY_COLOR_RGB);
        colReg.put(IColorConstants.MANDATORYM2_BG, MANDATORYM2_COLOR_RGB);
        colReg.put(IColorConstants.TERMINOLOGY_BG, new RGB(243, 183, 147));
        colReg.put(IColorConstants.WARNING_BG, new RGB(255, 55, 55));
        colReg.put(IColorConstants.LABEL_FG, new RGB(25, 76, 127));
    }

    public static final int DEFAULT_LABEL_STYLE = SWT.RIGHT;

    public static final int DEFAULT_SINGLETEXT_STYLE = SWT.SINGLE;

    public static final int DEFAULT_MULTITEXT_STYLE = SWT.MULTI;

    public static final int DEFAULT_SINGLEREADONLYTEXT_STYLE = DEFAULT_SINGLETEXT_STYLE
            | SWT.READ_ONLY;

    public static final int DEFAULT_MULTIREADONLYTEXT_STYLE = DEFAULT_MULTITEXT_STYLE
            | SWT.READ_ONLY;

    public static final int DEFAULT_BUTTON_STYLE = SWT.FLAT;

    public static final int DEFAULT_COMBO_STYLE = SWT.FLAT;

    public static final int DEFAULT_CLABEL_STYLE = SWT.FLAT;

    public static final int DEFAULT_PUSH_BUTTON_STYLE = SWT.PUSH;

    public static final int DEFAULT_RADIO_BUTTON_STYLE = SWT.RADIO;

    public static final int DEFAULT_CHECKBOX_BUTTON_STYLE = SWT.CHECK;

    public static final int DEFAULT_TOGGLE_BUTTON_STYLE = SWT.TOGGLE;

    private static final int DEFAULT_TABLE_STYLE = SWT.FULL_SELECTION | SWT.FLAT | SWT.H_SCROLL
            | SWT.V_SCROLL;

    public static final int DEFAULT_TABLE_STYLE_SINGLESELECT = SWT.SINGLE | DEFAULT_TABLE_STYLE;

    public static final int DEFAULT_TABLE_STYLE_MULTISELECT = SWT.MULTI | DEFAULT_TABLE_STYLE;

    public static final int DEFAULT_CHECKBOX_TABLE_STYLE = SWT.CHECK | DEFAULT_TABLE_STYLE;

    public static final int DEFAULT_TREE_STYLE = SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL;

    public static final int DEFAULT_HYPERLINK_STYLE = SWT.FLAT | SWT.WRAP;

    public static final int DEFAULT_GROUP_BORDER_STYLE = SWT.SHADOW_ETCHED_IN;

    public static final int DEFAULT_SECTION_STYLE = Section.TITLE_BAR | Section.TWISTIE
            | Section.EXPANDED;

    public static final int DEFAULT_LIST_STYLE = SWT.BORDER;

    private static Field s_fldListener;

    private static Field s_fldTxt;

    private static Field s_fldArrow;

    private static boolean s_bCComboFieldsFound;

    static
    {
        // Mega haessliches Hack, damit wir die Children einer CCombo zugreifen können.
        // Kann unter Umstände Probleme machen wenn SWT die Implementierung von CCombo ändert.
        try
        {
            Field[] fields = CCombo.class.getDeclaredFields();
            for (Field f : fields)
            {
                if ("listener".equals(f.getName())) { //$NON-NLS-1$
                    s_fldListener = f;
                    s_fldListener.setAccessible(true);
                }
                if ("text".equals(f.getName())) { //$NON-NLS-1$
                    s_fldTxt = f;
                    s_fldTxt.setAccessible(true);
                }
                if ("arrow".equals(f.getName())) { //$NON-NLS-1$
                    s_fldArrow = f;
                    s_fldArrow.setAccessible(true);
                }

                if (null != s_fldListener && null != s_fldTxt && null != s_fldArrow)
                {
                    s_bCComboFieldsFound = true;
                    break;
                }
            }

        }
        catch (Exception e)
        {
            s_bCComboFieldsFound = false;
            LOG
                    .severe("The Fields listener, text or arrow of the class CCombo could not be accessed! Probably the internal implementation of the SWT CCombo changed!"); //$NON-NLS-1$
        }

    }

    public RCPFormToolkit(Display display)
    {
        super(display);
        // TODO: this causes problems in size calculation of text fields
        // HACK to be able to render read-only controls with just an underline
        // borders are painted
        setBorderStyle(SWT.NULL);
        // register label color which has to be calculated...
        // JFaceResources.getColorRegistry().put(IColorConstants.LABEL_FG,
        // this.getColors().getColor(IFormColors.TITLE).getRGB());

    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createButton(org.eclipse.swt.widgets.Composite, java.lang.String)
     */
    public Button createButton(final Composite parent, final String text)
    {
        Button button = super.createButton(parent, text, DEFAULT_BUTTON_STYLE);
        return button;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createGroup(org.eclipse.swt.widgets.Composite, java.lang.String)
     */
    public Group createGroup(final Composite parent, final String text)
    {
        return createGroup(parent, text, DEFAULT_GROUP_BORDER_STYLE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createGroup(org.eclipse.swt.widgets.Composite, java.lang.String, int)
     */
    public Group createGroup(final Composite parent, final String text, final int style)
    {
        Group group = new Group(parent, style);
        group.setBackground(super.getColors().getBackground());
        group.setText(text);
        // add correct font
        group.setFont(JFaceResources.getDialogFont());
        return group;
    }

    @Override
    public Label createLabel(final Composite parent, final String text)
    {
        return createLabel(parent, text, DEFAULT_LABEL_STYLE);
    }

    @Override
    public Label createLabel(final Composite parent, final String text, final int style)
    {
        Label label = super.createLabel(parent, text, style);
        label.setForeground(super.getColors().getColor(IFormColors.TITLE));
        return label;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createText(Composite, String)
     */
    @Override
    final public Text createText(final Composite parent, String value)
    {
        return createText(parent, value, DEFAULT_SINGLETEXT_STYLE);
    }

    @Override
    public Text createText(final Composite parent, final String value, final int style)
    {
        int modifiedStyle = SWT.READ_ONLY == (style & SWT.READ_ONLY) ? style | SWT.NO_FOCUS : style;
        // create Text control which computes border size even if no border is set
        Text text = new Text(parent, getBorderStyle() | modifiedStyle | getOrientation());
        // VM: for some strange reasons this does not work to have the same layout of text controls
        // using SWT.BORDER or SWT.NULL
        // {
        // @Override
        // public Rectangle computeTrim(int x, int y, int width, int height)
        // {
        // Rectangle rect = super.computeTrim(x, y, width, height);
        // // if no border, calculate trim anyway
        // if ((getBorderStyle() & SWT.BORDER) == 0)
        // {
        // rect.x -= 3;
        // rect.y -= 3;
        // rect.width += 6;
        // rect.height += 6;
        // }
        // return rect;
        // }
        //
        // @Override
        // protected void checkSubclass()
        // {
        // }
        // };
        if (value != null)
            text.setText(value);
        super.adapt(text, true, false);

        // according style guideline:
        setReadonly(text, SWT.READ_ONLY == (style & SWT.READ_ONLY));

        return text;
    }

    /**
     * set the readonly style for the control if this is supported. Controls not supporting the
     * readonly style are set en/disabled.
     * 
     * @param control control to apply style to
     * @param readonly readonly style
     */
    public void setReadonly(final Control control, final boolean readonly)
    {
        // the readonly style means the control is only for display, the user cannot change the
        // control value.
        // Opposed to disabled, the user can still copy the displayed text to the clipboard.
        // most widgets do not support readonly style
        if (control instanceof CCombo)
        {
            setReadonly((CCombo) control, readonly);
        }
        else if (control instanceof Text)
        {
            setReadonly((Text) control, readonly);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#setReadonly(org.eclipse.swt.custom.CCombo, boolean)
     */
    public void setReadonly(final CCombo combo, final boolean readonly)
    {
        if (WidgetUtil.isValid(combo))
        {
            if (s_bCComboFieldsFound)
            {
                // Die Felder aus der Klasse CCombo konnten erfolgreich geholt werden
                combo.setData(FormToolkit.KEY_DRAW_BORDER, !readonly);
                combo.setData(READONLY_MARKER, readonly); // mark control
                combo.setEditable(!readonly);

                // Mega haessliches Hack, damit wir die Children einer CCombo zugreifen können.
                // Kann unter Umstände Probleme machen wenn SWT die Implementierung von CCombo
                // ändert.
                // Hängt alle Listener vom internen Text ab.
                // Nimmt den internen Pfeil weg.
                try
                {
                    Listener listener = (Listener) s_fldListener.get(combo);

                    Text text = (Text) s_fldTxt.get(combo);
                    int[] textEvents = {SWT.KeyDown, SWT.KeyUp, SWT.MenuDetect, SWT.Modify,
                            SWT.MouseDown, SWT.MouseUp, SWT.MouseWheel, SWT.Traverse, SWT.FocusIn, SWT.Verify};

                    if (readonly)
                    {
                        for (int i = 0; i < textEvents.length; i++)
                            text.removeListener(textEvents[i], listener);
                    }
                    // Wenn wieder editable, kein addListener() nötig!
                    // Listener wurden bereits wieder angehängt!

                    Button btn = (Button) s_fldArrow.get(combo);
                    btn.setVisible(!readonly);

                    final String painterAddedKey = "painterAddedKey"; //$NON-NLS-1$
                    final Composite pComp = combo.getParent();
                    if (readonly && null == pComp.getData(painterAddedKey))
                    {
                        pComp.setData(painterAddedKey, Boolean.TRUE);
                        pComp.addPaintListener(getReadonlyBorderPaintListener());
                    }
                }
                catch (Exception e)
                {
                    combo.setEnabled(!readonly);
                    LOG
                            .severe("The Fields listener, text or arrow of the class CCombo could not be accessed! Probably the internal implementation of the SWT CCombo changed!"); //$NON-NLS-1$
                }
                redrawBorderRect(combo);

            }
            else
            {
                combo.setEnabled(!readonly);
            }
        }
    }

    /**
     * marks the area of the control parent which contains the border as dirty
     * 
     * @param control control for which borders should be redrawn
     */
    private void redrawBorderRect(CCombo combo)
    {
        Rectangle rect = combo.getParent().getBounds();
        combo.getParent().redraw(rect.x - 1, rect.y - 2, rect.width + 1, rect.height + 3, false);
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#setReadonly(org.eclipse.swt.widgets.Text, boolean)
     */
    private void setReadonly(final Text text, final boolean readonly)
    {
        if (WidgetUtil.isValid(text))
        {
            text.setData(FormToolkit.KEY_DRAW_BORDER, !readonly);
            text.setData(READONLY_MARKER, readonly); // mark control

            text.setEditable(!readonly);

            final String painterAddedKey = "painterAddedKey"; //$NON-NLS-1$
            final Composite pComp = text.getParent();
            if (readonly && null == pComp.getData(painterAddedKey))
            {
                pComp.setData(painterAddedKey, Boolean.TRUE);
                pComp.addPaintListener(getReadonlyBorderPaintListener());
            }
            text.redraw();
        }
    }

    /**
     * @param control control to check
     * @return true if the control is marked as read-only, used to draw read-only border
     */
    private static boolean isReadonly(final Control control)
    {
        return control.getData(READONLY_MARKER) != null
                && ((Boolean) control.getData(READONLY_MARKER)).booleanValue();
    }

    protected PaintListener getReadonlyBorderPaintListener()
    {
        if (m_readonlyBorderPaintListener == null)
        {
            m_readonlyBorderPaintListener = new PaintListener()
            {
                public void paintControl(PaintEvent event)
                {
                    Composite composite = (Composite) event.widget;
                    Control[] children = composite.getChildren();
                    GC gc = event.gc;
                    gc.setForeground(getReadonlyTextBorderColor());

                    for (int i = 0; i < children.length; i++)
                    {
                        Control c = children[i];
                        // check control's mark
                        if (c.isVisible() && isReadonly(c))
                        {
                            final Rectangle b = c.getBounds();
                            int x1 = b.x - 1;
                            int x2 = b.x + b.width;
                            int by = b.y + b.height;
                            gc.drawLine(x1, by, x2, by);
                            gc.drawLine(x1, by - 1, x1, by);
                            gc.drawLine(x2, by, x2, by - 1);
                        }
                    }
                }
            };
        }
        return m_readonlyBorderPaintListener;
    }

    private Color getReadonlyTextBorderColor()
    {
        final String colorKey = "COLOR_NONEDITABLE_TEXT_BOREDER"; //$NON-NLS-1$

        Color borderColor = super.getColors().getColor(colorKey);
        if (null == borderColor)
        {
            Color origBorderColor = super.getColors().getBorderColor();
            int r = brighter(origBorderColor.getRed(), 2, 3);
            int g = brighter(origBorderColor.getGreen(), 2, 3);
            int b = brighter(origBorderColor.getBlue(), 2, 3);
            borderColor = super.getColors().createColor(colorKey, r, g, b);
        }
        return borderColor;
    }

    private int brighter(final int color, final int x, final int y)
    {
        Validate.isTrue(color <= 255);
        return (255 - color) * x / y + color;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createCombo(org.eclipse.swt.widgets.Composite)
     */
    public CCombo createCombo(final Composite parent)
    {
        return createCombo(parent, DEFAULT_COMBO_STYLE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createCombo(org.eclipse.swt.widgets.Composite, int)
     */
    public CCombo createCombo(final Composite parent, final int style)
    {
        return createCombo(parent, style, false);
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createCombo(org.eclipse.swt.widgets.Composite, int, boolean)
     */
    public CCombo createCombo(final Composite parent, final int style, final boolean autoComplete)
    {
        final CCombo combo = new CCombo(parent, style);
        super.adapt(combo, true, true);
        if (autoComplete)
        {
            // TODO LATER: make compliant to all platforms
            throw new IllegalArgumentException(
                    "AutoComplete currently not supported since not platform independent"); //$NON-NLS-1$
            // new ComboAutoComplete(combo);
        }
        return combo;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createCLabel(org.eclipse.swt.widgets.Composite)
     */
    public CLabel createCLabel(final Composite parent)
    {
        CLabel cLabel;
        cLabel = new CLabel(parent, DEFAULT_CLABEL_STYLE);
        super.adapt(cLabel, true, true);
        return cLabel;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createPushButton(org.eclipse.swt.widgets.Composite, java.lang.String)
     */
    public Button createPushButton(final Composite parent, final String text)
    {
        Button button = super.createButton(parent, text, DEFAULT_PUSH_BUTTON_STYLE);
        return button;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createPushButton(org.eclipse.swt.widgets.Composite,
     *      org.eclipse.swt.graphics.Image)
     */
    public Button createPushButton(final Composite parent, final Image image)
    {
        Button button = super.createButton(parent, null, DEFAULT_PUSH_BUTTON_STYLE);
        button.setImage(image);
        return button;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createRadioButton(org.eclipse.swt.widgets.Composite, java.lang.String)
     */
    public Button createRadioButton(final Composite parent, final String text)
    {
        Button button = super.createButton(parent, text, DEFAULT_RADIO_BUTTON_STYLE);
        return button;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createToggleButton(Composite, String)
     */
    public Button createToggleButton(final Composite parent, final String text)
    {
        Button button = super.createButton(parent, text, DEFAULT_TOGGLE_BUTTON_STYLE);
        // setApplicationDefaultFontToControl(button);
        return button;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createCheckbox(org.eclipse.swt.widgets.Composite, java.lang.String)
     */
    public Button createCheckbox(final Composite parent, final String text)
    {
        Button button = super.createButton(parent, text, DEFAULT_CHECKBOX_BUTTON_STYLE);
        return button;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createCustomButton(org.eclipse.swt.widgets.Composite,
     *      org.eclipse.swt.graphics.Image)
     */
    public CustomButton createCustomButton(Composite parent, Image image)
    {
        CustomButton cb = new CustomButton(parent, image);
        super.adapt(cb, true, true);
        return cb;
    }

    @Override
    public Composite createComposite(final Composite parent)
    {
        Composite result = super.createComposite(parent, SWT.NONE);
        super.paintBordersFor(result);
        return result;
    }

    @Override
    public Composite createComposite(Composite parent, int style)
    {
        Composite composite = super.createComposite(parent, style);
        super.paintBordersFor(composite);
        return composite;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createSection(org.eclipse.swt.widgets.Composite, int)
     */
    @Override
    public Section createSection(final Composite parent, final int style)
    {
        return super.createSection(parent, style);
    }

    public Section createSection(final Composite parent)
    {
        Section section = createSection(parent, DEFAULT_SECTION_STYLE);
        return section;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createTable(org.eclipse.swt.widgets.Composite)
     */
    public Table createTable(final Composite parent)
    {
        return createTable(parent, DEFAULT_TABLE_STYLE_MULTISELECT); // mukhinr: should be singel
        // select?
    }

    @Override
    public Table createTable(final Composite parent, final int style)
    {
        Table table = super.createTable(parent, style);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        return table;
    }

    public Table createCheckboxTable(final Composite parent)
    {
        return createTable(parent, DEFAULT_CHECKBOX_TABLE_STYLE);
    }

    public Table createCheckboxTable(final Composite parent, int style)
    {
        return createTable(parent, style | SWT.CHECK);
    }

    public IconText createIconText(final Composite parent, final Image image)
    {
        IconText iconText = new IconText(parent, SWT.NONE);
        iconText.setImage(image);
        super.adapt(iconText);
        return iconText;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createHyperlinkLabel(org.eclipse.swt.widgets.Composite, java.lang.String)
     */
    public HyperlinkLabel createHyperlinkLabel(final Composite parent, final String label)
    {
        // DEFAULT_HYPERLINK_STYLE = SWT.FLAT | SWT.WRAP;
        HyperlinkLabel hl = new HyperlinkLabel(parent, DEFAULT_HYPERLINK_STYLE);
        hl.setText(label);
        hl.setForeground(super.getColors().getColor(IFormColors.TITLE));
        super.adapt(hl, true, true);
        return hl;
    }

    @Override
    public Hyperlink createHyperlink(final Composite parent, final String label, final int style)
    {
        Hyperlink hyperlink = super.createHyperlink(parent, label, DEFAULT_HYPERLINK_STYLE);
        hyperlink.setForeground(super.getColors().getColor(IFormColors.TITLE));
        return hyperlink;

    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createHyperlink(org.eclipse.swt.widgets.Composite, java.lang.String)
     */
    public Hyperlink createHyperlink(final Composite parent, final String hyperlinklabel)
    {
        return createHyperlink(parent, hyperlinklabel, DEFAULT_HYPERLINK_STYLE);
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createCompositeImageHyperlink(org.eclipse.swt.widgets.Composite,
     *      java.lang.String, org.eclipse.swt.graphics.Image)
     */
    public ICompositeImageHyperLink createCompositeImageHyperlink(final Composite parent,
                                                                  final String label,
                                                                  final Image image)
    {
        final Composite hyperlinkComposite = createComposite(parent);
        hyperlinkComposite.setLayout(new RowLayout());
        final Hyperlink labelHyperlink = createHyperlink(hyperlinkComposite, label,
                DEFAULT_HYPERLINK_STYLE);
        final ImageHyperlink imageHyperlink = createHyperlink(hyperlinkComposite, null, image,
                SWT.LEFT | DEFAULT_HYPERLINK_STYLE);
        return new ICompositeImageHyperLink()
        {
            public ImageHyperlink getImageHyperlink()
            {
                return imageHyperlink;
            }

            public Hyperlink getLabelHyperlink()
            {
                return labelHyperlink;
            }
        };
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createHyperlink(org.eclipse.swt.widgets.Composite, java.lang.String,
     *      org.eclipse.swt.graphics.Image, int)
     */
    public ImageHyperlink createHyperlink(final Composite parent, final String label,
                                          final Image image, final int style)
    {
        ImageHyperlink hyperlink = super.createImageHyperlink(parent, style);
        hyperlink.setText(label);
        hyperlink.setImage(image);
        return hyperlink;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#createTree(org.eclipse.swt.widgets.Composite)
     */
    public Tree createTree(final Composite parent)
    {
        return createTree(parent, DEFAULT_TREE_STYLE);
    }

    @Override
    public Tree createTree(final Composite parent, final int style)
    {
        Tree tree = super.createTree(parent, style);
        return tree;
    }

    // BEFORE TO ADD NEW COLORS LOOK INTO super.getColors().get...

    public static Color getMandatoryColor()
    {
        return SWTResourceUtil.getCachedColor(MANDATORY_COLOR_RGB);
    }

    public static Color getMandatoryM2ColorFieldEmpty()
    {
        return SWTResourceUtil.getCachedColor(MANDATORYM2_COLOR_RGB);
    }

    public static Color getMandatoryM2ColorFieldFilled()
    {
        return SWTResourceUtil.getCachedColor(MANDATORY_COLOR_RGB);
    }

    public static Color getDisabledBGColor()
    {
        return SWTResourceUtil.getCachedColor(DISABLED_RGB_BG);
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#getTextReadonlyStyle(boolean)
     */
    public int getTextReadonlyStyle(final boolean readonly)
    {
        int style = readonly ? RCPFormToolkit.DEFAULT_SINGLEREADONLYTEXT_STYLE
                            : RCPFormToolkit.DEFAULT_SINGLETEXT_STYLE;
        return style;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#getMultilineTextReadonlyStyle(boolean)
     */
    public int getMultilineTextReadonlyStyle(final boolean readonly)
    {
        int style = readonly ? RCPFormToolkit.DEFAULT_MULTIREADONLYTEXT_STYLE
                            : RCPFormToolkit.DEFAULT_MULTITEXT_STYLE;
        return style;
    }

    /**
     * (non-Javadoc)
     * 
     * @see IFormToolkitEx#modifyFont(org.eclipse.swt.widgets.Control, int, boolean)
     */
    public void modifyFont(final Control control, int fontHeightDiv, boolean bold)
    {
        FontData fd[] = control.getFont().getFontData();
        if (bold)
        {
            control.setFont(SWTResourceUtil.getCachedBoldFont(SWTResourceUtil
                    .getCachedFont(new FontData(fd[0].getName(), fd[0].getHeight() + fontHeightDiv,
                            SWT.BOLD))));
        }
        else
        {
            control.setFont(SWTResourceUtil.getCachedFont(new FontData(fd[0].getName(), fd[0]
                    .getHeight()
                    + fontHeightDiv, SWT.BOLD)));
        }
    }

    public List createList(Composite parent, int style)
    {
        List list = new List(parent, style);
        super.adapt(list, true, true);
        return list;
    }

    public List createList(Composite parent)
    {
        List list = createList(parent, DEFAULT_LIST_STYLE);
        return list;
    }

    public TableViewer createTableViewer(Table widget)
    {
        TableViewer viewer = null;
        if ((widget.getStyle() & SWT.CHECK) == SWT.CHECK)
        {
            viewer = new CheckboxTableViewer(widget);
        }
        else
        {
            viewer = new TableViewer(widget);
        }
        return viewer;
    }

    public TreeViewer createTreeViewer(Tree widget)
    {
        TreeViewer viewer = null;
        if ((widget.getStyle() & SWT.CHECK) == SWT.CHECK)
        {
            viewer = new CheckboxTreeViewer(widget);
        }
        else
        {
            viewer = new TreeViewer(widget);
        }
        return viewer;
    }
}
