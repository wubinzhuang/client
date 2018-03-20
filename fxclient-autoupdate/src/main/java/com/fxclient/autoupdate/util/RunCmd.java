package com.fxclient.autoupdate.util;

import java.io.IOException;

public class RunCmd
{
    public static void runCmd(String strcmd)
    {
        //
        Runtime rt = Runtime.getRuntime();
        try
        {
            rt.exec(strcmd);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }
}
