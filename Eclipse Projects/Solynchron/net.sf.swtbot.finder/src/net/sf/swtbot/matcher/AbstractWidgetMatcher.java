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
import net.sf.swtbot.utils.ClassUtils;
import net.sf.swtbot.utils.SWTUtils;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Widget;

/**
 * A matcher that logs the matching process, provides a template method that subclasses have to implement for matching.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: AbstractWidgetMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public abstract class AbstractWidgetMatcher implements IMatcher {

	/** The logger used for logging. */
	protected final Logger	log;

	/**
	 * Constructs a widget matcher.
	 */
	protected AbstractWidgetMatcher() {
		log = Logger.getLogger(getClass());
	}

	/**
	 * Calls the template method{@link #doMatch(Object)}, and performs some additional logging around the matcher.
	 * 
	 * @see IMatcher#match(Object)
	 * @return <code>true</code> if the match is successful with the passed in object. Otherwise <code>false</code>.
	 */
	public boolean match(Object obj) {
		boolean result = false;
		try {
			result = doMatch(obj);
			String text = "";
			if (log.isDebugEnabled()) {
				try {
					text = SWTUtils.getText(obj);
					if (text.length() > 20)
						text = text.substring(0, 20);
				} catch (Exception e) {
					text = "";
				}
				// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206714
				// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206715
				if (result)
					log.debug("matched " + ClassUtils.simpleClassName(obj) + " {" + text + "}, using matcher: " + description());
				if (!result && log.isTraceEnabled())
					log.trace("did not match " + ClassUtils.simpleClassName(obj) + " {" + text + "}, using matcher: " + description());
			}

			if (log.isTraceEnabled() && (obj instanceof Widget)) {
				PathGenerator pathGenerator = new PathGenerator();
				TreePath path = pathGenerator.getPath((Widget) obj);
				int segmentCount = path.getSegmentCount();

				String prefix = "";
				for (int i = 0; i < segmentCount - 1; i++)
					prefix += "    ";
				prefix += "+---";
				log.trace(prefix + "Widget: " + ClassUtils.simpleClassName(obj) + "{" + text + "}");
			}
		} catch (Exception e) {
			log.warn("Matcher threw an exception: ", e);
		}
		return result;
	}

	/**
	 * Performs the matching of the passed in object to the data contained within the matcher.
	 * 
	 * @see #match(Object)
	 * @param obj the object to match.
	 * @return <code>true</code> if the matcher can match the object, <code>false</code> otherwise
	 */
	protected abstract boolean doMatch(Object obj);

	public String toString() {
		String description = description();
		if ((description == null) || description.equals(""))
			description = ClassUtils.simpleClassName(this);
		return description;
	}
}
