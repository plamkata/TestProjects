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

import java.util.Set;

import org.eclipse.swt.widgets.Widget;

/**
 * Finds a resolver that can resolve the parent anc children of a widget.
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: Resolvable.java 802 2008-06-26 03:57:38Z kpadegaonkar $
 */
public class Resolvable implements IResolvable {

	/** The resolver */
	protected Resolver	resolver;

	/**
	 * Create a resolvable instance, with some default resolvers.
	 */
	public Resolvable() {
		this(new Resolver());
		resolver.addResolver(new CTabFolderResolver());
		resolver.addResolver(new TabFolderResolver());
		resolver.addResolver(new CTabItemResolver());
		resolver.addResolver(new TabItemResolver());
		resolver.addResolver(new ToolbarResolver());
		resolver.addResolver(new CompositeResolver());
		resolver.addResolver(new NullResolver());
	}

	/**
	 * Creates a resolvable using the given resolvable item. It is recommended that the default construction be used.
	 * 
	 * @param resolver the resolver
	 */
	public Resolvable(Resolver resolver) {
		this.resolver = resolver;
	}

	/**
	 * Returns {@code true} if any of the resolvers can resolve the widget.
	 * 
	 * @see net.sf.swtbot.resolvers.IResolvable#canResolve(org.eclipse.swt.widgets.Widget)
	 * @param w The widget to resolve.
	 * @return <code>true</code> if any of the resolvers can resolve the widget. Otherwise <code>false</code>.
	 */
	public boolean canResolve(Widget w) {
		Class[] resolvableClasses = getResolvableClasses();
		for (int i = 0; i < resolvableClasses.length; i++) {
			Class clazz = resolvableClasses[i];
			if (w.getClass().equals(clazz))
				return true;
		}

		return false;
	}

	/**
	 * Gets the complete list of widget types that this object can resolve.
	 * 
	 * @return the types that this resolver can resolve
	 */
	public Class[] getResolvableClasses() {
		Set keySet = resolver.map.keySet();
		Class[] result = new Class[keySet.size()];
		keySet.toArray(result);
		return result;
	}

	/**
	 * Gets the resolver.
	 * 
	 * @return the resolver.
	 */
	public Resolver getResolver() {
		return resolver;
	}

}
