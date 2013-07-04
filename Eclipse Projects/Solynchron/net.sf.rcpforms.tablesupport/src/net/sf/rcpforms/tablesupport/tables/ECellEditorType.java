/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.tablesupport.tables;

public enum ECellEditorType {
    TEXT(String.class), COMBO(Object.class), CHECK(Boolean.class), DATE(String.class);

    private Object valueType;

    private ECellEditorType(Object valueType)
    {
        this.valueType = valueType;
    }

    /**
     * type converter needs to convert from/to for this cell editor
     * 
     * @return
     */
    public Object getValueType()
    {
        return valueType;
    }
}