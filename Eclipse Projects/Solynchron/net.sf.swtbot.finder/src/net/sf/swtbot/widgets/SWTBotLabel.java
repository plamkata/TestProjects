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
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;

/**
 * This represents a {@link Label} widget.
 * 
 * @author Stephen Paulin &lt;paulin [at] developerskingdom [dot] com&gt;
 * @version $Id: SWTBotLabel.java 811 2008-06-29 04:33:16Z kpadegaonkar $
 * @since 1.2
 */
public class SWTBotLabel extends AbstractSWTBot {

	/**
	 * Constructs an instance of this using the given finder and text to search for.
	 * 
	 * @param widget the widget
	 * @throws WidgetNotFoundException if the widget is null or disposed.
	 */
	public SWTBotLabel(Label widget) throws WidgetNotFoundException {
		super(widget);
	}

	/**
	 * Constructs an instance of this using the given finder and text to search for.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotLabel(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs an instance of this using the given finder and text to search for.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @param index the index of the control in case multiple controls are found.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotLabel(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.labelMatcher(text);
	}

	/**
	 * Gets the Label instance.
	 * 
	 * @return The {@link Label}.
	 */
	private Label getLabel() {
		return (Label) widget;
	}

	/**
	 * Return the Label's image or <code>null</code>.
	 * 
	 * @return the image of the label or <code>null</code>.
	 */
	public Image image() {
		return (Image) syncExec(new ObjectResult(){
			public Object run() {
				return getLabel().getImage();
			}
		});
	}

	/**
	 * Returns the alignment. The alignment style (LEFT, CENTER or RIGHT) is returned.
	 * 
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int alignment() {
		return syncExec(new IntResult() {
			public int run() {
				return getLabel().getAlignment();
			}
		});
	}

}
