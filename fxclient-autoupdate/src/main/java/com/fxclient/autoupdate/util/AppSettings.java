package com.fxclient.autoupdate.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class AppSettings
{
    private Properties         pro = new Properties();
    
    private static AppSettings _AppSettings;
    
    private AppSettings()
    {
        reload();
    }
    
    public static AppSettings getInstance()
    {
        if (_AppSettings == null)
        {
            _AppSettings = new AppSettings();
        }
        return _AppSettings;
    }
    
    public void reload()
    {
        FileInputStream in;
        try
        {
            in = new FileInputStream(System.getProperty("user.dir") + File.separator + "config" + File.separator + "AppSettings.properties");
            pro.load(in);
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public String getProperty(String key)
    {
        return pro.getProperty(key);
    }
    
    public void setProperty(String key, String value)
    {
        pro.setProperty(key, value);
    }
    
    public void save()
    {
        FileOutputStream oFile;
        try
        {
            oFile = new FileOutputStream(System.getProperty("user.dir") + File.separator + "config" + File.separator + "AppSettings.properties");
            pro.store(oFile, "Comment");
            oFile.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
