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

import java.net.MalformedURLException;
import java.net.URL;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.widgetwrapper.Activator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Bundle;

/**
 * resource management for rcpforms, currently only contains the image registry for rcpforms
 * 
 * @author Marco van Meegen
 */
public class RCPFormsResources
{
    /**
     * The path to the icons in the resources.
     */
    private final static String ICONS_PATH = "$nl$/icons/full/";//$NON-NLS-1$

    /**
     * The constant <code>PREFIX</code>
     */
    private static final String PREFIX = "net.sf.rcpforms.widgetwrapper."; //$NON-NLS-1$

    public static final String IMG_KEY_CALENDAR = PREFIX + "calendar"; //$NON-NLS-1$

    private static ImageRegistry imageRegistry;

    public static URL newURL(String url_name)
    {
        try
        {
            return new URL(url_name);
        }
        catch (MalformedURLException e)
        {
            throw new RuntimeException("Malformed URL " + url_name, e); //$NON-NLS-1$
        }
    }

    public static ImageRegistry getImageRegistry()
    {
        if (imageRegistry == null)
        {
            // TODO: make sure this works for different displays
            imageRegistry = new ImageRegistry(Display.getCurrent() == null ? Display.getDefault()
                                                                          : Display.getCurrent());
            initializeDefaultImages();
        }
        return imageRegistry;
    }

    /**
     * Initialize default images in JFace's image registry.
     */
    private static void initializeDefaultImages()
    {

        Object bundle = null;
        try
        {
            bundle = Activator.getDefault() != null ? Activator.getDefault().getBundle() : null;
        }
        catch (NoClassDefFoundError exception)
        {
            // Test to see if OSGI is present
        }
        declareImage(bundle, IMG_KEY_CALENDAR, ICONS_PATH + "calendar_7.gif", Activator.class, //$NON-NLS-1$
                "images/calendar_7.gif"); //$NON-NLS-1$

    }

    /**
     * Declares a JFace image given the path of the image file (relative to the JFace plug-in). This
     * is a helper method that creates the image descriptor and passes it to the main
     * <code>declareImage</code> method.
     * 
     * @param bundle the {@link Bundle} or <code>null</code> of the Bundle cannot be found
     * @param key the symbolic name of the image
     * @param path the path of the image file relative to the base of the workbench plug-ins install
     *            directory
     * @param fallback the {@link Class} where the fallback implementation of the image is relative
     *            to
     * @param fallbackPath the path relative to the fallback {@link Class}
     */
    private static final void declareImage(Object bundle, String key, String path, Class fallback,
                                           String fallbackPath)
    {

        ImageDescriptor descriptor = null;

        if (bundle != null)
        {
            URL url = FileLocator.find((Bundle) bundle, new Path(path), null);
            if (url != null)
                descriptor = ImageDescriptor.createFromURL(url);
        }

        // If we failed then load from the backup file
        if (descriptor == null)
            descriptor = ImageDescriptor.createFromFile(fallback, fallbackPath);

        imageRegistry.put(key, descriptor);
    }

    /**
     * @param key one of the constants defined in this file
     * @return shared image maintained by this registry
     * @throws IllegalArgumentException if image could not be loaded by current class loader
     */
    public static Image getImage(String key)
    {
        Image result = getImageRegistry().get(key);
        Validate.notNull(result, "Image with key " + key + " could not be loaded"); //$NON-NLS-1$ //$NON-NLS-2$
        return result;
    }
}
