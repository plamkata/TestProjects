
package net.sf.rcpforms.form;

import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * if a form should not show its messages in the form header, an implementor of this interface can
 * be handed over which should display the message.
 * 
 * @author lindoerfern, vanmeegenm
 */
public interface IMessageDisplay
{

    /**
     * see {@link ScrolledForm#setMessage(String, int, IMessage[])}
     * 
     * @param newMessage
     * @param newType
     */
    void setMessage(String newMessage, int newType, IMessage[] messages);

}
