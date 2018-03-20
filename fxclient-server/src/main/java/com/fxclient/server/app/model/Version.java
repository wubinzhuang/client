package com.fxclient.server.app.model;

public class Version
{
    private String lastVersion;
    
    private String previousVersion;
    
    private String url;
    
    private String versionInfo;
    
    public String getLastVersion()
    {
        return lastVersion;
    }
    
    public void setLastVersion(String lastVersion)
    {
        this.lastVersion = lastVersion;
    }
    
    public String getPreviousVersion()
    {
        return previousVersion;
    }
    
    public void setPreviousVersion(String previousVersion)
    {
        this.previousVersion = previousVersion;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public String getVersionInfo()
    {
        return versionInfo;
    }
    
    public void setVersionInfo(String versionInfo)
    {
        this.versionInfo = versionInfo;
    }
}
