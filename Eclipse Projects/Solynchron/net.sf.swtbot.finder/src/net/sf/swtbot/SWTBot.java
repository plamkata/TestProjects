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
package net.sf.swtbot;

import java.util.Iterator;
import java.util.List;

import net.sf.swtbot.finder.ControlFinder;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.MenuFinder;
import net.sf.swtbot.matcher.AllMatcher;
import net.sf.swtbot.matcher.ClassMatcher;
import net.sf.swtbot.matcher.DecoratingAndMatcher;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.MnemonicTextMatcher;
import net.sf.swtbot.matcher.TextMatcher;
import net.sf.swtbot.utils.SWTBotPreferences;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.wait.ICondition;
import net.sf.swtbot.widgets.SWTBotButton;
import net.sf.swtbot.widgets.SWTBotCCombo;
import net.sf.swtbot.widgets.SWTBotCLabel;
import net.sf.swtbot.widgets.SWTBotCheckBox;
import net.sf.swtbot.widgets.SWTBotCombo;
import net.sf.swtbot.widgets.SWTBotDateTime;
import net.sf.swtbot.widgets.SWTBotExpandBar;
import net.sf.swtbot.widgets.SWTBotLabel;
import net.sf.swtbot.widgets.SWTBotList;
import net.sf.swtbot.widgets.SWTBotMenu;
import net.sf.swtbot.widgets.SWTBotRadio;
import net.sf.swtbot.widgets.SWTBotShell;
import net.sf.swtbot.widgets.SWTBotStyledText;
import net.sf.swtbot.widgets.SWTBotTabItem;
import net.sf.swtbot.widgets.SWTBotTable;
import net.sf.swtbot.widgets.SWTBotText;
import net.sf.swtbot.widgets.SWTBotToolbarButton;
import net.sf.swtbot.widgets.SWTBotToolbarButtonWithToolTip;
import net.sf.swtbot.widgets.SWTBotToolbarDropDownButton;
import net.sf.swtbot.widgets.SWTBotToolbarDropDownButtonWithTooltip;
import net.sf.swtbot.widgets.SWTBotTree;
import net.sf.swtbot.widgets.TimeoutException;
import net.sf.swtbot.widgets.WidgetNotFoundException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

/**
 * Instances of this class provide a convenience API to access widgets.
 * <p>
 * Note: The SWTEclipeBot should be used if testing an eclipse based product/plug-in.
 * </p>
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBot.java 848 2008-07-21 14:26:11Z kpadegaonkar $
 */
public class SWTBot {

	/** the delay between successive polling while waiting for a condition to be true. */
	public static final int	DEFAULT_POLL_DELAY	= 500;
	/**
	 * the default timeout while waiting for a condition to become true. Can be overridden by using
	 * -Dnet.sf.swtbot.search.timeout.
	 */
	public static final int	DEFAULT_TIMEOUT		= 5000;
	/** The display on which the bot operates on. */
	protected final Display	display;
	/** The finder used by the bot to find controls. */
	protected final Finder	finder;

	/**
	 * Constructs a bot.
	 */
	public SWTBot() {
		this(new ControlFinder(), new MenuFinder());
	}

	/**
	 * Constructs an instance of the bot using the given control finder and menu finder.
	 *
	 * @param controlFinder the {@link ControlFinder} used to identify and find controls.
	 * @param menuFinder the {@link MenuFinder} used to find menu items.
	 */
	public SWTBot(ControlFinder controlFinder, MenuFinder menuFinder) {
		this(new Finder(controlFinder, menuFinder));
	}

	/**
	 * Constructs a bot with the given finder.
	 *
	 * @param finder the finder.
	 */
	public SWTBot(Finder finder) {
		display = SWTUtils.display();
		this.finder = finder;
	}

