package com.fxclient.app.model;

import java.io.Serializable;

public class NetSettingBo implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private String            id;
    
    private String            ip;
    
    private String            port;
    
    private boolean           select;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getIp()
    {
        return ip;
    }
    
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    
    public String getPort()
    {
        return port;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
    public boolean isSelect()
    {
        return select;
    }
    
    public void setSelect(boolean select)
    {
        this.select = select;
    }
}
