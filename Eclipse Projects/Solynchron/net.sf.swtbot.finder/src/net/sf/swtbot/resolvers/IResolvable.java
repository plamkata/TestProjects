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

import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: IResolvable.java 364 2008-02-27 13:39:51Z kpadegaonkar $
 */
public interface IResolvable {

	/**
	 * @param w the widget
	 * @return <code>true</code> if this widget can resolve the widget, <code>false</code> otherwise
	 */
	public boolean canResolve(Widget w);

	/**
	 * @return the list of classes that this resolver can resolve
	 */
	public abstract Class[] getResolvableClasses();

}
