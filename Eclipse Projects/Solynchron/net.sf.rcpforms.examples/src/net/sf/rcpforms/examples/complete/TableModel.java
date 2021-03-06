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

package net.sf.rcpforms.examples.complete;

import java.util.Arrays;
import java.util.Date;

import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.examples.complete.TestModel.Gender;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.widgets.Display;

import com.damnhandy.aspects.bean.Observable;

/**
 * Class TableModel represents the presentation model for the table part. WritableList is used,
 * since notification is built-in and much more efficient than bean indexed property notification.
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
@Observable
public class TableModel 
{

    public static final String P_DIRTY = "dirty";
    public static final String P_TEST_MODEL = "testModel";

    IListChangeListener listChangeListener = new IListChangeListener()
    {

        public void handleListChange(ListChangeEvent event)
        {
            // not interested in which kind of change,
            // only that it was changed!
            firePropertyChange(TableModel.P_DIRTY, null, null);
        }
    };

    /** list of elements to display in the table */
    private WritableList list;

    /** list of elements which are checked/selected in the table */
    private WritableList selectedList;
    
    /** variable used for master detail binding and therefore it has to be initalised, not null! */
    private TestModel testModel = new TestModel();

    public TableModel()
    {
        this(SWTObservables.getRealm(Display.getDefault()));
        TestModel[] list = {
                new TestModel("Mueller", new Date(10000000000L), true, 0, 1000.0, Gender.UNKNOWN, true),
                new TestModel("Meier1", new Date(20000000000L), false, 2, 500.0, Gender.MALE, true),
                new TestModel("Meier2", new Date(100000000000L), false, 2, 123.0, Gender.FEMALE, false),
                new TestModel("Meier3", new Date(40000000000L), false, 2, 11000.0, Gender.UNKNOWN, true),
                new TestModel("Meier4", new Date(30000000000L), false, 2, 2.0, Gender.UNKNOWN, false)};
        this.list.addAll(Arrays.asList(list));
    }
    
    public TestModel getTestModel()
    {
        return testModel;
    }
    
    public void setTestModel(TestModel newModel)
    {
        testModel = newModel;
    }

    /**
     * create writable lists using given realm
     */
    public TableModel(Realm realm)
    {
        Validate.notNull(realm, "Realm must not be null");
        list = new WritableList(realm);
        list.addListChangeListener(listChangeListener);
        selectedList = new WritableList(realm);
        selectedList.addListChangeListener(listChangeListener);
    }

    /**
     * @return Returns the list.
     */
    public WritableList getList()
    {
        return list;
    }

    /**
     * @return Returns the checkedList.
     */
    public WritableList getSelectedList()
    {
        return selectedList;
    }

    @Override
    public String toString()
    {
        StringBuffer result = new StringBuffer();
        result.append("TableModel.list[\n");
        for (Object element : list)
        {
            result.append("  " + element.toString() + "\n");
        }
        result.append("]\nselectedList[\n");
        for (Object element : selectedList)
        {
            result.append("  " + element.toString() + "\n");
        }
        result.append("]\n\n");
        return result.toString();
    }

}
