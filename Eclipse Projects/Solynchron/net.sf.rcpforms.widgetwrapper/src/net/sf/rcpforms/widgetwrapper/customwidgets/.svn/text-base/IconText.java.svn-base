
package net.sf.rcpforms.widgetwrapper.customwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.SharedScrolledComposite;

/**
 * A custom control to represent title and/or wrapped text
 * 
 * @author Remo Loetscher
 */
public class IconText extends Composite
{
    private Image m_image;

    private Label m_imageLabel;

    private String m_title = ""; //$NON-NLS-1$

    private String m_text = ""; //$NON-NLS-1$

    private StyledText m_titleLabel;

    private Label m_textLabel;

    private Composite textChild;

    // minimal text width hint
    private static final int TEXT_WIDTH_HINT = 350;

    /**
     * @param parent
     * @param style
     */
    public IconText(Composite parent, int style)
    {
        super(parent, style);
        setBackgroundMode(SWT.INHERIT_FORCE);
        init();
    }

    public void init()
    {
        GridLayout gl = new GridLayout(2, false);
        GridData gd = new GridData(GridData.FILL_BOTH);
        this.setLayout(gl);
        this.setLayoutData(gd);

        m_imageLabel = new Label(this, SWT.NONE);
        GridData gdIL = new GridData(SWT.LEFT, SWT.TOP, false, false);
        m_imageLabel.setLayoutData(gdIL);
        textChild = new Composite(this, SWT.NONE);
        GridData gdTextChild = new GridData(GridData.FILL_BOTH);
        textChild.setLayoutData(gdTextChild);
        textChild.setLayout(new GridLayout());

        m_titleLabel = new StyledText(textChild, SWT.SINGLE);
        m_titleLabel.setEnabled(false);
        GridData gdTitle = new GridData(GridData.FILL_HORIZONTAL);
        m_titleLabel.setLayoutData(gdTitle);
        this.setTitle(m_title);

        m_textLabel = new Label(textChild, SWT.WRAP);
        GridData gdText = new GridData(GridData.FILL_BOTH);
        gdText.widthHint = TEXT_WIDTH_HINT;
        // calculate height hint on text
        // gdText.heightHint = 150;
        m_textLabel.setLayoutData(gdText);
        this.setText(m_text);
    }

    /**
     * @param title string which represents a title. title will be bold. never null
     * @return
     */
    public IconText setTitle(String title)
    {
        this.m_title = title;
        this.m_titleLabel.setText(this.m_title);
        m_titleLabel.setStyleRange(new StyleRange(0, m_titleLabel.getText().length(), null, null,
                SWT.BOLD | SWT.LINE_SOLID));
        if (m_title == "") //$NON-NLS-1$
        {
            m_titleLabel.setVisible(false);
            ((GridData) m_titleLabel.getLayoutData()).exclude = true;
        }
        else
        {
            m_titleLabel.pack();
            ((GridData) m_titleLabel.getLayoutData()).exclude = false;
            // if title is longer then textlabel -> rescale text label also! text should have the
            // same width as title.
            if (m_titleLabel.getSize().x > TEXT_WIDTH_HINT)
            {
                this.adaptTextSize(this.calculateTextSize((m_titleLabel.getSize().x)));
            }
            m_titleLabel.setVisible(true);
        }
        this.updateLayout();

        return this;
    }

    /**
     * @param text string which represents a title. never null
     * @return
     */
    public IconText setText(String text)
    {
        this.m_text = text;
        this.m_textLabel.setText(this.m_text);
        if (m_text == "") //$NON-NLS-1$
        {
            m_textLabel.setVisible(false);
            ((GridData) m_textLabel.getLayoutData()).exclude = true;
        }
        else
        {
            GridData gd = (GridData) m_textLabel.getLayoutData();
            gd.exclude = false;
            // calculate height hint
            this.adaptTextSize(this.calculateTextSize((gd.widthHint)));
            m_textLabel.setVisible(true);

        }
        this.updateLayout();
        return this;
    }

    private void adaptTextSize(Point newHints)
    {
        GridData gd = (GridData) m_textLabel.getLayoutData();
        gd.widthHint = newHints.x;
        gd.heightHint = newHints.y;
    }

    private Point calculateTextSize(int widthHint)
    {
        return m_textLabel.computeSize(widthHint, SWT.DEFAULT, true);
    }

    /**
     * @param image
     * @return
     */
    public IconText setImage(Image image)
    {
        this.m_image = image;
        this.m_imageLabel.setImage(this.m_image);
        return this;
    }

    public void setFont(Font font)
    {
        super.setFont(font);
        m_imageLabel.setFont(font);
        m_titleLabel.setFont(font);
        m_textLabel.setFont(font);
    }

    public void setBackground(Color color)
    {
        super.setBackground(color);
        textChild.setBackground(color);
        m_imageLabel.setBackground(color);
        m_titleLabel.setBackground(color);
        m_textLabel.setBackground(color);
    }

    public void dispose()
    {
        super.dispose();
        m_imageLabel.dispose();
        m_titleLabel.dispose();
        m_textLabel.dispose();
        textChild.dispose();
    }

    private void updateLayout()
    {
        Composite c = this;
        while (c != null)
        {
            c.setRedraw(false);
            c = c.getParent();
            if (c instanceof SharedScrolledComposite)
            {
                break;
            }
        }
        c = this;
        while (c != null)
        {
            c.layout(true);
            c = c.getParent();
            if (c instanceof SharedScrolledComposite)
            {
                ((SharedScrolledComposite) c).reflow(true);
                break;
            }
        }
        c = this;
        while (c != null)
        {
            c.setRedraw(true);
            c = c.getParent();
            if (c instanceof SharedScrolledComposite)
            {
                break;
            }
        }
    }
}