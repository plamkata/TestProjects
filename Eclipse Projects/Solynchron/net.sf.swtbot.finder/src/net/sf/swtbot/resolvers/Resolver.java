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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.collections.set.ListOrderedSet;

/**
 * A resolver that maps classes to the {@link IChildrenResolver}s that resolve the classes.
 * 
 * @see IChildrenResolver#getResolvableClasses()
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: Resolver.java 802 2008-06-26 03:57:38Z kpadegaonkar $
 */
public class Resolver {

	/** The map that maps classes to {@link IChildrenResolver}s */
	MultiValueMap	map	= MultiValueMap.decorate(new HashMap(), ListOrderedSet.class);

	/**
	 * Map all the classes that the resolver resolver to the resolver.
	 * 
	 * @param resolver the resolver.
	 * @see IChildrenResolver#getResolvableClasses()
	 */
	public void addResolver(IResolvable resolver) {
		Class[] resolvableClasses = resolver.getResolvableClasses();
		if ((resolvableClasses != null) && (resolvableClasses.length > 0))
			addResolver(resolver, resolvableClasses);
	}

	/**
	 * Gets the resolvers that match the given class.
	 * 
	 * @param clazz the class that should be resolved using the resolvers.
	 * @return the list of {@link Resolver}s that can resolve objects of type <code>clazz</code>
	 */
	public List getResolvers(Class clazz) {

		ListOrderedSet result = new ListOrderedSet();

		Collection resolvers = map.getCollection(clazz);

		if ((resolvers != null) && !resolvers.isEmpty())
			result.addAll(resolvers);
		else if (!Object.class.equals(clazz))
			result.addAll(getResolvers(clazz.getSuperclass()));

		return new ArrayList(result.asList());
	}

	/**
	 * Adds a new resolver to the list.
	 * 
	 * @param resolver The resolver to add.
	 * @param resolvableClasses The classes supported by the resolver.
	 */
	private void addResolver(IResolvable resolver, Class[] resolvableClasses) {
		for (int i = 0; i < resolvableClasses.length; i++) {
			Class clazz = resolvableClasses[i];
			map.put(clazz, resolver);
		}
	}
}
