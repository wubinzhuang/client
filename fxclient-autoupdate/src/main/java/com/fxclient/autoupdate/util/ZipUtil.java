package com.fxclient.autoupdate.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil
{
    @SuppressWarnings("unchecked")
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception
    {
        File zipFile = new File(zipFilePath);
        if (includeZipFileName)
        {
            String fileName = zipFile.getName().replace("/", File.separator).replace("\\", File.separator);
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            unzipFilePath = unzipFilePath + File.separator + fileName;
        }
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory())
        {
            unzipFileDir.mkdirs();
        }
        ZipEntry entry = null;
        String entryFilePath = null, entryDirPath = null;
        File entryFile = null, entryDir = null;
        int index = 0, count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        @SuppressWarnings("resource")
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        while (entries.hasMoreElements())
        {
            entry = entries.nextElement();
            entryFilePath = unzipFilePath + File.separator + entry.getName().replace("/", File.separator).replace("\\", File.separator);
            index = entryFilePath.lastIndexOf(File.separator);
            if (index != -1)
            {
                entryDirPath = entryFilePath.substring(0, index);
            }
            else
            {
                entryDirPath = "";
            }
            entryDir = new File(entryDirPath);
            if (!entryDir.exists() || !entryDir.isDirectory())
            {
                entryDir.mkdirs();
            }
            entryFile = new File(entryFilePath);
            if (entryFile.isDirectory() && !entryFile.exists())
            {
                entryFile.mkdirs();
            }
            else if (!entryFile.isDirectory())
            {
                if (entryFile.exists())
                {
                    forceDelete(entryFile);
                }
                bos = new BufferedOutputStream(new FileOutputStream(entryFile));
                bis = new BufferedInputStream(zip.getInputStream(entry));
                while ((count = bis.read(buffer, 0, bufferSize)) != -1)
                {
                    bos.write(buffer, 0, count);
                }
                bos.flush();
                bos.close();
            }
        }
    }
    
    public static boolean forceDelete(File f)
    {
        boolean result = false;
        int tryCount = 0;
        while (!result && tryCount++ < 10)
        {
            System.gc();
            result = f.delete();
        }
        return result;
    }
}
