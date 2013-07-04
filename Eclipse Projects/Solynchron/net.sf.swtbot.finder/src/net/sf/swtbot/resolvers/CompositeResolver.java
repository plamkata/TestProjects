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

import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Resolves {@link Composite}s and {@link Control}s
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: CompositeResolver.java 846 2008-07-21 13:47:53Z kpadegaonkar $
 */
public class CompositeResolver implements IChildrenResolver, IParentResolver {

	public boolean canResolve(Widget w) {
		// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206868
		return w instanceof Composite && !(w.getClass().getName().equals("org.eclipse.swt.widgets.DateTime"));
	}

	public List getChildren(Widget w) {
		// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206868
		if (w.getClass().getName().equals("org.eclipse.swt.widgets.DateTime"))
			return new ArrayList();
		return hasChildren(w) ? Arrays.asList(((Composite) w).getChildren()) : new ArrayList();
	}

	public Widget getParent(Widget w) {
		Composite parent = w instanceof Control ? ((Control) w).getParent() : null;
		if ((w instanceof Composite) && (parent instanceof TabFolder))
			if (parent instanceof TabFolder) {
				TabItem[] items = ((TabFolder) parent).getItems();
				return items[SWTUtils.widgetIndex(w)];
			}
		return parent;
	}

	public Class[] getResolvableClasses() {
		return new Class[] { Composite.class, Control.class };
	}

	public boolean hasChildren(Widget w) {
		// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206868
    // No "instanceof DateTime" is used in order to be compatible with PDE 3.2.
		if (w.getClass().getName().equals("org.eclipse.swt.widgets.DateTime"))
			return false;
		return canResolve(w) ? ((Composite) w).getChildren().length > 0 : false;
	}

	public boolean hasParent(Widget w) {
		return getParent(w) != null;
	}
}
