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
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.StringResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.utils.Position;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.Bullet;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.StyledTextContent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotStyledText.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotStyledText extends AbstractLabelSWTBot {

	private static final int	TYPE_INTERVAL	= 50;

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotStyledText(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param styledText the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotStyledText(Widget styledText) throws WidgetNotFoundException {
		super(styledText);
	}

	protected Class nextWidgetType() {
		return StyledText.class;
	}

	/**
	 * Sets the text into the styled text.
	 * 
	 * @param text the text to set.
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
	 * Gets the control.
	 * 
	 * @return The {@link StyledText}.
	 */
	private StyledText getControl() {
		return (StyledText) widget;
	}

	/**
	 * Notifies of the keyboard event.
	 * <p>
	 * FIXME need some work for CTRL|SHIFT + 1 the 1 is to be sent as '!' in this case.
	 * </p>
	 * 
	 * @param modificationKey the modification key.
	 * @param c the character.
	 * @see Event#character
	 * @see Event#stateMask
	 */
	public void notifyKeyboardEvent(int modificationKey, char c) {
		setFocus();
		notifyKeyboardEvent(modificationKey, c, 0);
	}

	/**
	 * Notifies of keyboard event.
	 * 
	 * @param modificationKey the modification key.
	 * @param c the character.
	 * @param keyCode the keycode.
	 * @see Event#keyCode
	 * @see Event#character
	 * @see Event#stateMask
	 */
	public void notifyKeyboardEvent(int modificationKey, char c, int keyCode) {
		if (log.isDebugEnabled())
			log.debug("Enquing keyboard notification: " + toString(modificationKey, c));
		checkEnabled();
		notify(SWT.KeyDown, keyEvent(modificationKey, c, keyCode));
		notify(SWT.KeyUp, keyEvent(modificationKey, c, keyCode));
	}

	/**
	 * Converts the given data to a string.
	 * 
	 * @param modificationKey The modification key.
	 * @param c The character.
	 * @return The string.
	 */
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

	/**
	 * Gets the key event.
	 * 
	 * @param c the character.
	 * @param modificationKey the modification key.
	 * @return a key event with the specified keys.
	 * @see Event#character
	 * @see Event#stateMask
	 */
	protected Event keyEvent(int modificationKey, char c) {
		return keyEvent(modificationKey, c, c);
	}

	/**
	 * Gets the key event.
	 * 
	 * @param c the character.
	 * @param modificationKey the modification key.
	 * @param keyCode the keycode.
	 * @return a key event with the specified keys.
	 * @see Event#keyCode
	 * @see Event#character
	 * @see Event#stateMask
	 */
	protected Event keyEvent(int modificationKey, char c, int keyCode) {
		Event keyEvent = createEvent();
		keyEvent.stateMask = modificationKey;
		keyEvent.character = c;
		keyEvent.keyCode = keyCode;
		return keyEvent;
	}

	/**
	 * Sets the caret at the specified location.
	 * 
	 * @param line the line numnber.
	 * @param column the column number.
	 */
	public void navigateTo(final int line, final int column) {
		if (log.isDebugEnabled())
			log.debug("Enquing navigation to location " + line + ", " + column + " in " + SWTUtils.toString(widget));
		checkEnabled();
		setFocus();
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled())
					log.debug("Navigating to location " + line + ", " + column + " in " + widget);
				getControl().setSelection(offset(line, column));
			}
		});
	}

	/**
	 * Gets the current position of the cursor.
	 * 
	 * @return the position of the cursor in the styled text.
	 */
	public Position cursorPosition() {
		return (Position) syncExec(new ObjectResult() {
			public Object run() {
				getControl().setFocus();
				int offset = getControl().getSelectionRange().x;
				int lineNumber = getControl().getContent().getLineAtOffset(offset);
				int offsetAtLine = getControl().getContent().getOffsetAtLine(lineNumber);
				int columnNumber = offset - offsetAtLine;
				return new Position(lineNumber, columnNumber);
			}
		});
	}

	/**
	 * Types the text at the given location.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 * @param text the text to be typed at the specified location
	 * @since 1.0
	 */
	public void typeText(int line, int column, String text) {
		navigateTo(line, column);
		typeText(text);
	}

	/**
	 * Inserts text at the given location.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 * @param text the text to be inserted at the specified location
	 */
	public void insertText(int line, int column, String text) {
		navigateTo(line, column);
		insertText(text);
	}

	/**
	 * Inserts text at the end.
	 * <p>
	 * FIXME handle line endings
	 * </p>
	 * 
	 * @param text the text to be inserted at the location of the caret.
	 */
	public void insertText(final String text) {
		checkEnabled();
		syncExec(new VoidResult() {
			public void run() {
				getControl().insert(text);
			}
		});
	}

	/**
	 * Types the text.
	 * <p>
	 * FIXME handle line endings
	 * </p>
	 * 
	 * @param text the text to be typed at the location of the caret.
	 * @since 1.0
	 */
	public void typeText(final String text) {
		typeText(text, TYPE_INTERVAL);
	}

	/**
	 * Types the text.
	 * <p>
	 * FIXME handle line endings
	 * </p>
	 * 
	 * @param text the text to be typed at the location of the caret.
	 * @param interval the interval between consecutive key strokes.
	 * @since 1.0
	 */
	public void typeText(final String text, int interval) {
		if (log.isDebugEnabled())
			log.debug("Inserting text:" + text + " into styledtext" + SWTUtils.toString(widget));
		setFocus();
		for (int i = 0; i < text.length(); i++) {
			notifyKeyboardEvent(SWT.NONE, text.charAt(i));
			sleep(interval);
		}
	}

	/**
	 * Gets the style for the given line.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 * @return the {@link StyleRange} at the specified location
	 */
	public StyleRange getStyle(final int line, final int column) {
		return (StyleRange) syncExec(new ObjectResult() {
			public Object run() {
				return getControl().getStyleRangeAtOffset(offset(line, column));
			}
		});
	}

	/**
	 * Gets the offset.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 * @return the character offset at the specified location in the styledtext.
	 * @see StyledTextContent#getOffsetAtLine(int)
	 */
	protected int offset(final int line, final int column) {
		return getControl().getContent().getOffsetAtLine(line) + column;
	}

	/**
	 * Selects the range.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 * @param length the length of the selection.
	 */
	public void selectRange(final int line, final int column, final int length) {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				int offset = offset(line, column);
				getControl().setSelection(offset, offset + length);
			}
		});
		notify(SWT.Selection);
	}

	/**
	 * Gets the current selection text.
	 * 
	 * @return the selection in the styled text
	 */
	public String getSelection() {
		return syncExec(new StringResult() {
			public String run() {
				return getControl().getSelectionText();
			}
		});
	}

	/**
	 * Gets the style information.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 * @param length the length.
	 * @return the styles in the specified range.
	 * @see StyledText#getStyleRanges(int, int)
	 */
	public StyleRange[] getStyles(final int line, final int column, final int length) {
		return (StyleRange[]) syncExec(new ObjectResult() {
			public Object run() {
				return getControl().getStyleRanges(offset(line, column), length);
			}

		});
	}

	/**
	 * Gets the text on the current line.
	 * 
	 * @return the text on the current line, without the line delimiters.
	 * @see SWTBotStyledText#getTextOnLine(int)
	 */
	public String getTextOnCurrentLine() {
		final Position currentPosition = cursorPosition();
		final int line = currentPosition.line;
		return getTextOnLine(line);
	}

	/**
	 * Gets the text on the line.
	 * <p>
	 * TODO: throw exception if the line is out of range.
	 * </p>
	 * 
	 * @param line the line number.
	 * @return the text on the given line number, without the line delimiters.
	 */
	public String getTextOnLine(final int line) {
		return syncExec(new StringResult() {
			public String run() {
				return getControl().getContent().getLine(line);
			}
		});
	}

	/**
	 * Checks if this has a bullet on the current line.
	 * 
	 * @return <code>true</code> if the styledText has a bullet on the given line, <code>false</code> otherwise.
	 * @see StyledText#getLineBullet(int)
	 */
	public boolean hasBulletOnCurrentLine() {
		return hasBulletOnLine(cursorPosition().line);
	}

	/**
	 * Gets if this has a bullet on the specific line.
	 * 
	 * @param line the line number.
	 * @return <code>true</code> if the styledText has a bullet on the given line, <code>false</code> otherwise.
	 * @see StyledText#getLineBullet(int)
	 */
	public boolean hasBulletOnLine(final int line) {
		return getBulletOnLine(line) != null;
	}

	/**
	 * Gets the bullet on the current line.
	 * 
	 * @return the bullet on the current line.
	 * @see StyledText#getLineBullet(int)
	 */
	public Bullet getBulletOnCurrentLine() {
		return getBulletOnLine(cursorPosition().line);
	}

	/**
	 * Gets the bullet on the given line.
	 * 
	 * @param line the line number.
	 * @return the bullet on the given line.
	 * @see StyledText#getLineBullet(int)
	 */
	public Bullet getBulletOnLine(final int line) {
		return (Bullet) syncExec(new ObjectResult() {
			public Object run() {
				return getControl().getLineBullet(line);
			}
		});
	}

	/**
	 * Selects the text on the specified line.
	 * 
	 * @param line the line number.
	 * @since 1.1
	 */
	public void selectLine(int line) {
		selectRange(line, 0, getTextOnLine(line).length());
	}

	/**
	 * Selects the text on the current line.
	 * 
	 * @since 1.1
	 */
	public void selectCurrentLine() {
		selectLine(cursorPosition().line);
	}
}