	/**
	 * Gets the {@link Menu} with the given text in the main menu bar.
	 *
	 * @param menuText the text on the menu item
	 * @return a wrapper around a menuItem with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menu(String menuText) throws WidgetNotFoundException {
		return new SWTBotMenu(finder, menuText);
	}

	/**
	 * Gets the {@link Shell} with the the given text.
	 *
	 * @param shellText the text on the shell.
	 * @return a wrapper around a shell with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotShell shell(String shellText) throws WidgetNotFoundException {
		return new SWTBotShell(finder, shellText);
	}

	/**
	 * Gets the {@link Shell} with the given text and index.
	 *
	 * @param shellText the text on the shell.
	 * @param index the index of the shell in case there are multiple shells with the same text.
	 * @return a wrapper around a shell with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotShell shell(String shellText, int index) throws WidgetNotFoundException {
		return new SWTBotShell(finder, shellText, index);
	}

	/**
	 * Gets the {@link TabItem} with the given tab text.
	 *
	 * @param tabText the text on the tab.
	 * @return a wrapper around a TabItem with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTabItem tabItem(String tabText) throws WidgetNotFoundException {
		return new SWTBotTabItem(finder, tabText);
	}

	/**
	 * Gets the {@link Text} box with the given label text.
	 *
	 * @param labelText the text on the label.
	 * @return a wrapper around a TextBox with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotText textWithLabel(String labelText) throws WidgetNotFoundException {
		return textWithLabel(labelText, 0);
	}

	/**
	 * Gets the {@link Text} box with the given label text.
	 *
	 * @param labelText the text on the label.
	 * @param index the index of the Text widget.
	 * @return a wrapper around a TextBox with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotText textWithLabel(String labelText, int index) throws WidgetNotFoundException {
		return new SWTBotText(finder, labelText, index);
	}

	/**
	 * Gets the {@link Text} box with the given text.
	 *
	 * @param text the text on the text box.
	 * @return the 1st Text widget with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotText text(String text) throws WidgetNotFoundException {
		return text(text, 0);
	}

	/**
	 * Gets the {@link Text} box with the give text and index number.
	 *
	 * @param text the text on the text box.
	 * @param index the index of the Text widget.
	 * @return a wrapper around a Text widget matching the specified criteria.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotText text(String text, int index) throws WidgetNotFoundException {
		List findControls = finder.findControls(new DecoratingAndMatcher(new ClassMatcher(Text.class), new TextMatcher(text)));
		if (findControls.isEmpty())
			throw new WidgetNotFoundException("Could not find any text widget");
		return new SWTBotText((Text) findControls.get(index));
	}

	/**
	 * Gets the {@link Button} with the given text.
	 *
	 * @param buttonText the text on the button.
	 * @return a wrapper around a Button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotButton button(String buttonText) throws WidgetNotFoundException {
		return button(buttonText, 0);
	}

	/**
	 * Gets the {@link Button} with the given text and index.
	 *
	 * @param buttonText the text on the button.
	 * @param index the index of the button in case there are multiple buttons with the same text.
	 * @return a wrapper around a button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotButton button(String buttonText, int index) throws WidgetNotFoundException {
		return new SWTBotButton(finder, buttonText, index);
	}

	/**
	 * Gets the checkbox button with the given text.
	 *
	 * @param checkBoxText the text on the checkbox.
	 * @return a wrapper around a Checkbox Button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotCheckBox checkBox(String checkBoxText) throws WidgetNotFoundException {
		return new SWTBotCheckBox(finder, checkBoxText);
	}

	/**
	 * Gets the radio button with the given text.
	 *
	 * @param radioText the text on the radio button.
	 * @return a wrapper around a radio Button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotRadio radio(String radioText) throws WidgetNotFoundException {
		return new SWTBotRadio(finder, radioText);
	}

	/**
	 * Gets the {@link Combo} box with the given label text.
	 *
	 * @param comboLabel the text on the label.
	 * @param index the index of the combo in case there are multiple combo with the same text.
	 * @return a wrapper around a comboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @deprecated Use {@link #comboBoxWithLabel(String,int)} instead
	 * @since 1.2
	 */
	public SWTBotCombo comboBox(String comboLabel, int index) throws WidgetNotFoundException {
		return comboBoxWithLabel(comboLabel, index);
	}

