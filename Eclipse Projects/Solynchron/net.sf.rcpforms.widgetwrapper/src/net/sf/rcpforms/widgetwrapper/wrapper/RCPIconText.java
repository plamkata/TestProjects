
package net.sf.rcpforms.widgetwrapper.wrapper;

import net.sf.rcpforms.widgetwrapper.customwidgets.IconText;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RCPIconText extends RCPControl
{
    Image image;

    public RCPIconText(Image icon)
    {
        this(null, icon, SWT.DEFAULT);

    }

    public RCPIconText(String labelText, Image icon, int style)
    {
        super(labelText, style);
        this.image = icon;
    }

    private IconText iconText;

    @Override
    protected Widget createWrappedWidget(FormToolkit formToolkit)
    {
        iconText = getFormToolkitEx().createIconText(getSWTParent(), image);
        return iconText;
    }
}
