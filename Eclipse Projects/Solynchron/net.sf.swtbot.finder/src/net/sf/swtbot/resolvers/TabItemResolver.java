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
import java.util.List;

import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: TabItemResolver.java 262 2008-01-11 04:12:51Z kpadegaonkar $
 */
public class TabItemResolver implements IChildrenResolver, IParentResolver {

	public boolean canResolve(Widget w) {
		return w instanceof TabItem;
	}

	public List getChildren(Widget w) {
		ArrayList children = new ArrayList();
		children.add(((TabItem) w).getControl());
		return children;
	}

	public Widget getParent(Widget w) {
		return (canResolve(w)) ? ((TabItem) w).getParent() : null;
	}

	public Class[] getResolvableClasses() {
		return new Class[] { TabItem.class };
	}

	public boolean hasChildren(Widget w) {
		return (canResolve(w)) ? ((TabItem) w).getControl() != null : false;
	}

	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
