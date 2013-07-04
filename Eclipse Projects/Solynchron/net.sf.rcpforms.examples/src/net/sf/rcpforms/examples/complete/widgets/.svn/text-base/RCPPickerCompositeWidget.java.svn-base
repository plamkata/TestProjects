package net.sf.rcpforms.examples.complete.widgets;

import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.builder.GridLayoutFactory;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePicker;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RCPPickerCompositeWidget extends RCPComposite
{

    private RCPDatePicker picker;
    
    public RCPPickerCompositeWidget()
    {
        this.picker = new RCPDatePicker("");
    }
    
    
    @Override
    public void createUI(FormToolkit formToolkit)
    {
        super.createUI(formToolkit);
        GridBuilder widgetBuilder = new GridBuilder(formToolkit, getClientComposite(), 3);
        picker.setState(EControlState.MANDATORY, true);
        Composite composite = getClientComposite();
        GridLayout gl = (GridLayout) composite.getLayout();
        gl.marginWidth = 0;
        gl.marginHeight = 0;
        gl.marginTop = 0;
        gl.marginBottom = 0;
        composite.setLayout(gl);

        GridData gd = (GridData) composite.getLayoutData();
        if(gd == null)
            gd = GridLayoutFactory.createDefaultLayoutData();
        //calculate horizontal indent for the first widget. take care of the margin width of the grid layout
        //if margin width is greater than indent -> set indent to 0
//        gd.horizontalIndent = GridLayoutFactory.INDENT_FOR_DECORATORS < gl.marginWidth ? 0 : GridLayoutFactory.INDENT_FOR_DECORATORS - gl.marginWidth;
        gd.horizontalIndent = 0;
        composite.setLayoutData(gd);
        widgetBuilder.add(picker);
        picker.getText().getRCPHyperlink().setVisible(false);
    }

    @Override
    public void setLayoutData(Object layoutData)
    {
        if(layoutData instanceof GridData)
        {
            //clear horizontal indent to have an aligned client composite
            ((GridData)layoutData).horizontalIndent = 0;
        }
        super.setLayoutData(layoutData);
    }

    @Override
    public boolean isExtensible()
    {
        return false;
    }
}
