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
package net.sf.swtbot.matcher;

import java.lang.reflect.InvocationTargetException;

import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Widget;

/**
 * A factory for creating common matchers.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: WidgetMatcherFactory.java 810 2008-06-29 04:27:41Z kpadegaonkar $
 */
public abstract class WidgetMatcherFactory {

	/**
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 * @version $Id: WidgetMatcherFactory.java 810 2008-06-29 04:27:41Z kpadegaonkar $
	 */
	private static final class NullMatcher implements IMatcher {

		public String description() {
			return "Matches nothing";
		}

		public boolean match(Object w) {
			return false;
		}

	}

	/**
	 * A toolbar button matcher.
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class ToolbarButtonMatcher extends AbstractWidgetTextMatcher {

		/**
		 * Constructs an instance of this matcher with the given text matching.
		 * 
		 * @param text the text on the toolbar to match.
		 */
		public ToolbarButtonMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (ToolItem.class).
		 */
		protected Class getWidgetClass() {
			return ToolItem.class;
		}

		/**
		 * This performs the match and returns the results of the comparison.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#doMatch(java.lang.Object)
		 * @param w The widget to match.
		 * @return The results of the match. <code>true</code> if a match occurs. Otherwise <code>false</code>.
		 */
		protected boolean doMatch(Object w) {
			return super.doMatch(w) && !hasStyle((Widget) w, SWT.DROP_DOWN);
		}
	}

	/**
	 * A toolbar drop down matcher.
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class ToolbarDropDownButtonMatcher extends AbstractWidgetTextMatcher {

		/**
		 * Constructs this matcher with the given text to use.
		 * 
		 * @param text the text on the toolbar.
		 */
		public ToolbarDropDownButtonMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (ToolItem.class).
		 */
		protected Class getWidgetClass() {
			return ToolItem.class;
		}

		/**
		 * This performs the match and returns the results of the comparison.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#doMatch(java.lang.Object)
		 * @param w The widget to match.
		 * @return The results of the match. <code>true</code> if a match occurs. Otherwise <code>false</code>.
		 */
		protected boolean doMatch(Object w) {
			return super.doMatch(w) && hasStyle((Widget) w, SWT.DROP_DOWN);
		}
	}

	/**
	 * A static method used to check if this has a style set for the given widget.
	 * 
	 * @param w The widget to check the style.
	 * @param style The style to look for a match.
	 * @return <code>true</code> if the style is used within the widget.
	 */
	private static boolean hasStyle(Widget w, int style) {
		return SWTUtils.hasStyle(w, style);
	}

	/**
	 * A combo matcher.
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class ComboTextMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs a combo text matcher with the given text to use for matching.
		 * 
		 * @param text The text to compare with.
		 */
		private ComboTextMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (Combo.class).
		 */
		protected Class getWidgetClass() {
			return Combo.class;
		}
	}

	/**
	 * A ccombo matcher.
	 * 
	 * @author Cedric Chabanois &lt;cchabanois [at] no-log [dot] org&gt;
	 */
	private static final class CComboTextMatcher extends AbstractWidgetTextMatcher {

		/**
		 * Constructs a ccombo text matcher with the given text to use for matching.
		 * 
		 * @param text The text to compare with.
		 */
		private CComboTextMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (CCombo.class).
		 */
		protected Class getWidgetClass() {
			return CCombo.class;
		}
	}

	/**
	 * Matches {@link MenuItem}s with the specified text. Skips mnemonics, so to click on a menu item with the text
	 * "&amp;File" use "File".
	 * 
	 * @see MnemonicTextMatcher
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class MenuMatcher extends AbstractWidgetTextMatcher {

		/**
		 * Constructs the menu matcher to match the given text.
		 * 
		 * @param text The text to compare for.
		 */
		public MenuMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the MenuMnemonicTextMatcher instance for the text of this instance.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getTextMatcher()
		 * @return A new instance of {@link MenuMnemonicTextMatcher}.
		 */
		protected TextMatcher getTextMatcher() {
			return new MenuMnemonicTextMatcher(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (MenuItem.class).
		 */
		protected Class getWidgetClass() {
			return MenuItem.class;
		}

	}

	/**
	 * Matches Buttons with {@link SWT#CHECK}
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class CheckBoxMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs the checkbox matcher to match the given text.
		 * 
		 * @param text The text to compare for.
		 */
		private CheckBoxMatcher(String text) {
			super(text);
		}

		/**
		 * This performs the match and returns the results of the comparison.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#doMatch(java.lang.Object)
		 * @param w The widget to match.
		 * @return The results of the match. <code>true</code> if a match occurs. Otherwise <code>false</code>.
		 */
		protected boolean doMatch(Object w) {
			return super.doMatch(w) && hasStyle((Widget) w, SWT.CHECK);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (Button.class).
		 */
		protected Class getWidgetClass() {
			return Button.class;
		}
	}

	/**
	 * Matches Buttons with {@link SWT#RADIO}
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class RadioButtonMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs a Radio Button matcher with the given text to use for matching.
		 * 
		 * @param text The text to compare with.
		 */
		private RadioButtonMatcher(String text) {
			super(text);
		}

		/**
		 * This performs the match and returns the results of the comparison.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#doMatch(java.lang.Object)
		 * @param w The widget to match.
		 * @return The results of the match. <code>true</code> if a match occurs. Otherwise <code>false</code>.
		 */
		protected boolean doMatch(Object w) {
			if (!(w instanceof Button))
				return false;
			Button b = (Button) w;
			return super.doMatch(b) && hasStyle(b, SWT.RADIO);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (Button.class).
		 */
		protected Class getWidgetClass() {
			return Button.class;
		}
	}
	
	/**
	 * Matches {@link Label}s
	 * 
	 * @author Stephen Paulin &lt;paulin [at] developerskingdom [dot] com&gt;
	 */
	private static final class LabelMatcher extends AbstractWidgetTextMatcher {

		/**
		 * Constructs a CLabel text matcher with the given text to use for matching.
		 * 
		 * @param text the text on the {@link CLabel}.
		 */
		public LabelMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (Label.class).
		 */
		protected Class getWidgetClass() {
			return Label.class;
		}
	}

	/**
	 * Matches {@link CLabel}s
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class CLabelMatcher extends AbstractWidgetTextMatcher {

		/**
		 * Constructs a CLabel text matcher with the given text to use for matching.
		 * 
		 * @param text the text on the {@link CLabel}.
		 */
		public CLabelMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (CLabel.class).
		 */
		protected Class getWidgetClass() {
			return CLabel.class;
		}
	}

	/**
	 * Matches {@link Button}s
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class ButtonMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs a button text matcher with the given text to use for matching.
		 * 
		 * @param text The text to compare with.
		 */
		private ButtonMatcher(String text) {
			super(text);
		}

		/**
		 * This performs the match and returns the results of the comparison.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#doMatch(java.lang.Object)
		 * @param w The widget to match.
		 * @return The results of the match. <code>true</code> if a match occurs. Otherwise <code>false</code>.
		 */
		protected boolean doMatch(Object w) {
			return super.doMatch(w) && isPush((Button) w);
		}

		/**
		 * Checks if this is a push button type ({@link SWT#PUSH}).
		 * 
		 * @param button the button
		 * @return <code>true</code> if the button has {@link SWT#PUSH} style, <code>false</code> otherwise.
		 */
		protected boolean isPush(Button button) {
			return hasStyle(button, SWT.PUSH);
		}

		/**
		 * Checks if this is a checkbox button type ({@link SWT#CHECK}).
		 * 
		 * @param button the button
		 * @return <code>true</code> if the button has {@link SWT#CHECK} style, <code>false</code> otherwise.
		 */
		protected boolean isCheck(Button button) {
			return hasStyle(button, SWT.CHECK);
		}

		/**
		 * Checks if this is a radio button type ({@link SWT#RADIO}).
		 * 
		 * @param button the button
		 * @return <code>true</code> if the button has {@link SWT#RADIO} style, <code>false</code> otherwise.
		 */
		protected boolean isRadio(Button button) {
			return hasStyle(button, SWT.RADIO);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (Button.class).
		 */
		protected Class getWidgetClass() {
			return Button.class;
		}
	}

	/**
	 * Matches {@link TabItem}s
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 */
	private static final class TabItemMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs a Tab Item matcher with the given text to use for matching.
		 * 
		 * @param text the text on the {@link TabItem}.
		 */
		private TabItemMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (TabItem.class).
		 */
		protected Class getWidgetClass() {
			return TabItem.class;
		}
	}

	private static final class ShellMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs a Shell matcher with the given text to use for matching.
		 * 
		 * @param text the text on the {@link Shell}.
		 */
		private ShellMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (Shell.class).
		 */
		protected Class getWidgetClass() {
			return Shell.class;
		}
	}

	private static final class CTabItemMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs a CTabItem text matcher with the given text to use for matching.
		 * 
		 * @param text the text on the {@link CTabItem}.
		 */
		private CTabItemMatcher(String text) {
			super(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (CTabItem.class).
		 */
		protected Class getWidgetClass() {
			return CTabItem.class;
		}
	}

	/**
	 * A class for matching {@link ToolTip}s.
	 */
	private static final class ToolTipMatcher extends TextMatcher {
		/**
		 * Constructs a Tooltip text matcher with the given text to use for matching.
		 * 
		 * @param tooltip the text on the tooltip.
		 */
		public ToolTipMatcher(String tooltip) {
			super(tooltip);
		}

		/**
		 * This performs the match and returns the results of the comparison.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#doMatch(java.lang.Object)
		 * @param w The widget to match.
		 * @return The results of the match. <code>true</code> if a match occurs. Otherwise <code>false</code>.
		 */
		protected boolean doMatch(Object w) {
			try {
				return getTooltip(w).equals(text);
			} catch (Exception e) {
				// do nothing
			}
			return false;
		}

		/**
		 * Gets the tooltip of the widget.
		 * 
		 * @param w The widget to get the tooltip of.
		 * @return The string representing the tooltip of the widget.
		 * @throws NoSuchMethodException Thrown if the getTooltip method doesn't exist on the widget object (I.E. Not a
		 *             widget).
		 * @throws IllegalAccessException Thrown if the method is hidden (not public).
		 * @throws InvocationTargetException Thrown if the method is failed to be invoked.
		 */
		private String getTooltip(Object w) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
			return (String) SWTUtils.invokeMethod(w, "getToolTipText");
		}

	}

	/**
	 * A matcher for Toolbar button tooltip matching.
	 */
	private static final class ToolbarButtonToolTipMatcher extends AbstractWidgetTextMatcher {
		/**
		 * Constructs a Toolbar Button tooltip matcher with the given text to use for matching.
		 * 
		 * @param tooltip the text on the tooltip of a toolbar button.
		 */
		public ToolbarButtonToolTipMatcher(String tooltip) {
			super(tooltip);
		}

		/**
		 * The text matcher to use.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getTextMatcher()
		 * @return A new tooltip text matcher.
		 */
		protected TextMatcher getTextMatcher() {
			return new ToolTipMatcher(text);
		}

		/**
		 * Gets the class representing this matcher.
		 * 
		 * @see net.sf.swtbot.matcher.AbstractWidgetTextMatcher#getWidgetClass()
		 * @return The {@link Class} for this matcher (ToolItem.class).
		 */
		protected Class getWidgetClass() {
			return ToolItem.class;
		}
	}

	/**
	 * Matches drop down toolbar buttons with the specified tooltip.
	 * 
	 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
	 * @version $Id: WidgetMatcherFactory.java 810 2008-06-29 04:27:41Z kpadegaonkar $
	 */
	private static final class ToolbarDropDownButtonToolTipMatcher extends AbstractWidgetTextMatcher {
		public ToolbarDropDownButtonToolTipMatcher(String tooltip) {
			super(tooltip);
		}

		protected TextMatcher getTextMatcher() {
			return new ToolTipMatcher(text);
		}

		protected Class getWidgetClass() {
			return ToolItem.class;
		}

		protected boolean doMatch(Object w) {
			return super.doMatch(w) && hasStyle((Widget) w, SWT.DROP_DOWN);
		}
	}

	/**
	 * @param text the text on the shell.
	 * @return a matcher that matches {@link Shell}s with the specified text.
	 */
	public static IMatcher shellMatcher(String text) {
		return new ShellMatcher(text);
	}

	/**
	 * Gets an new instance of the tab item matcher.
	 * 
	 * @param text the text on the tab item.
	 * @return a matcher that matches {@link TabItem}s with the specified text.
	 */
	public static IMatcher tabItemMatcher(String text) {
		return new TabItemMatcher(text);
	}

	/**
	 * Gets an new instance of the button matcher.
	 * 
	 * @param text the text on the push buttons.
	 * @return a matcher that matches push {@link Button}s.
	 */
	public static IMatcher buttonMatcher(String text) {
		return new ButtonMatcher(text);
	}

	/**
	 * Gets an new instance of the radio button matcher.
	 * 
	 * @param text the text on the radio buttons.
	 * @return a matcher that matches radio {@link Button}s.
	 */
	public static IMatcher radioButtonMatcher(String text) {
		return new RadioButtonMatcher(text);
	}

	/**
	 * Gets an new instance of the checkbox matcher.
	 * 
	 * @param text the text on the checkbox buttons.
	 * @return a matcher that matches checkbox {@link Button}s.
	 */
	public static IMatcher checkBoxMatcher(String text) {
		return new CheckBoxMatcher(text);
	}

	/**
	 * Gets an new instance of the menu matcher.
	 * 
	 * @param text the text on the menus
	 * @return a matcher that matches {@link Menu}s.
	 */
	public static IMatcher menuMatcher(String text) {
		return new MenuMatcher(text);
	}

	/**
	 * Gets an new instance of the combo text matcher.
	 * 
	 * @param comboText the selection on the combo box.
	 * @return a matcher that matches {@link Combo}s.
	 */
	public static IMatcher comboTextMatcher(String comboText) {
		return new ComboTextMatcher(comboText);
	}

	/**
	 * Gets an new instance of the ccombo text matcher.
	 * 
	 * @param comboText the selection on the combo box.
	 * @return a matcher that matches {@link Combo}s.
	 * @since 1.0
	 */
	public static IMatcher ccomboTextMatcher(String comboText) {
		return new CComboTextMatcher(comboText);
	}

	/**
	 * Gets an new instance of the cTabItem matcher.
	 * 
	 * @param text the text on the custom tab item.
	 * @return a matcher that matches {@link CTabItem}s with the specified text.
	 */
	public static IMatcher cTabItemMatcher(String text) {
		return new CTabItemMatcher(text);
	}

	/**
	 * Gets an new instance of the toolbar button matcher.
	 * 
	 * @param text the text on the toolbar button.
	 * @return a matcher that matches {@link ToolItem}s.
	 */
	public static IMatcher toolbarButtonMatcher(String text) {
		return new ToolbarButtonMatcher(text);
	}

	/**
	 * Gets an new instance of the toolbar drop down button matcher.
	 * 
	 * @param text the text on the toolbar button.
	 * @return a matcher that matches {@link ToolItem}s.
	 * @since 1.2
	 */
	public static IMatcher toolbarDropDownButtonMatcher(String text) {
		return new ToolbarDropDownButtonMatcher(text);
	}

	/**
	 * Gets an new instance of the toolbar button tooltip matcher.
	 * 
	 * @param tooltip the tooltip on the toolbar button.
	 * @return a matcher that matches {@link ToolItem}s.
	 * @since 1.0
	 */
	public static IMatcher toolbarButtonToolTipMatcher(String tooltip) {
		return new ToolbarButtonToolTipMatcher(tooltip);
	}

	/**
	 * Gets an new instance of the toolbar drop down button tooltip matcher.
	 * 
	 * @param tooltip the tooltip on the toolbar button.
	 * @return a matcher that matches {@link ToolItem}s.
	 * @since 1.2
	 */
	public static IMatcher toolbarDropDownButtonToolTipMatcher(String tooltip) {
		return new ToolbarDropDownButtonToolTipMatcher(tooltip);
	}

	/**
	 * Gets an new instance of the null matcher (matches nothing).
	 * 
	 * @return a matcher that matches nothing.
	 */
	public static IMatcher nullMatcher() {
		return new NullMatcher();
	}

	/**
	 * Gets an new instance of the label text matcher.
	 * 
	 * @param text the text on the label.
	 * @return a matcher that matches {@link Label}s
	 * @since 1.2
	 */
	public static IMatcher labelMatcher(String text) {
		return new LabelMatcher(text);
	}

	/**
	 * Gets an new instance of the cLabel text matcher.
	 * 
	 * @param text the text on the clabel.
	 * @return a matcher that matches {@link CLabel}s
	 * @since 1.0
	 */
	public static IMatcher cLabelMatcher(String text) {
		return new CLabelMatcher(text);
	}
}
