package com.fxclient.framework.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils
{
    public static String getMd5(String pwd)
    {
        try
        {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bs = digest.digest(pwd.getBytes());
            String hexString = "";
            for (byte b : bs)
            {
                int temp = b & 255;
                if (temp < 16 && temp >= 0)
                {
                    hexString = hexString + "0" + Integer.toHexString(temp);
                }
                else
                {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}