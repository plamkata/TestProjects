package net.sf.rcpforms.bindingvalidation;

import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.IMessageManager;
import org.eclipse.ui.forms.IMessagePrefixProvider;

/**
 * dummy message manager which does nothing, for tests
 *
 * @author Marco van Meegen
 */
public class DummyMessageManager implements IMessageManager
{

    public void addMessage(Object key, String messageText, Object data, int type)
    {

    }

    public void addMessage(Object key, String messageText, Object data, int type, Control control)
    {

    }

    public String createSummary(IMessage[] messages)
    {
        return "Summary";
    }

    public int getDecorationPosition()
    {
        return 0;
    }

    public IMessagePrefixProvider getMessagePrefixProvider()
    {
        return null;
    }

    public boolean isAutoUpdate()
    {
        return false;
    }

    public void removeAllMessages()
    {

    }

    public void removeMessage(Object key)
    {

    }

    public void removeMessage(Object key, Control control)
    {

    }

    public void removeMessages()
    {
    }

    public void removeMessages(Control control)
    {
    }

    public void setAutoUpdate(boolean enabled)
    {
    }

    public void setDecorationPosition(int position)
    {
    }

    public void setMessagePrefixProvider(IMessagePrefixProvider provider)
    {
    }

    public void update()
    {
    }

}
