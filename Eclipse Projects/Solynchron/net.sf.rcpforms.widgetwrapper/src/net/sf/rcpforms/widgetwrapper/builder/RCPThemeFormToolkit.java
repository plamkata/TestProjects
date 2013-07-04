
package net.sf.rcpforms.widgetwrapper.builder;

import static org.eclipse.jface.resource.JFaceResources.DIALOG_FONT;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Wrapper class for org.eclipse.ui.forms.widgets.FormToolkit Adds support for JFaceResource fonts
 * and in eclipse rcp environement adds support for eclipse themes
 * 
 * @author Remo Loetscher
 * @version 0.1 early adopter
 */

public class RCPThemeFormToolkit extends FormToolkit
{
    /**
     * @see org.eclipse.jface.resource.JFaceResources
     */
    private Font dialogFont, dialogFontBold;

    // bannerFont,
    // defaultFont,
    // headerFont,
    // textFont,
    // viewerFont,
    // windowFont;

    public RCPThemeFormToolkit(Display display)
    {
        super(display);
        this.initializeJFaceFonts();
    }

    public RCPThemeFormToolkit(FormColors colors)
    {
        super(colors);
        this.initializeJFaceFonts();
    }

    private void initializeJFaceFonts()
    {
        dialogFont = JFaceResources.getDialogFont();
        dialogFontBold = JFaceResources.getFontRegistry().getBold(DIALOG_FONT);
    }

    @Override
    public Button createButton(Composite parent, String text, int style)
    {
        Button tmpButton = super.createButton(parent, text, style);
        tmpButton.setFont(dialogFont);

        return tmpButton;
    }

    @Override
    public Composite createComposite(Composite parent, int style)
    {
        Composite tmpComp = super.createComposite(parent, style);
        tmpComp.setFont(dialogFont);
        return tmpComp;
    }

    @Override
    public Composite createComposite(Composite parent)
    {
        Composite tmpComp = super.createComposite(parent);
        tmpComp.setFont(dialogFont);
        return tmpComp;
    }

    @Override
    public ExpandableComposite createExpandableComposite(Composite parent, int expansionStyle)
    {
        ExpandableComposite ecWrapper = new ExpandableComposite(parent, Window
                .getDefaultOrientation(), expansionStyle);
        // Programmer is intended to set the right font on textClient param!
        // {
        //		
        // public void setTextClient(Control textClient)
        // {
        // super.setTextClient(textClient);
        // textClient.setFont(dialogFont);
        // }
        //		
        // };
        ecWrapper.setMenu(parent.getMenu());
        adapt(ecWrapper, true, true);
        ecWrapper.setFont(dialogFontBold);
        return ecWrapper;

    }

    @Override
    public Form createForm(Composite parent)
    {
        Form tmpForm = super.createForm(parent);
        return tmpForm;
    }

    @Override
    public ScrolledForm createScrolledForm(Composite parent)
    {
        ScrolledForm tmpScrolledForm = super.createScrolledForm(parent);
        return tmpScrolledForm;
    }

    @Override
    public FormText createFormText(Composite parent, boolean trackFocus)
    {
        FormText tmpFormText = super.createFormText(parent, trackFocus);
        tmpFormText.setFont(dialogFont);
        return tmpFormText;
    }

    @Override
    public Hyperlink createHyperlink(Composite parent, String text, int style)
    {
        Hyperlink tmpHL = super.createHyperlink(parent, text, style);
        tmpHL.setFont(dialogFont);
        return tmpHL;
    }

    @Override
    public ImageHyperlink createImageHyperlink(Composite parent, int style)
    {
        ImageHyperlink hyperlink = super.createImageHyperlink(parent, style);
        hyperlink.setFont(dialogFont);
        return hyperlink;
    }

    @Override
    public Label createLabel(Composite parent, String text, int style)
    {
        Label tmpLabel = super.createLabel(parent, text, style);
        tmpLabel.setFont(dialogFont);
        return tmpLabel;
    }

    @Override
    public Label createLabel(Composite parent, String text)
    {
        Label tmpLabel = super.createLabel(parent, text);
        tmpLabel.setFont(dialogFont);
        return tmpLabel;
    }

    @Override
    public ScrolledPageBook createPageBook(Composite parent, int style)
    {
        ScrolledPageBook spb = super.createPageBook(parent, style);
        spb.setFont(dialogFont);
        return spb;
    }

    @Override
    public Section createSection(Composite parent, int sectionStyle)
    {
        Section tmpSection = super.createSection(parent, sectionStyle);
        tmpSection.setFont(dialogFontBold);
        if (tmpSection.getDescriptionControl() != null)
            tmpSection.getDescriptionControl().setFont(dialogFont);
        return tmpSection;
    }

    @Override
    public Label createSeparator(Composite parent, int style)
    {
        return super.createSeparator(parent, style);
    }

    @Override
    public Table createTable(Composite parent, int style)
    {
        return super.createTable(parent, style);
    }

    @Override
    public Text createText(Composite parent, String value, int style)
    {
        Text tmpText = super.createText(parent, value, style);
        tmpText.setFont(dialogFont);
        return tmpText;
    }

    @Override
    public Text createText(Composite parent, String value)
    {
        Text tmpText = super.createText(parent, value);
        tmpText.setFont(dialogFont);
        return tmpText;
    }

    @Override
    public Tree createTree(Composite parent, int style)
    {
        Tree tmpTree = super.createTree(parent, style);
        tmpTree.setFont(dialogFont);
        return tmpTree;
    }

    @Override
    public void adapt(Composite composite)
    {
        super.adapt(composite);
        composite.setFont(dialogFont);
    }

    @Override
    public void adapt(Control control, boolean trackFocus, boolean trackKeyboard)
    {
        super.adapt(control, trackFocus, trackKeyboard);
        control.setFont(dialogFont);
    }

    public void dispose()
    {
        super.dispose();
    }

}
