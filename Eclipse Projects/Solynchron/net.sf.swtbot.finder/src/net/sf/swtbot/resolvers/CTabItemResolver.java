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

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Resolves {@link CTabItem}s.
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: CTabItemResolver.java 802 2008-06-26 03:57:38Z kpadegaonkar $
 */
public class CTabItemResolver implements IChildrenResolver, IParentResolver {

	public List getChildren(Widget w) {
		ArrayList children = new ArrayList();
		Control control = ((CTabItem) w).getControl();
		if (control != null)
			children.add(control);
		return children;
	}

	public boolean hasChildren(Widget w) {
		return canResolve(w) ? ((CTabItem) w).getControl() != null : false;
	}


	public boolean canResolve(Widget w) {
		return w instanceof CTabItem;
	}

	public Class[] getResolvableClasses() {
		return new Class[] { CTabItem.class };
	}

	public Widget getParent(Widget w) {
		return canResolve(w) ? ((CTabItem) w).getParent() : null;
	}

	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
