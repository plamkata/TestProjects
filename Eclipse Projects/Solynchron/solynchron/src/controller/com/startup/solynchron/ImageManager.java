/**
 * 
 */
package com.startup.solynchron;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

/**
 * @author plamKaTa
 *
 */
public class ImageManager {

	public static final String IMG_FORM_BG = "formBg"; //$NON-NLS-1$
	public static final String IMG_LARGE = "large"; //$NON-NLS-1$
	public static final String IMG_HORIZONTAL = "horizontal"; //$NON-NLS-1$
	public static final String IMG_VERTICAL = "vertical"; //$NON-NLS-1$
	public static final String IMG_SAMPLE = "sample"; //$NON-NLS-1$
	public static final String IMG_WIZBAN = "wizban"; //$NON-NLS-1$
	public static final String IMG_LINKTO_HELP = "linkto_help"; //$NON-NLS-1$
	public static final String IMG_HELP_TOPIC = "help_topic"; //$NON-NLS-1$
	public static final String IMG_CLOSE = "close"; //$NON-NLS-1$
	
	public static void initialize(ImageRegistry registry) {
		registerImage(registry, IMG_FORM_BG, "form_banner.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_LARGE, "large_image.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_HORIZONTAL, "th_horizontal.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_VERTICAL, "th_vertical.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_SAMPLE, "sample.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_WIZBAN, "newprj_wiz.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_LINKTO_HELP, "linkto_help.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_HELP_TOPIC, "help_topic.gif"); //$NON-NLS-1$
		registerImage(registry, IMG_CLOSE, "close_view.gif"); //$NON-NLS-1$
	}
	
	private static void registerImage(ImageRegistry registry, 
			String key, String fileName) {
		try {
			IPath path = new Path("icons/" + fileName); //$NON-NLS-1$
			URL url = FileLocator.find(Activator.getDefault().getBundle(), path, null);
			if (url!=null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception ex) {
			Activator.log(ex);
		}
	}
	
	public static void dispose(ImageRegistry registry) {
		registry.remove(IMG_FORM_BG);
		registry.remove(IMG_LARGE);
		registry.remove(IMG_HORIZONTAL);
		registry.remove(IMG_VERTICAL);
		registry.remove(IMG_SAMPLE);
		registry.remove(IMG_WIZBAN);
		registry.remove(IMG_LINKTO_HELP);
		registry.remove(IMG_HELP_TOPIC);
		registry.remove(IMG_CLOSE);
	}

	public static Image getImage(String key) {
		return Activator.getDefault().getImageRegistry().get(key);
	}
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return Activator.getDefault().getImageRegistry().getDescriptor(key);
	}

}
