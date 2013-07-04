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

/**
 * Fügt ein AutoComplete zu einer Combobox hinzu. Es genügt somit die ersten Buchstaben einer
 * vorhandenen Auswahl einzugeben. TODO: make compliant to all platforms
 * 
 * @author schaefersn
 * @author cabassug
 */
public class ComboAutoComplete
{
    // CCombo combo;
    // int lastSelection = -1;
    //
    // public ComboAutoComplete(final CCombo combo) {
    // this.combo = combo;
    // combo.setEditable(true);
    // combo.addTraverseListener(new TraverseListener() {
    // // Ohne diesen Listener ist es nicht möglich das Control mit
    // // Shift-Tab zu verlassen
    // public void keyTraversed(TraverseEvent e) {
    // // System.out.println("keyTraverse " + e.getSource());
    // if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
    // combo.getShell().setFocus();
    // }
    // }
    // });
    //
    // combo.addKeyListener(new KeyListener() {
    //
    // public void keyPressed(KeyEvent e) {
    // }
    //
    // public void keyReleased(KeyEvent e) {
    // combo.setEditable(true);
    // if (e.character >= 0 && e.character < ' '
    // && e.character != Character.LINE_SEPARATOR
    // && e.character != 22 || e.character == 127)
    // return;
    // doAutoComplete(combo, true);
    // if (e.character == Character.LINE_SEPARATOR) {
    // combo.clearSelection();
    // return;
    // }
    // }
    //
    // });
    //
    // combo.addFocusListener(new FocusListener() {
    //
    // public void focusGained(FocusEvent e) {
    // if (e.display.msg.message != org.eclipse.swt.internal.win32.OS.WM_LBUTTONDOWN) {
    // // if (e.display.msg.message ==
    // // org.eclipse.swt.internal.win32.OS.WM_KEYDOWN) { //
    // // Alternative, ginge auch
    // // Focus gained via Tab
    // //remsy why should shell receive focus? -> problem with that call:
    // // can be possible that the blinking cursor is set in the combo, but another (text) widget
    // has the focus and gets therefore the key strokes!
    // // bug occurred after comment out the lines 100ff!
    // // combo.getShell().setFocus();
    // combo.setFocus();
    // }
    // }
    //
    // public void focusLost(FocusEvent e) {
    // doAutoComplete(combo, false);
    // }
    //
    // });
    //
    // combo.addSelectionListener(new SelectionListener(){
    //
    // public void widgetDefaultSelected(SelectionEvent e) {
    // }
    //
    // public void widgetSelected(SelectionEvent e) {
    // lastSelection = combo.getSelectionIndex();
    // }
    //
    // });
    // }
    //
    // private void doAutoComplete(CCombo combo, boolean complete) {
    // String text = combo.getText();
    // if (text.length() == 0) {
    // //remsy uncommented snippet -> why should default element be selected?
    // //e.g. user only "walk" through widgets by using tab -> why force to select an element?
    // // Default Element setzen:
    // // if (lastSelection >= 0 && lastSelection < combo.getItemCount()){
    // // combo.select(lastSelection);
    // // combo.notifyListeners(13, null);
    // // }else
    // // if (combo.getItemCount() > 0){
    // // combo.select(0);
    // // combo.notifyListeners(13, null);
    // // }
    // return;
    // }
    // if (!complete)
    // return;
    // String[] items = combo.getItems();
    // boolean found = false;
    // // Ergänzen:
    // for (int i = 0; i < items.length; i++) {
    // String item = items[i];
    // if (item.toLowerCase().startsWith(text.toLowerCase())) {
    // combo.select(i);
    // combo.setSelection(new Point(text.length(), item.length()));
    // combo.notifyListeners(13, null);
    // found = true;
    // break;
    // }
    // }
    // if (!found) {
    // // Korrigieren:
    // while (text.length() > 0 && !found) {
    // text = text.substring(0, text.length() - 1);
    // if (text.length() == 0) {
    //                    combo.setText(""); //$NON-NLS-1$
    // return;
    // }
    // found = false;
    // for (int i = 0; i < items.length; i++) {
    // String item = items[i];
    // if (item.toLowerCase().startsWith(text.toLowerCase())) {
    // combo.select(i);
    // combo.setSelection(new Point(text.length(), item
    // .length()));
    // combo.notifyListeners(13, null);
    // found = true;
    // break;
    // }
    // }
    // }
    // }
    // }
}
