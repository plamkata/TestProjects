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

import java.io.File;

import junit.framework.TestCase;
import net.sf.swtbot.utils.ClassUtils;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.widgets.AbstractSWTBot;

import org.eclipse.swt.widgets.Widget;

/**
 * The SWTBotTestCase extends the JUnit TestCase to provide extra capabilities for comparing widgets and other UI items.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotTestCase.java 837 2008-07-12 13:09:37Z kpadegaonkar $
 * @since 1.0
 */
public class SWTBotTestCase extends TestCase {

	/**
	 * An instance of SWTBot that may be used by extending items.
	 *
	 * @since 1.1
	 */
	protected SWTBot	bot	= new SWTBot();

	/**
	 * Asserts that two widgets do not refer to the same object.
	 *
	 * @see #assertNotSame(String, Object, Object)
	 * @param unexpected the object you don't expect
	 * @param actual the object to compare to unexpected
	 */
	public static void assertNotSameWidget(Widget unexpected, Widget actual) {
		String message = "expected not same was:<" + SWTUtils.toString(unexpected) + "> and:<" + SWTUtils.toString(actual) + ">";
		assertNotSameWidget(message, unexpected, actual);
	}

	/**
	 * Asserts the two widgets do not refer to the same object. The message will be used if the test fails.
	 *
	 * @param message the identifying message or null for the AssertionError
	 * @param unexpected the object you don't expect
	 * @param actual the object to compare to unexpected
	 */
	public static void assertNotSameWidget(String message, Widget unexpected, Widget actual) {
		assertNotSame(message, unexpected, actual);
	}

	/**
	 * Asserts that the <code>needle</code> is contained within the <code>hayStack</code>.
	 *
	 * @param needle the text to search in the <code>hayStack</code>.
	 * @param hayStack the text to look within.
	 */
	public static void assertContains(String needle, String hayStack) {
		if (!contains(needle, hayStack))
			fail("expected :<" + hayStack + "> to contain:<" + needle + ">");
	}

	private static boolean contains(String needle, String hayStack) {
		return (hayStack.indexOf(needle) >= 0);
	}

	/**
	 * Asserts that the <code>needle</code> is not present in the <code>hayStack</code>.
	 *
	 * @param needle the text to search in the <code>hayStack</code>.
	 * @param hayStack the text to look within.
	 * @since 1.2
	 */
	public static void assertDoesNotContain(String needle, String hayStack) {
		if (contains(needle, hayStack))
			fail("did not expect :<" + hayStack + "> to contain:<" + needle + ">");
	}

	/**
	 * Asserts that two widgets refer to the same widget.
	 *
	 * @param expected the expected widget
	 * @param actual the widget to compare to expected
	 */
	public static void assertSameWidget(Widget expected, Widget actual) {
		String message = "expected:<" + SWTUtils.toString(expected) + "> but was:<" + SWTUtils.toString(actual) + ">";
		assertSameWidget(message, expected, actual);
	}

	/**
	 * Asserts that two widgets refer to the same widgets.
	 *
	 * @param message the identifying message or <code>null</code> for the AssertionError
	 * @param expected the expected widget
	 * @param actual the widget to compare to expected
	 */
	public static void assertSameWidget(String message, Widget expected, Widget actual) {
		assertSame(message, expected, actual);
	}

	/**
	 * A helper to explicitly convey that the test has passed. Does nothing.
	 */
	public static void pass() {
		// Do nothing
	}

	/**
	 * Assert that the given string is the same as the widgets text.
	 *
	 * @param expected the expected text
	 * @param widget the widget to get the text from to compare.
	 */
	public static void assertText(String expected, Widget widget) {
		assertEquals(expected, SWTUtils.getText(widget));
	}

	/**
	 * Assert that the given string is the same as the widgets text.
	 *
	 * @param expected the expected text
	 * @param widget the widget to get the text from to compare.
	 */
	public static void assertText(String expected, AbstractSWTBot widget) {
		assertEquals(expected, widget.getText());
	}

