package com.fxclient.sysmanager.util;

import com.fxclient.framework.util.AppSettings;

public class Apis
{
    public static final String QUERY      = "fxclient/userManager/query.do";
    
    public static final String ADD_UPDATE = "fxclient/userManager/addOrUpdate.do";
    
    public static final String DELETE     = "fxclient/userManager/delete.do";
    
    public static final String EXPORT     = "fxclient/userManager/export.do";

    public static final String FutureKind_QUERY      = "fxclient/futureKindManager/query.do";

    public static final String FutureKind_ADD_UPDATE = "fxclient/futureKindManager/addOrUpdate.do";

    public static final String FutureKind_DELETE     = "fxclient/futureKindManager/delete.do";

    public static final String FutureKind_all     = "fxclient/futureKindManager/all.do";

    public static final String FutureKind_EXPORT     = "fxclient/futureKindManager/export.do";

    public static final String EXCHANGE_QUERY      = "fxclient/exchangeRateManager/query.do";

    public static final String EXCHANGE_ADD_UPDATE = "fxclient/exchangeRateManager/addOrUpdate.do";

    public static final String EXCHANGE_DELETE     = "fxclient/exchangeRateManager/delete.do";

    public static final String EXCHANGE_EXPORT     = "fxclient/exchangeRateManager/export.do";

    public static final String MARKETINFO_QUERY      = "fxclient/marketInfoManager/query.do";

    public static final String MARKETINFO_ADD_UPDATE = "fxclient/marketInfoManager/addOrUpdate.do";

    public static final String MARKETINFO_DELETE     = "fxclient/marketInfoManager/delete.do";

    public static final String MARKETINFO_EXPORT     = "fxclient/marketInfoManager/export.do";
    
    public static String getHttpHost()
    {
        return "http://" + AppSettings.getInstance().getProperty("serverip") + ":" + AppSettings.getInstance().getProperty("serverPort") + "/";
    }
    
    public static String getHttpsHost()
    {
        return "https://" + AppSettings.getInstance().getProperty("serverip") + ":" + AppSettings.getInstance().getProperty("serverPort") + "/";
    }
}
