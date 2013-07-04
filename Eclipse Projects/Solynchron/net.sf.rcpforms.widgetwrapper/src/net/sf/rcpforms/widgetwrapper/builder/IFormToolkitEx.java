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

import net.sf.rcpforms.widgetwrapper.customwidgets.CustomButton;
import net.sf.rcpforms.widgetwrapper.customwidgets.HyperlinkLabel;
import net.sf.rcpforms.widgetwrapper.customwidgets.ICompositeImageHyperLink;
import net.sf.rcpforms.widgetwrapper.customwidgets.IconText;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;

/**
 * interface extending {@link FormToolkit} with additional methods used by the widget wrappers. If
 * you supply your own FormToolkit for RCPForms, you need to implement this interface.
 * 
 * @author Marco van Meegen
 * @version
 * @since
 */
public interface IFormToolkitEx
{

    public Button createButton(final Composite parent, final String text);

    public Button createButton(Composite parent, String text, int style);

    public Group createGroup(final Composite parent, final String text);

    public Group createGroup(final Composite parent, final String text, final int style);

    public Text createText(final Composite parent, final String text);

    public CCombo createCombo(final Composite parent);

    public CCombo createCombo(final Composite parent, final int style);

    public CCombo createCombo(final Composite parent, final int style, final boolean autoComplete);

    public CLabel createCLabel(final Composite parent);

    public Button createPushButton(final Composite parent, final String text);

    public Button createPushButton(final Composite parent, final Image image);

    public Button createRadioButton(final Composite parent, final String text);

    public Button createToggleButton(Composite parent, String text);

    public Button createCheckbox(final Composite parent, final String text);

    public Section createSection(final Composite parent, final int style);

    public Section createSection(final Composite parent);

    public Table createTable(final Composite parent);

    public HyperlinkLabel createHyperlinkLabel(final Composite parent, final String label);

    public Hyperlink createHyperlink(final Composite parent, final String hyperlinklabel);

    public ICompositeImageHyperLink createCompositeImageHyperlink(final Composite parent,
                                                                  final String label,
                                                                  final Image image);

    public IconText createIconText(final Composite parent, final Image image);

    /**
     * Creates a hyperlink with an image and a text. Both (icon and text) trigger the same
     * {@link IHyperlinkListener} (they are not controllable separately)
     * 
     * @param parent the parent composite
     * @param label the (text-)label for the hyperlink
     * @param image the image for the hyperlink
     * @return the {@link ImageHyperlink} created
     */
    public ImageHyperlink createHyperlink(final Composite parent, final String label,
                                          final Image image, final int style);

    public Tree createTree(final Composite parent);

    /**
     * returns the style for a single line text-field.
     * 
     * @param readonly indicates whether the text field shall be read-only or not
     * @return the style to be used for the text-field
     */
    public int getTextReadonlyStyle(final boolean readonly);

    /**
     * returns the style for a single line text-field.
     * 
     * @param readonly indicates whether the text field shall be read-only or not
     * @return the style to be used for the text-field
     */
    public int getMultilineTextReadonlyStyle(final boolean readonly);

    /**
     * Modifiziert den Font eines Control
     * 
     * @param control Das Control, dessen Font modifiziert werden soll
     * @param fontHeightDiv Der Unterschied zum vorhandenen Font (z.B. 2 für 2 Pixel größer als
     *            bisher)
     * @param bold true, wenn der Font Fett dargestellt werden soll, sonst false
     */
    public void modifyFont(final Control control, int fontHeightDiv, boolean bold);

    /**
     * Erzeugt einen Button mit Image, dessen Größe durch das Image betimmt wird
     */
    public CustomButton createCustomButton(Composite parent, Image image);

    /**
     * create a list widget with the given style
     * 
     * @param parent
     * @param style
     * @return
     */
    public List createList(Composite parent, int style);

    /**
     * create a list widget with default style
     * 
     * @param parent
     * @return list widget
     */
    public List createList(Composite parent);

    /**
     * create a default checkbox table
     * 
     * @param parent
     * @return
     */
    public Table createCheckboxTable(Composite parent);

    /**
     * create a checkbox table with the given style, check style will be added.
     * 
     * @param parent
     * @return table with {@link SWT#CHECK} style set.
     */
    public Table createCheckboxTable(Composite parent, int style);

    /**
     * set the uneditable style for the control if this is supported. Controls not supporting the
     * uneditable style are set en/disabled.
     * 
     * @param control control to apply style to
     * @param uneditable uneditable style
     */
    public void setReadonly(final Control control, final boolean uneditable);

    /**
     * @param widget table widget to create viewer for
     * @return table viewer suited for the given table, if SWT.CHECK is used, a
     *         {@link CheckboxTableViewer} is returned
     */
    public TableViewer createTableViewer(Table widget);

    /**
     * @param widget tree widget to create viewer for
     * @return tree viewer suited for the given tree, if SWT.CHECK is used, a
     *         {@link CheckboxTreeViewer} is returned
     */
    public TreeViewer createTreeViewer(Tree widget);

}