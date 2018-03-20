package com.fxclient.app.util;

import java.io.*;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fxclient.app.model.NetSettingBo;
import com.fxclient.framework.appcontext.FxAppContext;

public class NetSettingUtil
{
    public static List<NetSettingBo> Load()
    {
        try
        {
            FileReader netstr = new FileReader(FxAppContext.getInstance().getBaseDir() + File.separator + "config" + File.separator + "netsttings");
            BufferedReader bufferedReader = new BufferedReader(netstr);
            String str = null;
            StringBuffer sb = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null)
            {
                sb.append(str);
            }
            bufferedReader.close();
            List<NetSettingBo> netsttrings = JSON.parseArray(sb.toString(), NetSettingBo.class);
            return netsttrings;
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public static void SaveNetSettings(List<NetSettingBo> netSettings)
    {
        String json = JSON.toJSONString(netSettings);
        try
        {
            Writer w = new FileWriter(FxAppContext.getInstance().getBaseDir() + File.separator + "config" + File.separator + "netsttings", false);
            w.write(json);
            w.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}