	/**
	 * Gets the {@link Combo} box with the given label text.
	 *
	 * @param comboLabel the text on the label.
	 * @param index the index of the combo in case there are multiple combo with the same text.
	 * @return a wrapper around a comboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotCombo comboBoxWithLabel(String comboLabel, int index) throws WidgetNotFoundException {
		return new SWTBotCombo(finder, comboLabel, index);
	}

	/**
	 * Gets the {@link Combo} box with the given label text.
	 *
	 * @param comboLabel the text on the label.
	 * @return a wrapper around a comboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @deprecated Use {@link #comboBoxWithLabel(String)} instead
	 */
	public SWTBotCombo comboBox(String comboLabel) throws WidgetNotFoundException {
		return comboBoxWithLabel(comboLabel);
	}

	/**
	 * Gets the {@link Combo} box with the given label text.
	 *
	 * @param comboLabel the text on the label.
	 * @return a wrapper around a comboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotCombo comboBoxWithLabel(String comboLabel) throws WidgetNotFoundException {
		return comboBoxWithLabel(comboLabel, 0);
	}

	/**
	 * Gets the {@link CCombo} box with the given label text.
	 *
	 * @param comboLabel the text on the label.
	 * @param index the index of the combo in case there are multiple combo with the same text.
	 * @return a wrapper around a comboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @deprecated Use {@link #ccomboBoxWithLabel(String,int)} instead
	 */
	public SWTBotCCombo ccomboBox(String comboLabel, int index) throws WidgetNotFoundException {
		return ccomboBoxWithLabel(comboLabel, index);
	}

	/**
	 * Gets the {@link CCombo} box with the given text currently selected and the given index.
	 *
	 * @param comboText the selection on the combo.
	 * @param index the index of the button in case there are multiple ccombo with the same text.
	 * @return a wrapper around a ccomboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotCCombo ccomboBoxWithLabel(String comboText, int index) throws WidgetNotFoundException {
		return new SWTBotCCombo(finder, comboText, index);
	}

	/**
	 * Gets the {@link CCombo} box with the given text currently selected.
	 *
	 * @param comboText the selection on the combo.
	 * @return a wrapper around a ccomboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 * @deprecated Use {@link #ccomboBoxWithLabel(String)} instead
	 */
	public SWTBotCCombo ccomboBox(String comboText) throws WidgetNotFoundException {
		return ccomboBoxWithLabel(comboText);
	}

	/**
	 * Gets the {@link CCombo} box with the given text currently selected.
	 *
	 * @param comboText the selection on the combo.
	 * @return a wrapper around a ccomboBox with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotCCombo ccomboBoxWithLabel(String comboText) throws WidgetNotFoundException {
		return ccomboBoxWithLabel(comboText, 0);
	}

	/**
	 * Gets the {@link StyledText} widget with the given label text.
	 *
	 * @param labelText the label of the styled text.
	 * @return a wrapper around a styled text with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotStyledText styledTextWithLabel(String labelText) throws WidgetNotFoundException {
		return new SWTBotStyledText(finder, labelText);
	}

	/**
	 * Gets the {@link Table} with the given label.
	 *
	 * @param tableLabel the label on the table.
	 * @return a wrapper around a Table with the specified label
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTable tableWithLabel(String tableLabel) throws WidgetNotFoundException {
		return new SWTBotTable(finder, tableLabel);
	}

	/**
	 * Gets the {@link List} with the given label.
	 *
	 * @param label the label of the list.
	 * @return a wrapper around a List with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotList listWithLabel(String label) throws WidgetNotFoundException {
		return new SWTBotList(finder, label);
	}

	/**
	 * Gets the current active shell.
	 *
	 * @return the current active shell
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotShell activeShell() throws WidgetNotFoundException {
		return new SWTBotShell(finder.activeShell());
	}

	/**
	 * Gets the first available table.
	 *
	 * @return a wrapper around the table widget.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTable table() throws WidgetNotFoundException {
		return table(0);
	}

	/**
	 * Gets the table at the given index.
	 *
	 * @param index the index of the table
	 * @return a wrapper around the table widget.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTable table(int index) throws WidgetNotFoundException {
		List findControls = finder.findControls(new ClassMatcher(Table.class));
		if (findControls.isEmpty())
			throw new WidgetNotFoundException("Could not find any table");
		return new SWTBotTable((Table) findControls.get(index));
	}

	/**
	 * Gets the first table in the given shell.
	 *
	 * @param shell the shell containing a table.
	 * @return a wrapper around the table widget.
	 * @throws WidgetNotFoundException if the table is not found.
	 */
	public SWTBotTable table(SWTBotShell shell) throws WidgetNotFoundException {
		List findControls = finder.findControls(shell.widget, new ClassMatcher(Table.class), true);
		if (findControls.isEmpty())
			throw new WidgetNotFoundException("Could not find any table");
		return new SWTBotTable((Table) findControls.get(0));
	}

