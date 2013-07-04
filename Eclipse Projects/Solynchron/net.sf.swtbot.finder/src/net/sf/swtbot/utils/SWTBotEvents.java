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
package net.sf.swtbot.utils;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.bidimap.DualTreeBidiMap;
import org.eclipse.swt.SWT;

/**
 * This maps SWT events to the SWTBot events.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotEvents.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 */
public abstract class SWTBotEvents {
	private static final BidiMap	EVENTS	= new DualTreeBidiMap();
	static {
		EVENTS.put("Activate", new Integer(SWT.Activate));
		EVENTS.put("Arm", new Integer(SWT.Arm));
		EVENTS.put("Close", new Integer(SWT.Close));
		EVENTS.put("Collapse", new Integer(SWT.Collapse));
		EVENTS.put("Deactivate", new Integer(SWT.Deactivate));
		EVENTS.put("DefaultSelection", new Integer(SWT.DefaultSelection));
		EVENTS.put("Deiconify", new Integer(SWT.Deiconify));
		EVENTS.put("Dispose", new Integer(SWT.Dispose));
		EVENTS.put("DragDetect", new Integer(SWT.DragDetect));
		EVENTS.put("EraseItem", new Integer(SWT.EraseItem));
		EVENTS.put("Expand", new Integer(SWT.Expand));
		EVENTS.put("FocusIn", new Integer(SWT.FocusIn));
		EVENTS.put("FocusOut", new Integer(SWT.FocusOut));
		EVENTS.put("HardKeyDown", new Integer(SWT.HardKeyDown));
		EVENTS.put("HardKeyUp", new Integer(SWT.HardKeyUp));
		EVENTS.put("Help", new Integer(SWT.Help));
		EVENTS.put("Hide", new Integer(SWT.Hide));
		EVENTS.put("Iconify", new Integer(SWT.Iconify));
		EVENTS.put("KeyDown", new Integer(SWT.KeyDown));
		EVENTS.put("KeyUp", new Integer(SWT.KeyUp));
		EVENTS.put("MeasureItem", new Integer(SWT.MeasureItem));
		EVENTS.put("MenuDetect", new Integer(SWT.MenuDetect));
		EVENTS.put("Modify", new Integer(SWT.Modify));
		EVENTS.put("MouseDoubleClick", new Integer(SWT.MouseDoubleClick));
		EVENTS.put("MouseDown", new Integer(SWT.MouseDown));
		EVENTS.put("MouseEnter", new Integer(SWT.MouseEnter));
		EVENTS.put("MouseExit", new Integer(SWT.MouseExit));
		EVENTS.put("MouseHover", new Integer(SWT.MouseHover));
		EVENTS.put("MouseMove", new Integer(SWT.MouseMove));
		EVENTS.put("MouseUp", new Integer(SWT.MouseUp));
		EVENTS.put("MouseWheel", new Integer(SWT.MouseWheel));
		EVENTS.put("Move", new Integer(SWT.Move));
		EVENTS.put("Paint", new Integer(SWT.Paint));
		EVENTS.put("PaintItem", new Integer(SWT.PaintItem));
		EVENTS.put("Resize", new Integer(SWT.Resize));
		EVENTS.put("Selection", new Integer(SWT.Selection));
		EVENTS.put("SetData", new Integer(SWT.SetData));
		EVENTS.put("Settings", new Integer(SWT.Settings)); // note: this event only goes to Display
		EVENTS.put("Show", new Integer(SWT.Show));
		EVENTS.put("Traverse", new Integer(SWT.Traverse));
		EVENTS.put("Verify", new Integer(SWT.Verify));
	}

	/**
	 * Converts the event to a string for display.
	 * 
	 * @param event the event.
	 * @return the string representation of the display.
	 */
	public static String toString(int event) {
		return (String) EVENTS.getKey(new Integer(event));
	}

	/**
	 * Lists all the events.
	 * 
	 * @return all the events.
	 */
	public static int[] events() {
		int[] events = new int[EVENTS.size()];
		int i = 0;

		MapIterator it = EVENTS.mapIterator();
		while (it.hasNext()) {
			it.next();
			Integer value = (Integer) it.getValue();
			events[i++] = value.intValue();
		}

		return events;
	}

}
