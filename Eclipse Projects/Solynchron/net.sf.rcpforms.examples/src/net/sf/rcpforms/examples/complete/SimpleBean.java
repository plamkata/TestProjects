package net.sf.rcpforms.examples.complete;

import com.damnhandy.aspects.bean.Observable;

@Observable
public class SimpleBean {
    public static final String P_TEXT = "text";
    
    private String text;
    
    public String getText()
    {
        return text;
    }
    
    public void setText(String newValue)
    {
        text = newValue;
    }
    
}