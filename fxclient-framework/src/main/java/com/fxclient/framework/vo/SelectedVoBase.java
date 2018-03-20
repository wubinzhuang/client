package com.fxclient.framework.vo;

import java.io.Serializable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SelectedVoBase implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private BooleanProperty   selected         = new SimpleBooleanProperty();
    
    public boolean getSelected()
    {
        return selected.get();
    }
    
    public void setSelected(boolean selected)
    {
        this.selected.set(selected);
    }
    
    public BooleanProperty selectedProperty()
    {
        return selected;
    }
}
