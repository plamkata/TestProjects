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

import java.util.List;

import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: IChildrenResolver.java 364 2008-02-27 13:39:51Z kpadegaonkar $
 */
public interface IChildrenResolver extends IResolvable {

	/**
	 * @param w the widget
	 * @return the children of the specified widget
	 */
	public List getChildren(Widget w);

	/**
	 * @param w the widget
	 * @return <code>true</code> if the resolver can provide children of the specified widget, <code>false</code>
	 *         otherwise.
	 */
	public boolean hasChildren(Widget w);

}