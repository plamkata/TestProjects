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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: DefaultChildrenResolver.java 802 2008-06-26 03:57:38Z kpadegaonkar $
 */
public class DefaultChildrenResolver extends Resolvable implements IChildrenResolver {

	public List getChildren(Widget w) {
		List result = new ArrayList();

		if (!hasChildren(w))
			return result;

		List resolvers = this.resolver.getResolvers(w.getClass());

		for (Iterator iterator = resolvers.iterator(); iterator.hasNext();) {
			IChildrenResolver resolver = (IChildrenResolver) iterator.next();
			if (resolver.canResolve(w) && resolver.hasChildren(w)) {
				List children = resolver.getChildren(w);
				if (children != null) {
					result.addAll(children);
					return result;
				}
			}
		}
		return result;
	}

	public boolean hasChildren(Widget w) {
		List resolvers = this.resolver.getResolvers(w.getClass());
		for (Iterator iterator = resolvers.iterator(); iterator.hasNext();) {
			IChildrenResolver resolver = (IChildrenResolver) iterator.next();
			if (resolver.canResolve(w) && resolver.hasChildren(w))
				return true;
		}

		return false;
	}

}
