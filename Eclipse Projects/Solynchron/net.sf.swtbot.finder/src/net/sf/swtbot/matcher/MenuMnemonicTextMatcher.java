package net.sf.swtbot.matcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Extends the {@link MnemonicTextMatcher} by matching only the text on the menu item, and not the accelerator. For e.g.
 * if a menu item has text "&New\tCTRL+N" then a MenuMnemonicTextMatcher will only match "New".
 * 
 * @see org.eclipse.swt.widgets.MenuItem#setText(String)
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: MenuMnemonicTextMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public class MenuMnemonicTextMatcher extends MnemonicTextMatcher {

	/**
	 * Constructs the menu mnemonic text matcher.
	 * 
	 * @param text the mnemonic to match on the {@link org.eclipse.swt.widgets.Widget}
	 */
	public MenuMnemonicTextMatcher(String text) {
		super(text);
	}

	/**
	 * Extends the behavior of MnemonicTextMatcher by removing "shortcut sequences" in the menu text.
	 * 
	 * @see net.sf.swtbot.matcher.TextMatcher#getText(java.lang.Object)
	 * @param obj The object to get the text from.
	 * @return The newly formated string.
	 * @throws NoSuchMethodException if the method "getText" does not exist on the object.
	 * @throws IllegalAccessException if the java access control does not allow invocation.
	 * @throws InvocationTargetException if the method "getText" throws an exception.
	 * @see Method#invoke(Object, Object[])
	 */
	String getText(Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return super.getText(obj).split("\t")[0];
	}
}
