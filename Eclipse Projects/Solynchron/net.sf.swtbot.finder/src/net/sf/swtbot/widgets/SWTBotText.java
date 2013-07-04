/*******************************************************************************
 * Copyright 2007 SWTBot, http://swtbot.org/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sf.swtbot.widgets;

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

/**
 * This represents a {@link Text} widget.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotText.java 824 2008-07-04 21:19:27Z kpadegaonkar $
 */
public class SWTBotText extends AbstractLabelSWTBot {

	private static final int	TYPE_INTERVAL	= 50;

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotText(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotText(Text w) throws WidgetNotFoundException {
		super(w);
	}

	/**
	 * Constructs an instance of this object.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @param index the index of the control in case multiple controls are found.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotText(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}

	protected Class nextWidgetType() {
		return Text.class;
	}

	/**
	 * Sets the text of the widget.
	 * 
	 * @param text the text to be set.
	 */
	public void setText(final String text) {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				getControl().setText(text);
			}
		});
	}

	/**
	 * Gets the control this represents.
	 * 
	 * @return The {@link Text} widget.
	 */
	private Text getControl() {
		return (Text) widget;
	}

	/**
	 * Types the string in the text box.
	 * 
	 * @param text the text to be typed.
	 * @since 1.2
	 */
	public void typeText(final String text) {
		typeText(text, TYPE_INTERVAL);
	}

	/**
	 * Types the string in the text box.
	 * 
	 * @param text the text to be typed.
	 * @param interval the interval between consecutive key strokes.
	 * @since 1.2
	 */
	public void typeText(final String text, int interval) {
		if (log.isDebugEnabled())
			log.debug("Inserting text:" + text + " into text " + SWTUtils.toString(widget));

		setFocus();
		for (int i = 0; i < text.length(); i++) {
			notifyKeyboardEvent(SWT.NONE, text.charAt(i));
			sleep(interval);
		}
	}

	/**
	 * FIXME need some work for CTRL|SHIFT + 1 the 1 is to be sent as '!' in this case.
	 * 
	 * @param modificationKey the modification key.
	 * @param c the character.
	 * @see Event#character
	 * @see Event#stateMask
	 * @since 1.2
	 */
	public void notifyKeyboardEvent(int modificationKey, char c) {
		setFocus();
		notifyKeyboardEvent(modificationKey, c, 0);
	}

	/**
	 * @param modificationKey the modification key.
	 * @param c the character.
	 * @param keyCode the keycode.
	 * @see Event#keyCode
	 * @see Event#character
	 * @see Event#stateMask
	 * @since 1.2
	 */
	public void notifyKeyboardEvent(int modificationKey, char c, int keyCode) {
		if (log.isDebugEnabled())
			log.debug("Enquing keyboard notification: " + toString(modificationKey, c));

		checkEnabled();

		notify(SWT.KeyDown, keyEvent(modificationKey, c, keyCode));
		notify(SWT.KeyUp, keyEvent(modificationKey, c, keyCode));

		setText(getText() + c);
	}

	/**
	 * @param c the character.
	 * @param modificationKey the modification key.
	 * @param keyCode the keycode.
	 * @return a key event with the specified keys.
	 * @see Event#keyCode
	 * @see Event#character
	 * @see Event#stateMask
	 * @since 1.2
	 */
	protected Event keyEvent(int modificationKey, char c, int keyCode) {
		Event keyEvent = createEvent();
		keyEvent.stateMask = modificationKey;
		keyEvent.character = c;
		keyEvent.keyCode = keyCode;

		return keyEvent;
	}

	private String toString(int modificationKey, char c) {
		String mod = "";
		if ((modificationKey & SWT.CTRL) != 0)
			mod += "SWT.CTRL + ";
		if ((modificationKey & SWT.SHIFT) != 0)
			mod += "SWT.SHIFT + ";
		int lastPlus = mod.lastIndexOf(" + ");
		if (lastPlus == (mod.length() - 3))
			mod = mod.substring(0, mod.length() - 3) + " + ";
		mod = mod + c;
		return mod;
	}
}
