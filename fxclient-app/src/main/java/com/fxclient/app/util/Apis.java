package com.fxclient.app.util;

import com.fxclient.framework.util.AppSettings;

public class Apis
{
    public static final String CHECK_VERSION = "fxclient/version/checkVersion.do";
    
    public static final String DOLOGIN       = "fxclient/login/login.do";
    
    public static final String TEST          = "fxclient/login/test.do";
    
    public static String getHttpHost()
    {
        return "http://" + AppSettings.getInstance().getProperty("serverip") + ":" + AppSettings.getInstance().getProperty("serverPort") + "/";
    }
    
    public static String getHttpsHost()
    {
        return "https://" + AppSettings.getInstance().getProperty("serverip") + ":" + AppSettings.getInstance().getProperty("serverPort") + "/";
    }
}
