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
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;

/**
 * This represents a {@link CLabel} widget.
 * 
 * @author Cedric Chabanois &lt;cchabanois [at] no-log [dot] org&gt;
 * @version $Id: SWTBotCLabel.java 810 2008-06-29 04:27:41Z kpadegaonkar $
 * @since 1.0
 */
public class SWTBotCLabel extends AbstractSWTBot {

	/**
	 * Constructs an instance of this using the given finder and text to search for.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotCLabel(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.cLabelMatcher(text);
	}

	/**
	 * Gets the CLabel instance.
	 * 
	 * @return The {@link CLabel}.
	 */
	private CLabel getCLabel() {
		return (CLabel) widget;
	}

	/**
	 * Return the CLabel's image or <code>null</code>.
	 * 
	 * @return the image of the label or null
	 */
	public Image image() {
		return getCLabel().getImage();
	}

	/**
	 * Returns the alignment. The alignment style (LEFT, CENTER or RIGHT) is returned.
	 * 
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int alignment() {
		return syncExec(new IntResult(){
			public int run() {
				return getCLabel().getAlignment();
			}
		});
	}

}
