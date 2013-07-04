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

import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

/**
 * CompositeImageHyperlink is a widget composed of 2 hyperlinks. One hyperlink shows text and the
 * second one shows a icon. Both hyperlinks may be attached separately allowing different actions on
 * each of them. The current implementation does show the icon to the right of the label
 * 
 * @author dietisheima
 */
public interface ICompositeImageHyperLink
{

    /**
     * returns the hyperlink for the image
     * 
     * @return
     */
    ImageHyperlink getImageHyperlink();

    /**
     * returns the hyperlink for the label
     */
    Hyperlink getLabelHyperlink();

}
