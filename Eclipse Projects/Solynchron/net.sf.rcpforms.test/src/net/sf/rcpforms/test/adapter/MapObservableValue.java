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
// Created on 27.05.2008

package net.sf.rcpforms.test.adapter;

import org.eclipse.core.databinding.observable.map.IMapChangeListener;
import org.eclipse.core.databinding.observable.map.MapChangeEvent;
import org.eclipse.core.databinding.observable.map.WritableMap;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.ValueDiff;

/**
 * observes one key in an WritableMap for changes
 * 
 * @author Marco van Meegen
 */
public class MapObservableValue extends AbstractObservableValue
{
    private WritableMap map;

    private String key;

    public MapObservableValue(WritableMap modelInstance, MapPropertyChain propertyChain)
    {
        map = modelInstance;
        key = propertyChain.getKey();
        modelInstance.addMapChangeListener(new IMapChangeListener()
        {
            public void handleMapChange(final MapChangeEvent event)
            {
                if (!nullEquals(event.diff.getNewValue(key), event.diff.getOldValue(key)))
                {
                    fireValueChange(new ValueDiff()
                    {
                        public Object getNewValue()
                        {
                            return event.diff.getNewValue(key);
                        }

                        @Override
                        public Object getOldValue()
                        {
                            return event.diff.getOldValue(key);
                        };

                    });
                }
            }
        });
    }

    protected boolean nullEquals(Object newValue, Object oldValue)
    {
        if (newValue == null)
        {
            return oldValue == null;
        }
        else
        {
            return newValue.equals(oldValue);
        }
    }

    @Override
    protected Object doGetValue()
    {
        return map.get(key);
    }

    public Object getValueType()
    {
        return map.containsKey(key) ? map.get(key).getClass() : Object.class;
    }

    @Override
    protected void doSetValue(Object value)
    {
        map.put(key, value);
    }
}
