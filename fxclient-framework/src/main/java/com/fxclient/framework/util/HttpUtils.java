package com.fxclient.framework.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class HttpUtils
{
    private static final OkHttpClient mOkHttpClient;
    static
    {
        File sdcache = new File(System.getProperty("user.dir") + File.separator + "cache");
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS).readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }
    
    public static Response execute(Request request) throws IOException
    {
        return mOkHttpClient.newCall(request).execute();
    }
    
    public static void enqueue(Request request, Callback responseCallback)
    {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
    
    public static void enqueue(Request request)
    {
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call arg0, IOException arg1)
            {
            }
            
            @Override
            public void onResponse(Call arg0, Response arg1) throws IOException
            {
            }
        });
    }
    
    public static String httpGetToStringWithCache(String url, Map<String, String> params) throws IOException
    {
        if (params != null)
        {
            url = attachHttpGetParams(url, params);
        }
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return responseUrl;
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static String httpGetToString(String url, Map<String, String> params) throws IOException
    {
        if (params != null)
        {
            url = attachHttpGetParams(url, params);
        }
        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return responseUrl;
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static <T> T httpGetToObjectWithCache(String url, Map<String, String> params, TypeReference<T> typeReference) throws IOException
    {
        if (params != null)
        {
            url = attachHttpGetParams(url, params);
        }
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return JSON.parseObject(responseUrl, typeReference);
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static <T> T httpGetToObject(String url, Map<String, String> params, TypeReference<T> typeReference) throws IOException
    {
        if (params != null)
        {
            url = attachHttpGetParams(url, params);
        }
        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return JSON.parseObject(responseUrl, typeReference);
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static String httpPostToStringWithCache(String url, Map<String, String> params, boolean encode) throws IOException
    {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null)
        {
            for (String key : params.keySet())
            {
                if (!encode)
                {
                    builder.add(key, params.get(key));
                }
                else
                {
                    builder.addEncoded(key, params.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return responseUrl;
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static String httpPostToString(String url, Map<String, String> params, boolean encode) throws IOException
    {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null)
        {
            for (String key : params.keySet())
            {
                if (!encode)
                {
                    builder.add(key, params.get(key));
                }
                else
                {
                    builder.addEncoded(key, params.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url).post(formBody).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return responseUrl;
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static <T> T httpPostToObjectWithCache(String url, Map<String, String> params, TypeReference<T> typeReference, boolean encode) throws IOException
    {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null)
        {
            for (String key : params.keySet())
            {
                if (!encode)
                {
                    builder.add(key, params.get(key));
                }
                else
                {
                    builder.addEncoded(key, params.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return JSON.parseObject(responseUrl, typeReference);
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static <T> T httpPostToObject(String url, Map<String, String> params, TypeReference<T> typeReference, boolean encode) throws IOException
    {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null)
        {
            for (String key : params.keySet())
            {
                if (!encode)
                {
                    builder.add(key, params.get(key));
                }
                else
                {
                    builder.addEncoded(key, params.get(key));
                }
            }
        }
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url).post(formBody).build();
        Response response = execute(request);
        if (response.isSuccessful())
        {
            String responseUrl = response.body().string();
            return JSON.parseObject(responseUrl, typeReference);
        }
        else
        {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    public static String attachHttpGetParams(String url, Map<String, String> params)
    {
        if (params == null || params.keySet().size() == 0) { return url; }
        url = url + "?";
        for (String key : params.keySet())
        {
            url = url + key + "=" + params.get(key).toString() + "&";
        }
        url = url.substring(0, url.lastIndexOf('&'));
        return url;
    }
    
    public static String attachHttpGetParam(String url, String name, String value)
    {
        return url + "?" + name + "=" + value;
    }
}
