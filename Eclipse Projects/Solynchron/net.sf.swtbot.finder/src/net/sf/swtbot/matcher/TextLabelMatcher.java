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

import net.sf.swtbot.finder.PathGenerator;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 * Matches {@link Text} with the specified {@link Label} text. This does not take into account mnemonics.
 * <p>
 * This works by matching a {@link Text} that has a {@link Label} as its previous sibling.
 * </p>
 * 
 * @see MnemonicTextMatcher
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: TextLabelMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public class TextLabelMatcher extends AbstractWidgetMatcher {

	/** The identifying label text. */
	private final String				labelText;
	/**
	 * The mnemonic text matcher instance to use.
	 */
	private final MnemonicTextMatcher	mnemonicTextMatcher;

	/**
	 * Constructs an instance of the text label matcher.
	 * 
	 * @param labelText the label for a {@link Text} widget.
	 */
	public TextLabelMatcher(String labelText) {
		this.labelText = labelText;
		mnemonicTextMatcher = new MnemonicTextMatcher(labelText);
	}

	protected boolean doMatch(Object obj) {
		boolean match = new ClassMatcher(Text.class).doMatch(obj);
		if (match) {
			Text text = (Text) obj;
			Widget previousWidget = SWTUtils.previousWidget((Widget) obj);
			if ((previousWidget instanceof Label) && mnemonicTextMatcher.match(previousWidget))
				return true;

			if (previousWidget == null) {
				TreePath path = new PathGenerator().getPath(text);
				int segmentCount = path.getSegmentCount();
				for (int i = 0; i < segmentCount; i++) {
					previousWidget = (Widget) path.getSegment(i);
					if ((previousWidget instanceof Label) && mnemonicTextMatcher.match(previousWidget))
						return true;
				}
			}
		}
		return false;

	}

	public String description() {
		return "Matches TextBox with Label: " + labelText;
	}
}
