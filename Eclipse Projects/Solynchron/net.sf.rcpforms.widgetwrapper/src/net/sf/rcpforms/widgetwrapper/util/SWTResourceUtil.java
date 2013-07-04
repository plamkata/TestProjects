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

package net.sf.rcpforms.widgetwrapper.util;

/**
 *
 * prototype: org.eclipse.ui.internal.util.SWTResourceUtil
 * @author mukhinr
 *
 */
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * SWTResourceUtil is a class that holds onto Colors, Fonts and Images and disposes them on
 * shutdown.
 * 
 * @deprecated TODO: remove
 */
public class SWTResourceUtil
{
    private SWTResourceUtil()
    {

    }

    /**
     * The cache of images that have been dispensed by this provider. Maps ImageDescriptor->Image.
     * Caches are all static to avoid creating extra system resources for very common images, font
     * and colors.
     */
    private static Map<ImageDescriptor, Image> s_imageTable = new Hashtable<ImageDescriptor, Image>(
            40);

    /**
     * The cache of colors that have been dispensed by this provider. Maps RGB->Color.
     */
    private static Map<RGB, Color> s_colorTable = new Hashtable<RGB, Color>(7);

    /**
     * The cache of fonts that have been dispensed by this provider. Maps FontData->Font.
     */
    private static Map<FontData, Font> s_fontTable = new Hashtable<FontData, Font>(7);

    /**
     * The cache of fonts that have been dispensed by this provider. Maps Regular Font -> Bold Font.
     */
    private static Map<Font, Font> s_boldFontTable = new Hashtable<Font, Font>(7);

    private static Map<Integer, Cursor> s_cursorTable = new Hashtable<Integer, Cursor>(5);

    public static final Image getCachedImage(final ImageDescriptor descriptor)
    {
        Image image = null;
        if (null != descriptor)
        {
            image = getImageTable().get(descriptor);
            if (null == image)
            {
                image = descriptor.createImage();
                getImageTable().put(descriptor, image);
            }
        }
        return image;
    }

    public static final Font getCachedFont(final FontData descriptor)
    {
        Font font = null;
        if (null != descriptor)
        {
            font = getFontTable().get(descriptor);
            if (null == font)
            {
                font = new Font(Display.getCurrent(), descriptor);
                getFontTable().put(descriptor, font);
            }
        }
        return font;
    }

    public static final Color getCachedColor(final RGB descriptor)
    {
        Color color = null;
        if (null != descriptor)
        {
            color = getColorTable().get(descriptor);
            if (null == color)
            {
                color = new Color(Display.getCurrent(), descriptor);
                getColorTable().put(descriptor, color);
            }
        }
        return color;
    }

    public static final Font getCachedBoldFont(final Font regularFont)
    {
        Font boldFont = null;
        if (null != regularFont)
        {
            boldFont = getBoldFontTable().get(regularFont);
            if (null == boldFont)
            {
                boldFont = createBoldFont(regularFont);
                getBoldFontTable().put(regularFont, boldFont);
            }
        }
        return boldFont;
    }

    public static final Cursor getCachedCursor(final int cursorID)
    {
        Cursor cursor = null;
        final Integer key = new Integer(cursorID);

        cursor = getCursorTable().get(key);
        if (null == cursor)
        {
            cursor = new Cursor(Display.getCurrent(), cursorID);
            getCursorTable().put(key, cursor);
        }
        return cursor;
    }

    /**
     * Disposes of all allocated images, colors and fonts when shutting down the plug-in.
     */
    public static final void shutdown()
    {
        if (s_imageTable != null)
        {
            for (Iterator<Image> i = s_imageTable.values().iterator(); i.hasNext();)
            {
                i.next().dispose();
            }
            s_imageTable = null;
        }
        if (s_colorTable != null)
        {
            for (Iterator<Color> i = s_colorTable.values().iterator(); i.hasNext();)
            {
                i.next().dispose();
            }
            s_colorTable = null;
        }
        if (s_fontTable != null)
        {
            for (Iterator<Font> i = s_fontTable.values().iterator(); i.hasNext();)
            {
                i.next().dispose();
            }
            s_fontTable = null;
        }

        if (s_fontTable != null)
        {
            // free only bold part
            for (Iterator<Font> i = s_boldFontTable.values().iterator(); i.hasNext();)
            {
                i.next().dispose();
            }
            s_boldFontTable = null;
        }
    }

    // grabed from org.eclipse.ui.internal.forms.widgets.FormUtil
    private static Font createBoldFont(Font regularFont)
    {
        FontData[] fontDatas = regularFont.getFontData();
        for (int i = 0; i < fontDatas.length; i++)
        {
            fontDatas[i].setStyle(fontDatas[i].getStyle() | SWT.BOLD);
        }
        return new Font(Display.getCurrent(), fontDatas);
    }

    /**
     * Get the Map of RGBs to Colors.
     * 
     * @return Returns the colorTable.
     * @uml.property name="colorTable"
     */
    private static Map<RGB, Color> getColorTable()
    {
        return s_colorTable;
    }

    /**
     * Return the map of FontDatas to Fonts.
     * 
     * @return Returns the fontTable.
     * @uml.property name="fontTable"
     */
    private static Map<FontData, Font> getFontTable()
    {
        return s_fontTable;
    }

    /**
     * Return the map of Regular Font to bold Fonts.
     * 
     * @return Returns the boldFontTable.
     * @uml.property name="boldFontTable"
     */
    private static Map<Font, Font> getBoldFontTable()
    {
        return s_boldFontTable;
    }

    /**
     * Return the map of ImageDescriptors to Images.
     * 
     * @return Returns the imageTable.
     */
    private static Map<ImageDescriptor, Image> getImageTable()
    {
        return s_imageTable;
    }

    /**
     * Return the map of Cursors.
     * 
     * @return Returns the cursor table.
     */
    private static Map<Integer, Cursor> getCursorTable()
    {
        return s_cursorTable;
    }
}
