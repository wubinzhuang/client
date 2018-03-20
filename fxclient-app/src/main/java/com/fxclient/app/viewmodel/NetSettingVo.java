package com.fxclient.app.viewmodel;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import com.fxclient.framework.vo.SelectedVoBase;

public class NetSettingVo extends SelectedVoBase implements Serializable
{
    private static final long    serialVersionUID = 1L;
    
    private final StringProperty ip               = new SimpleStringProperty();
    
    private final StringProperty port             = new SimpleStringProperty();
    
    private final StringProperty test             = new SimpleStringProperty();
    
    public StringProperty testProperty()
    {
        return test;
    }
    
    public String getTest()
    {
        return test.get();
    }
    
    public void setTest(String test)
    {
        this.test.set(test);
    }
    
    public StringProperty ipProperty()
    {
        return ip;
    }
    
    public String getIp()
    {
        return ip.get();
    }
    
    public void setIp(String ip)
    {
        this.ip.set(ip);
    }
    
    public StringProperty portProperty()
    {
        return port;
    }
    
    public String getPort()
    {
        return port.get();
    }
    
    public void setPort(String port)
    {
        this.port.set(port);
    }
    
    @Override
    public String toString()
    {
        return "select " + getSelected() + " ip " + getIp() + " port " + getPort();
    }
}