	/**
	 * Assert that the text on the widget contains the expected text.
	 *
	 * @param expected the expected text.
	 * @param widget the widget
	 */
	public static void assertTextContains(String expected, Widget widget) {
		assertContains(expected, SWTUtils.getText(widget));
	}

	/**
	 * Assert that the text on the widget contains the expected text.
	 *
	 * @param expected the expected text
	 * @param widget the widget
	 */
	public static void assertTextContains(String expected, AbstractSWTBot widget) {
		assertContains(expected, widget.getText());
	}

	/**
	 * Assert that the text on the widget does not contain the expected text.
	 *
	 * @param expected the expected text
	 * @param widget the widget
	 */
	public static void assertTextDoesNotContain(String expected, Widget widget) {
		String hayStack = SWTUtils.getText(widget);
		if (contains(expected, hayStack))
			fail("did not expect:<" + hayStack + "> to contain:<" + expected + ">");
	}

	/**
	 * Assert that the text on the widget does not contain the expected text.
	 *
	 * @param expected the expected text
	 * @param widget the widget
	 */
	public static void assertTextDoesNotContain(String expected, AbstractSWTBot widget) {
		String hayStack = SWTUtils.getText(widget);
		if (contains(expected, hayStack))
			fail("did not expect:<" + hayStack + "> to contain:<" + expected + ">");
	}

	/**
	 * Asserts that the widget is enabled.
	 *
	 * @param widget the widget.
	 */
	public static void assertEnabled(AbstractSWTBot widget) {
		assertTrue("Expected widget " + widget + " to be enabled.", widget.isEnabled());
	}

	/**
	 * Asserts that the widget is not enabled.
	 *
	 * @param widget the widget.
	 */
	public static void assertNotEnabled(AbstractSWTBot widget) {
		assertTrue("Expected widget " + widget + " to be disabled.", !widget.isEnabled());
	}

	/**
	 * Asserts that the widget is visible.
	 *
	 * @param widget the widget.
	 */
	public static void assertVisible(AbstractSWTBot widget) {
		assertTrue("Expected widget " + widget + " to be visible.", widget.isVisible());
	}

	/**
	 * Asserts that the widget is not visible.
	 *
	 * @param widget the widget.
	 */
	public static void assertNotVisible(AbstractSWTBot widget) {
		assertTrue("Expected widget " + widget + " to be visible.", !widget.isVisible());
	}

	/**
	 * Overides the runBare method to TestCase to add the ability to capture a screen shot when an error is thrown. The
	 * screenshot is saved to a file in the current run directory inside a folder called {@code screenshots} that gets
	 * created if it does not already exist. The screenshot will be saved with the file {@code
	 * screenshots/screenshot-<classname>.<testname>.png}
	 *
	 * @see junit.framework.TestCase#runBare()
	 * @throws Throwable Thrown if an error occurs during running.
	 */
	public void runBare() throws Throwable {
		Throwable exception = null;
		try {
			super.runBare();
		} catch (Throwable running) {
			exception = running;
			captureScreenshot();
		}
		if (exception != null)
			throw exception;
	}

	/**
	 * Helper used by {@link #runBare()}.
	 *
	 * @see #runBare()
	 */
	private void captureScreenshot() {
		String fileName = "screenshots/screenshot-" + ClassUtils.simpleClassName(getClass()) + "." + getName() + ".png";
		new File("screenshots").mkdirs();
		captureScreenshot(fileName);
	}

	/**
	 * Allows the screen shot to be captured and saved to the given file.
	 *
	 * @see SWTUtils#captureScreenshot(String)
	 * @param fileName the filename to save screenshot to.
	 * @return <code>true</code> if the screenshot was created and saved, <code>false</code> otherwise.
	 */
	public static boolean captureScreenshot(String fileName) {
		return SWTUtils.captureScreenshot(fileName);
	}

}