	/**
	 * Gets the first list widget.
	 *
	 * @return a wrapper around the list widget.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotList list() throws WidgetNotFoundException {
		return list(0);
	}

	/**
	 * Gets the list at the given index.
	 *
	 * @param index the index of the list
	 * @return a wrapper around the list widget.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotList list(int index) throws WidgetNotFoundException {
		List findControls = finder.findControls(new ClassMatcher(org.eclipse.swt.widgets.List.class));
		if (findControls.isEmpty())
			throw new WidgetNotFoundException("Could not find any table");
		return new SWTBotList((org.eclipse.swt.widgets.List) findControls.get(index));
	}

	/**
	 * Gets the {@link Tree} with the given label.
	 *
	 * @param treeLabel the label of the tree.
	 * @return a wrapper around an tree with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTree treeWithLabel(String treeLabel) throws WidgetNotFoundException {
		return new SWTBotTree(finder, treeLabel);
	}

	/**
	 * Gets the first tree found.
	 *
	 * @return a wrapper around the tree.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTree tree() throws WidgetNotFoundException {
		return tree(0);
	}

	/**
	 * Gets the tree at the given index.
	 *
	 * @param index the index of the tree
	 * @return a wrapper around the tree widget.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotTree tree(int index) throws WidgetNotFoundException {
		List findControls = finder.findControls(new ClassMatcher(Tree.class));
		if (findControls.isEmpty())
			throw new WidgetNotFoundException("Could not find any tree");
		return new SWTBotTree((Tree) findControls.get(index));
	}

	/**
	 * @param expandBarLabel the label of the expandbar.
	 * @return a wrapper around an tree with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotExpandBar expandBarWithLabel(String expandBarLabel) throws WidgetNotFoundException {
		return new SWTBotExpandBar(finder, expandBarLabel);
	}

	/**
	 * @return a wrapper around the first expandBar
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotExpandBar expandBar() throws WidgetNotFoundException {
		return expandBar(0);
	}

	/**
	 * @param index the index of the expandBar
	 * @return the index(th) tree
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotExpandBar expandBar(int index) throws WidgetNotFoundException {
		List findControls = finder.findControls(new ClassMatcher(ExpandBar.class));
		if (findControls.isEmpty())
			throw new WidgetNotFoundException("Could not find any tree");
		return new SWTBotExpandBar((ExpandBar) findControls.get(index));
	}

	/**
	 * Gets the {@link Button} with the given label text.
	 *
	 * @param labelText the label for the button.
	 * @return a wrapper around a button with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotButton buttonWithLabel(String labelText) throws WidgetNotFoundException {
		final int index = 0;
		return buttonWithLabel(labelText, index);
	}

	/**
	 * Gets the button with the given text and index number.
	 *
	 * @param labelText the label for the button.
	 * @param index the index of the button in case there are multiple buttons with the same text.
	 * @return a wrapper around a button with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotButton buttonWithLabel(String labelText, final int index) throws WidgetNotFoundException {
		return new SWTBotButton(finder, labelText, index) {

			protected Widget findWidget(int index) throws WidgetNotFoundException {
				return getNextWidget(super.findWidget(index));
			}

			protected Widget getNextWidget(Widget widget) {
				Class nextWidget = nextWidget();
				if (!nextWidget.isInstance(widget)) {
					List allControls = findControls(new AllMatcher());
					int indexOf = allControls.indexOf(widget);
					for (Iterator iterator = allControls.listIterator(indexOf); iterator.hasNext();) {
						Object next = iterator.next();
						if (nextWidget.isInstance(next))
							return (Widget) next;
					}
				}
				return widget;
			}

			protected IMatcher getMatcher() {
				return new MnemonicTextMatcher(text);
			}

			private Class nextWidget() {
				return Button.class;
			}
		};
	}

	/**
	 * Gets the data/time widget with the given label.
	 *
	 * @param label the label on the date/time widget.
	 * @return a wrapper around a DateTime widget with the specified label.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotDateTime dateTimeWithLabel(String label) throws WidgetNotFoundException {
		return new SWTBotDateTime(finder, label);
	}

	/**
	 * Waits until a specified condition evaluates to true.
	 *
	 * @param condition the {@link ICondition} to be evaluated.
	 * @throws TimeoutException if the condition does not evaluate to true after {@link #timeout()} milliseconds.
	 * @since 1.2
	 */
	public void waitUntil(ICondition condition) throws TimeoutException {
		waitUntil(condition, timeout());
	}

