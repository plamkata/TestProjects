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

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.customwidgets.IHyperlinkLabel;
import net.sf.rcpforms.widgetwrapper.util.PixelConverter;
import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Scrollable;

/**
 * TODO all todos here were mentioned in task 152106 -look into JFace
 * {@link org.eclipse.jface.layout.GridLayoutFactory} if this is more suitable -add methods for
 * vertical height hint using font size -refactor all methods to get the layout and not apply; this
 * is much more flexible to use in frameworks.
 * 
 * @author mukhinr apply<ROLE>LayoutData
 * @author dietisheima
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class GridLayoutFactory
{
    static final int INDENT_FOR_DECORATORS = 8; // TODO: make calculation for * and warn

    public void applyLayout(final RCPComposite composite, int numColumns,
                            boolean makeColumnsEqualWidth)
    {
        Validate.notNull(composite, "RCPComposite must not be null"); //$NON-NLS-1$
        composite.setLayout(getLayout(numColumns, makeColumnsEqualWidth));
    }

    public Layout getLayout(int numColumns, boolean makeColumnsEqualWidth)
    {
        GridLayout layout = new GridLayout(numColumns, makeColumnsEqualWidth);
        // vertical spacing must take border into account because toolkit paints it outside client
        // rect
        layout.verticalSpacing = 4;
        layout.marginTop = 3;
        layout.marginBottom = 3;
        return layout;
    }

    public void applyLayoutData(final RCPControl ctrl, final GridData gridData)
    {
        Validate.isTrue(ctrl != null, "RCPControl must not be null"); //$NON-NLS-1$
        ctrl.setLayoutData(gridData);
    }

    public final void applyLabelLayoutData(final RCPControl label)
    {
        applyLabelLayoutData(label, 1);
    }

    public final void applyLabelLayoutData(final IHyperlinkLabel label)
    {
        Validate.isTrue(RCPControl.class.isAssignableFrom(label.getClass()),
                "IHyperlinkLabel MUST be a RCPControl"); //$NON-NLS-1$
        applyLabelLayoutData((RCPControl) label, 1);
    }

    public void applyLabelLayoutData(final RCPControl label, final int verticalSpan)
    {
        Validate.isTrue(label != null, "label must not be null"); //$NON-NLS-1$
        GridData labelLayoutData = getLabelLayoutData(verticalSpan);
        this.applyExcludeFlag(label, labelLayoutData);
        label.setLayoutData(labelLayoutData);
    }

    private void applyExcludeFlag(RCPControl control, GridData labelLayoutData)
    {
        labelLayoutData.exclude = !control.isVisible() && !control.isKeepSpaceIfInvisible();
    }

    public GridData getLabelLayoutData(final int verticalSpan)
    {
        GridData gridData = null;
        gridData = createLayoutData();
        gridData.horizontalAlignment = SWT.END;
        gridData.verticalAlignment = SWT.TOP;
        gridData.verticalSpan = verticalSpan;
        return gridData;
    }

    public final void applyLabelLayoutData(final IHyperlinkLabel label, final int verticalSpan)
    {
        Validate.isTrue(RCPControl.class.isAssignableFrom(label.getClass()),
                "IHyperlinkLabel MUST be a RCPControl"); //$NON-NLS-1$
        Validate.isTrue(verticalSpan > 0, "verticalSpan must be > 0"); //$NON-NLS-1$
        applyLabelLayoutData((RCPControl) label, verticalSpan);

    }

    public void applyRightAlignedLayoutData(final RCPControl ctrl, final int horizontalSpan)
    {
        Validate.isTrue(ctrl != null, "ctrl must not be null"); //$NON-NLS-1$
        ctrl.setLayoutData(getRightAlignedLayoutData(horizontalSpan));
    }

    public Object getRightAlignedLayoutData(final int horizontalSpan)
    {
        Validate.isTrue(horizontalSpan > 0, "horizontalSpan must be > 0"); //$NON-NLS-1$
        GridData gridData = createLayoutData();
        gridData.horizontalAlignment = SWT.END;
        gridData.horizontalSpan = horizontalSpan;
        return gridData;
    }

    public final void applyFillLayoutData(final RCPControl ctrl, final int horizontalSpan)
    {
        applyFillLayoutData(ctrl, horizontalSpan, true);
    }

    public final void applyFillLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                          final boolean indent)
    {
        applyFillLayoutData(ctrl, horizontalSpan, 1, indent);
    }

    public void applyFillLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                    final int verticalSpan, final boolean indent)
    {
        ctrl.setLayoutData(getFillLayoutData(ctrl, horizontalSpan, verticalSpan, indent));
    }

    public Object getFillLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                    final int verticalSpan, final boolean indent)
    {
        WidgetUtil.ensureValid(ctrl);
        Validate.isTrue(horizontalSpan > 0, "horizontalSpan must be > 0"); //$NON-NLS-1$
        Validate.isTrue(verticalSpan > 0, "verticalSpan must be > 0"); //$NON-NLS-1$
        GridData gridData = createLayoutData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.horizontalSpan = horizontalSpan;
        gridData.verticalSpan = verticalSpan;
        // TODO: do correct calculation
        if (verticalSpan > 1)
        {
            FontData fd = getFontData(ctrl);
            gridData.heightHint = 8 + verticalSpan * (fd.getHeight() + 4);
        }

        if (indent)
        {
            gridData.horizontalIndent = INDENT_FOR_DECORATORS;
        }
        return gridData;
    }

    /**
     * @param ctrl
     * @return first font data of the given control, which is the only one on windows
     */
    private FontData getFontData(final RCPControl ctrl)
    {
        return ctrl.getSWTControl().getFont().getFontData()[0];
    }

    public final void applyFillHorizontalGrabLayoutData(final RCPControl ctrl,
                                                        final int horizontalSpan,
                                                        final boolean indent)
    {
        applyFillGrabLayoutData(ctrl, horizontalSpan, 1, true, false, indent);
    }

    public final void applyFillHorizontalGrabLayoutData(final IHyperlinkLabel ctrl,
                                                        final int horizontalSpan,
                                                        final boolean indent)
    {
        applyFillGrabLayoutData((RCPControl) ctrl, horizontalSpan, 1, true, false, indent);
    }

    public final void applyFillHorizontalGrabLayoutData(final RCPControl ctrl,
                                                        final int horizontalSpan)
    {
        applyFillHorizontalGrabLayoutData(ctrl, horizontalSpan, true);
    }

    public final void applyFillHorizontalGrabLayoutData(final RCPControl ctrl,
                                                        final int horizontalSpan,
                                                        final int verticalSpan, final boolean indent)
    {
        applyFillGrabLayoutData(ctrl, horizontalSpan, verticalSpan, true, verticalSpan > 1, indent);
    }

    /**
     * applies a layout data that centers the widget horizontally in the available (grabbed) space
     * 
     * @param ctrl the control to apply the layout data to
     * @param horizontalSpan the number of columns to span over
     * @param verticalSpan the row span to use
     * @param grabHorizontal a flag that indicates whether available horizontal space shall be
     *            grabbed
     * @param grabVertical a flag that indicates whether available vertical space shall be grabbed
     * @param indent a flag that indicates whether an ident shall be applied to the control
     */
    public final void applyCenteredGrabLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                                  final int verticalSpan, boolean grabHorizontal,
                                                  boolean grabVertical, final boolean indent)
    {
        Validate.isTrue(ctrl != null, "ctrl must not be null"); //$NON-NLS-1$
        applyLayoutData(new GridData(SWT.CENTER, SWT.CENTER, grabHorizontal, grabVertical), ctrl,
                horizontalSpan, verticalSpan, indent);

    }

    public GridData applyFillGrabLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                            final int verticalSpan, boolean grabHorizontal,
                                            boolean grabVertical, final boolean indent)
    {
        Validate.isTrue(ctrl != null); //$NON-NLS-1$
        Validate.isTrue(horizontalSpan > 0);
        Validate.isTrue(verticalSpan > 0);
        WidgetUtil.ensureValid(ctrl);
        GridData gridData = new GridData(SWT.FILL, SWT.TOP, grabHorizontal, grabVertical);
        applyLayoutData(gridData, ctrl, horizontalSpan, verticalSpan, indent);
        return gridData;
    }
    
    
    /**
     * applies to the composite a minimial layout. this means this composite will try to not to waste any space and should be aligned like e.g. a textfield control in the upper line.
     * 
     * @param composite composite on which the minimal layout should be applied
     */
    public void applyAlignedProperties(RCPComposite composite)
    {
        GridLayout gl = (GridLayout) composite.getClientComposite().getLayout();
        gl.marginWidth = 0;
        gl.marginHeight = 0;
        gl.marginTop = 0;
        gl.marginBottom = 0;
        composite.setLayout(gl);

        GridData gd = (GridData) composite.getClientComposite().getLayoutData();
        //calculate horizontal indent for the first widget. take care of the margin width of the grid layout
        //if margin width is greater than indent -> set indent to 0
//        gd.horizontalIndent = GridLayoutFactory.INDENT_FOR_DECORATORS < gl.marginWidth ? 0 : GridLayoutFactory.INDENT_FOR_DECORATORS - gl.marginWidth;
        gd.horizontalIndent = 0;
        composite.setLayoutData(gd);
    }
    /**
     * apply layout with minal spaces and layoutdata with no indent
     * @param comp
     */
    public void applyMinimalSpace(Control control)
    {
        if(control instanceof Composite)
        {
            GridLayout layout = (GridLayout)((Composite)control).getLayout();
            Validate.notNull(layout, "No Layout is set... Please set a layout!");
            ////layout.verticalSpacing = 1;
            ////layout.horizontalSpacing = 1;
            ////layout.marginTop = 0;
            ////layout.marginBottom = 0;
            ////layout.marginLeft = 0;
            ////layout.marginRight = 0;
            //layout.marginWidth = 1;
            //layout.marginHeight = 1;
            
            layout.marginWidth = 1;
            layout.marginHeight = 1;
            layout.marginTop = 1;
            layout.marginBottom = 1;
            ((Composite) control).setLayout(layout);
        }
        
        Object clientLayoutData = control.getLayoutData();
        if (clientLayoutData instanceof GridData)
        {
            GridData gd = (GridData) clientLayoutData;
            gd.horizontalIndent = 0;
        }
    }

    /**
     * applies a layout data (that's already configured in grabbing space and alignement), applies
     * horizontal & vertical span & ident and sets the gridData to the control supplied
     * 
     * @param gridData the configured (grabbing space and alignement) gridData
     * @param ctrl the control that shall get the gridData
     * @param horizontalSpan flag whether to span horizontally
     * @param verticalSpan flag whether to span vertically
     * @param indent the ident
     */
    private void applyLayoutData(final GridData gridData, final RCPControl ctrl,
                                 final int horizontalSpan, final int verticalSpan,
                                 final boolean indent)
    {
        Validate.isTrue(ctrl != null); //$NON-NLS-1$
        Validate.isTrue(gridData != null);

        gridData.horizontalSpan = horizontalSpan;
        applyVerticalSpan(ctrl, verticalSpan, gridData);
        if (indent)
        {
            gridData.horizontalIndent = INDENT_FOR_DECORATORS;
        }
        ctrl.setLayoutData(gridData);
    }

    /**
     * applies a vertical span and height (that's calculated out of the effective font height) hint
     * to the supplied GridData
     * 
     * @param ctrl
     */
    private void applyVerticalSpan(final RCPControl ctrl, final int verticalSpan,
                                   final GridData gridData)
    {
        if (verticalSpan > 1)
        {
            FontData fd = getFontData(ctrl);
            gridData.heightHint = 8 + verticalSpan * (fd.getHeight() + 4);
        }
        gridData.verticalSpan = verticalSpan;
    }

    /**
     * sets grid layout constraints (GridData) to a control.
     * 
     * @param ctrl the control the layout shall be applied to
     * @param horizontalSpan the number of columns the control shall span
     * @return the grid data that was applied to the widget to be layouted
     */
    public void applyFloatLayoutData(final RCPControl ctrl, final int horizontalSpan)
    {
        applyFloatLayoutData(ctrl, horizontalSpan, 1, true);
    }

    public void applyFloatLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                     final boolean indent)
    {
        applyFloatLayoutData(ctrl, horizontalSpan, 1, indent);
    }

    public void applyFloatLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                     final int verticalSpan, final boolean indent)
    {
        Validate.isTrue(ctrl != null); //$NON-NLS-1$
        WidgetUtil.ensureValid(ctrl);
        GridData floatLayoutData = (GridData) getFloatLayoutData(horizontalSpan, verticalSpan, indent);
        this.applyExcludeFlag(ctrl, floatLayoutData);
        ctrl.setLayoutData(floatLayoutData);
    }

    public Object getFloatLayoutData(final int horizontalSpan, final int verticalSpan,
                                     final boolean indent)
    {
        // simple left to right layout

        GridData gridData = createLayoutData();
        gridData.verticalAlignment = SWT.TOP;
        gridData.horizontalSpan = horizontalSpan;
        gridData.verticalSpan = verticalSpan;
        if (indent)
        {
            gridData.horizontalIndent = INDENT_FOR_DECORATORS;
        }
        return gridData;
    }

    /**
     * apply layout data to size to preferred size with place for numberOfChars characters.
     * 
     * @param wrapper wrapper to apply layout to
     * @param horizontalSpan horizontal span
     * @param indent true: control is indented the default indent pixel
     * @param numberOfChars number of chars to hold, -1: use preferred size of control
     */
    public void applyFloatLayoutData(final RCPControl wrapper, final int horizontalSpan,
                                     final int verticalSpan, final boolean indent, int numberOfChars)
    {
        // simple left to right layout
        Validate.isTrue(wrapper != null); //$NON-NLS-1$

        WidgetUtil.ensureValid(wrapper);
        final int MAGIC = 2;
        Control typedWidget = wrapper.getTypedWidget();
        final PixelConverter pc = new PixelConverter(typedWidget);
        GridData gridData = createLayoutData();
        gridData.horizontalSpan = horizontalSpan;
        gridData.verticalSpan = verticalSpan;
        // GC gc = new GC(ctrl.getDisplay());
        // gc.setFont(ctrl.getFont());
        //            Point pt = gc.textExtent(StringUtils.repeat("X", numberOfChars));//$NON-NLS-1$
        //
        // TODO: would max char width be better than average char width + MAGIC ?
        if (numberOfChars != -1)
        {
            int controlWidthForChars = pc.convertWidthInCharsToPixels(numberOfChars + MAGIC);
            int widthIncludingTrim = 0;
            // calculate borders/trim
            if (typedWidget instanceof Scrollable)
            {
                widthIncludingTrim = ((Scrollable) typedWidget).computeTrim(0, 0,
                        controlWidthForChars, 20).width;
            }
            else
            {
                widthIncludingTrim = controlWidthForChars + 2 * typedWidget.getBorderWidth();
            }
            gridData.widthHint = widthIncludingTrim;
        }
        if (indent)
        {
            gridData.horizontalIndent = INDENT_FOR_DECORATORS;
        }
        this.applyExcludeFlag(wrapper, gridData);
        wrapper.setLayoutData(gridData);
    }

    public void applyDateLayoutData(final RCPControl ctrl, final int horizontalSpan,
                                    final boolean indent)
    {
        // simple left to right layout
        Validate.isTrue(ctrl != null); //$NON-NLS-1$

        // TODO cleanup and make more efficient
        WidgetUtil.ensureValid(ctrl);
        GridData gridData = createLayoutData();
        gridData.horizontalSpan = horizontalSpan;
        GC gc = null;
        try
        {
            gc = new GC(ctrl.getSWTControl().getDisplay());
            gc.setFont(ctrl.getSWTControl().getFont());
            Point pt = gc.textExtent("88.88.8888");//$NON-NLS-1$
            gridData.widthHint = pt.x + 2; // TODO: find constant
        }
        finally
        {
            if (gc != null)
            {
                gc.dispose();
            }
        }
        if (indent)
        {
            gridData.horizontalIndent = INDENT_FOR_DECORATORS;
        }
        this.applyExcludeFlag(ctrl, gridData);
        ctrl.setLayoutData(gridData);
    }

    private GridData createLayoutData()
    {
        // write top level default here
        return new GridData();
    }

    public void applyTopAlignLayoutData(final RCPControl ctrl, final int horizontalSpan)
    {
        
        GridData alginLayoutData = (GridData) getTopAlignLayoutData(ctrl, horizontalSpan);;
        this.applyExcludeFlag(ctrl, alginLayoutData);
        ctrl.setLayoutData(alginLayoutData);
    }

    public Object getTopAlignLayoutData(final RCPControl ctrl, final int horizontalSpan)
    {
        GridData gridData = createLayoutData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.verticalAlignment = SWT.TOP;
        gridData.horizontalSpan = horizontalSpan;
        return gridData;
    }

    public void applyReadonlyFrameLayout(RCPComposite frame, RCPControl control,
                                         final int horizontalSpan)
    {
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.FILL;
        gd.verticalAlignment = SWT.FILL;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        // gd.horizontalSpan = horizontalSpan;
        control.setLayoutData(gd);
        control.setRcpParent(frame);
        applyFillHorizontalGrabLayoutData(frame, horizontalSpan, true);
    }

    /**
     * Creates a new GridData which is already initialised.
     * 
     * @return proper initialised GridData object. see also {@link GridData}.
     */
    public static GridData createDefaultLayoutData()
    {
        GridData gridData = new GridData();
        gridData.horizontalIndent = INDENT_FOR_DECORATORS;
        return gridData;
    }

}
