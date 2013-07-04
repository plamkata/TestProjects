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

package net.sf.rcpforms.test.table;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TableModel

{

    public static final String P_NAME = "name";

    public static final String P_AGE = "age";

    protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(

    this);

    private String name;

    private int age;

    /**
     * Constructor for WidgetFactoryManualTest.TableModel
     */

    public TableModel()

    {

        name = "name";

        age = 55;

    }

    /**
     * Constructor for TableModel
     * 
     * @param name
     * @param age
     */

    public TableModel(String name, int age)

    {

        this.name = name;

        this.age = age;

    }

    public String getName()

    {

        return name;

    }

    public void setName(String name)

    {

        String oldName = this.name;

        this.name = name;

        propertyChangeSupport.firePropertyChange(P_NAME, oldName, name);

    }

    public int getAge()

    {

        return age;

    }

    public void setAge(int age)

    {

        int oldAge = this.age;

        this.age = age;

        propertyChangeSupport.firePropertyChange(P_AGE, oldAge, age);

    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {

        propertyChangeSupport.addPropertyChangeListener(listener);

    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {

        propertyChangeSupport.removePropertyChangeListener(listener);

    }

}