	/**
	 * Gets the timeout as specified by the SWTBot preferences.
	 *
	 * @return the value of "net.sf.swtbot.search.timeout" system property, or {@link #DEFAULT_TIMEOUT} if that cannot
	 *         be evaluated.
	 */
	protected int timeout() {
		try {
			return SWTBotPreferences.getTimeout();
		} catch (Exception e) {
			// do nothing
		}
		return DEFAULT_TIMEOUT;
	}

	/**
	 * Waits until the timeout is reached or the condition is met.
	 *
	 * @param condition the condition to be evaluated.
	 * @param timeout the timeout.
	 * @throws TimeoutException if the condition does not evaluate to true after timeout milliseconds.
	 * @since 1.2
	 */
	public void waitUntil(ICondition condition, int timeout) throws TimeoutException {
		waitUntil(condition, timeout, DEFAULT_POLL_DELAY);
	}

	/**
	 * Waits until the condition has been meet, or the timeout is reached. The interval is the delay between evaluating
	 * the condition after it has failed.
	 *
	 * @param condition the condition to be evaluated.
	 * @param timeout the timeout.
	 * @param interval The delay time.
	 * @throws TimeoutException if the condition does not evaluate to true after timeout milliseconds.
	 */
	private void waitUntil(ICondition condition, int timeout, long interval) throws TimeoutException {
		Assert.isTrue(interval >= 0, "interval value is negative");
		Assert.isTrue(timeout >= 0, "timeout value is negative");
		long limit = System.currentTimeMillis() + timeout;
		condition.init(this);
		while (true) {
			try {
				if (condition.test())
					return;
			} catch (Throwable e) {
				// do nothing
			}
			sleep(interval);
			if (System.currentTimeMillis() > limit)
				throw new TimeoutException("Timeout after: " + timeout + " ms.: " + condition.getFailureMessage());
		}
	}

	/**
	 * Sleeps for the given number of milliseconds.
	 *
	 * @param millis the time in milliseconds for which to sleep.
	 */
	public void sleep(long millis) {
		SWTUtils.sleep(millis);
	}

