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
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: ToolbarResolver.java 262 2008-01-11 04:12:51Z kpadegaonkar $
 */
public class ToolbarResolver implements IChildrenResolver, IParentResolver {

	public boolean canResolve(Widget w) {
		return w instanceof ToolBar;
	}

	public List getChildren(Widget w) {
		return hasChildren(w) ? Arrays.asList(((ToolBar) w).getItems()) : new ArrayList();
	}

	public Widget getParent(Widget w) {
		return (canResolve(w)) ? ((ToolBar) w).getParent() : null;
	}

	public Class[] getResolvableClasses() {
		return new Class[] { ToolBar.class };
	}

	public boolean hasChildren(Widget w) {
		return (canResolve(w)) ? ((ToolBar) w).getItems().length > 0 : false;
	}

	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}

}
