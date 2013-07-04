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
package net.sf.swtbot.resolvers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: DefaultParentResolver.java 802 2008-06-26 03:57:38Z kpadegaonkar $
 */
public class DefaultParentResolver extends Resolvable implements IParentResolver {

	public Widget getParent(Widget w) {
		if (!hasParent(w))
			return null;

		List resolvers = getResolver().getResolvers(w.getClass());

		for (Iterator iter = resolvers.iterator(); iter.hasNext();) {
			IParentResolver resolver = (IParentResolver) iter.next();
			if (resolver.hasParent(w))
				return resolver.getParent(w);
		}
		return null;
	}

	public boolean hasParent(Widget w) {

		List resolvers = getResolver().getResolvers(w.getClass());

		for (Iterator iter = resolvers.iterator(); iter.hasNext();) {
			IParentResolver resolver = (IParentResolver) iter.next();
			if (resolver.hasParent(w))
				return true;
		}
		return false;
	}

}
