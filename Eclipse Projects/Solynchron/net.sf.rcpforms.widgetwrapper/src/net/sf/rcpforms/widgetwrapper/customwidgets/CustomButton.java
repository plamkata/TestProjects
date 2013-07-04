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

package net.sf.rcpforms.widgetwrapper.customwidgets;

import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;

/**
 * Ein Button, dessen Größe durch das übergebene Image bestimmt wird. Dabei ist der Button exakt so
 * groß wie das Image. Selbiges muss daher über einen zwei Pixel breiten Rand verfügen. Ferner muss
 * der Hintergrund des Images transparent sein.
 * 
 * @author schaefersn
 */
public class CustomButton extends ImageHyperlink
{
    boolean m_mouseDown = false;

    public CustomButton(Composite parent, Image image)
    {
        super(parent, SWT.NONE);
        super.setImage(image);
        marginWidth = 0;
        marginHeight = 0;
        addMouseListener(new MouseListener()
        {
            public void mouseDoubleClick(MouseEvent e)
            {
            }

            public void mouseDown(MouseEvent e)
            {
                m_mouseDown = true;
                redraw();
            }

            public void mouseUp(MouseEvent e)
            {
                m_mouseDown = false;
                redraw();
            }

        });
    }

    @Override
    protected void handleExit(Event e)
    {
        super.handleExit(e);
        m_mouseDown = false;
        redraw();
    }

    @Override
    protected void paint(PaintEvent e)
    {
        GC gc = e.gc;
        Rectangle clientArea = getClientArea();
        if (clientArea.width == 0 || clientArea.height == 0)
            return;
        paintHyperlink(gc);
        if (getSelection())
        {
            Rectangle carea = getClientArea();
            gc.setForeground(getForeground());
            gc.drawFocus(1, 1, carea.width - 2, carea.height - 2);
        }
    }

    @Override
    protected void paintHyperlink(GC gc, Rectangle bounds)
    {
        Color foregroundColor = getForeground();
        Color backgroundColor = getBackground();
        Rectangle ibounds = getImage() != null ? getImage().getBounds() : new Rectangle(0, 0, 0, 0);
        gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        gc.fillRectangle(ibounds.x + 2, ibounds.y + 2, ibounds.width - 4, ibounds.height - 4);
        gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        gc.drawRectangle(ibounds.x + 1, ibounds.y + 1, ibounds.width - 2, ibounds.height - 2);
        gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        gc.drawRectangle(ibounds.x, ibounds.y, ibounds.width - 1, ibounds.height - 1);
        setForeground(foregroundColor);
        setBackground(backgroundColor);
        if (m_mouseDown)
        {
            bounds.x++;
            bounds.y++;
        }
        super.paintHyperlink(gc, bounds);
        if (m_mouseDown)
        {
            bounds.x--;
            bounds.y--;
        }
    }

}