	/**
	 * Gets the global toolbar matching the given text (toolbar.getText()).
	 *
	 * @param text the text on the toolbar button.
	 * @return a wrapper around the toolbar button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotToolbarButton toolbarButton(String text) throws WidgetNotFoundException {
		return toolbarButton(text, 0);
	}

	/**
	 * Gets the global toolbar matching the given text (toolbar.getText()) and index.
	 *
	 * @param text the text on the toolbar button.
	 * @param index the index of the toolbar button.
	 * @return a wrapper around the toolbar button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotToolbarButton toolbarButton(String text, int index) throws WidgetNotFoundException {
		return new SWTBotToolbarButton(finder, text, index);
	}

	/**
	 * Gets the button on the global toolbar matching the tooltip text (tooltip.getTooltip()).
	 *
	 * @param tooltip the tooltip on the toolbar button.
	 * @return a wrapper around the toolbar button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotToolbarButton toolbarButtonWithTooltip(String tooltip) throws WidgetNotFoundException {
		return new SWTBotToolbarButtonWithToolTip(finder, tooltip);
	}

	/**
	 * Gets the button on the global toolbar matching the tooltip text (tooltip.getTooltip()) and matches the index
	 * number.
	 *
	 * @param tooltip the tooltip on the toolbar button.
	 * @param index the index of the button.
	 * @return a wrapper around the toolbar button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotToolbarButton toolbarButtonWithTooltip(String tooltip, int index) throws WidgetNotFoundException {
		return new SWTBotToolbarButtonWithToolTip(finder, tooltip, index);
	}

	/**
	 * Gets the drop down button on the toolbar matching the given text.
	 *
	 * @param text the text on the toolbar drop down button.
	 * @return a wrapper around the toolbar drop down button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotToolbarDropDownButton toolbarDropDownButton(String text) throws WidgetNotFoundException {
		return new SWTBotToolbarDropDownButton(finder, text);
	}

	/**
	 * Gets the drop down button on the toolbar matching the given text and the given index.
	 *
	 * @param text the text on the toolbar drop down button.
	 * @param index the index of the button.
	 * @return a wrapper around the toolbar drop down button with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotToolbarDropDownButton toolbarDropDownButton(String text, int index) throws WidgetNotFoundException {
		return new SWTBotToolbarDropDownButton(finder, text, index);
	}

	/**
	 * Gets the drop down button on the toolbar matching the given tooltip text (dropDown.getTooltip()).
	 *
	 * @param text the text on the toolbar drop down button.
	 * @return a wrapper around the toolbar drop down button with the specified tooltip.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotToolbarDropDownButton toolbarDropDownButtonWithTooltip(String text) throws WidgetNotFoundException {
		return new SWTBotToolbarDropDownButtonWithTooltip(finder, text);
	}

	/**
	 * Gets the drop down button on the toolbar matching the given tooltip text (dropDown.getTooltip()) and the given
	 * index.
	 *
	 * @param text the text on the toolbar drop down button.
	 * @param index the index of the button.
	 * @return a wrapper around the toolbar drop down button with the specified tooltip.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotToolbarDropDownButton toolbarDropDownButtonWithTooltip(String text, int index) throws WidgetNotFoundException {
		return new SWTBotToolbarDropDownButtonWithTooltip(finder, text, index);
	}

	/**
	 * Gets the list of shells found in the display.
	 *
	 * @return all the shells in the display.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotShell[] shells() throws WidgetNotFoundException {
		Shell[] shells = finder.getShells();
		SWTBotShell[] result = new SWTBotShell[shells.length];
		for (int i = 0; i < result.length; i++)
			result[i] = new SWTBotShell(shells[i]);
		return result;
	}

	/**
	 * Gets the {@link Label} widget matching the given text.
	 *
	 * @param labelText the text for the label.
	 * @return a wrapper around a Label with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotLabel label(String labelText) throws WidgetNotFoundException {
		return new SWTBotLabel(finder, labelText);
	}

	/**
	 * Gets the {@link Label} widget matching the given text.
	 *
	 * @param labelText the text for the label.
	 * @param index the index of the label.
	 * @return a wrapper around a Label with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotLabel label(String labelText, int index) throws WidgetNotFoundException {
		return new SWTBotLabel(finder, labelText, index);
	}

	/**
	 * Gets the {@link CLabel} widget matching the given text.
	 *
	 * @param labelText the text for the label.
	 * @return a wrapper around a CLabel with the specified text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.0
	 */
	public SWTBotCLabel clabel(String labelText) throws WidgetNotFoundException {
		return new SWTBotCLabel(finder, labelText);
	}

	/**
	 * Gets the display
	 *
	 * @return the display
	 * @since 1.0
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * Captures a screenshot to the given file name.
	 *
	 * @see SWTUtils#captureScreenshot(String)
	 * @param fileName the filename to save screenshot to.
	 * @return <code>true</code> if the screenshot was created and saved, <code>false</code> otherwise.
	 * @since 1.1
	 */
	public boolean captureScreenshot(String fileName) {
		return SWTUtils.captureScreenshot(fileName);
	}

}
