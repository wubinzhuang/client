package com.fxclient.autoupdate.page;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;

import okhttp3.*;

import com.fxclient.autoupdate.http.ProgressResponseBody;
import com.fxclient.autoupdate.http.ProgressResponseBody.ProgressListener;
import com.fxclient.autoupdate.util.AppSettings;
import com.fxclient.autoupdate.util.RunCmd;
import com.fxclient.autoupdate.util.UpdateSettings;
import com.fxclient.autoupdate.util.ZipUtil;

public class AutoupdatePage implements ProgressResponseBody.ProgressListener
{
    @FXML
    Label                    lblUpdmsg;
    
    @FXML
    TextArea                 txtUpdVersionInfo;
    
    @FXML
    ProgressBar              pbUpd;
    
    @FXML
    Button                   btnGoOn;
    
    private OkHttpClient     client;
    
    private Call             call;
    
    private ProgressListener progressListener;
    
    private long             contentLength;
    
    private long             breakPoints;
    
    private long             totalBytes;
    
    @FXML
    void initialize()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        File file = new File(System.getProperty("user.dir") + "/download/autoupdate.zip");
        if (file.exists())
        {
            file.delete();
        }
        File dir = new File(System.getProperty("user.dir") + "/download");
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        txtUpdVersionInfo.setText(UpdateSettings.getInstance().getProperty("autoupdate.versioninfo"));
        progressListener = this;
        client = getProgressClient();
        download(0L);
    }
    
    @FXML
    void goOn()
    {
        btnGoOn.setDisable(true);
        btnGoOn.setText("正在下载");
        download(breakPoints);
    }
    
    private Call newCall(long startPoints)
    {
        Request request = new Request.Builder().url(UpdateSettings.getInstance().getProperty("autoupdate.url")).header("RANGE", "bytes=" + startPoints + "-").build();
        // Request request = new Request.Builder().url("http://localhost:8080/fxclient/version/download.do").header("RANGE", "bytes=" + startPoints + "-").build();
        return client.newCall(request);
    }
    
    public OkHttpClient getProgressClient()
    {
        Interceptor interceptor = new Interceptor()
        {
            @Override
            public Response intercept(Chain chain) throws IOException
            {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
            }
        };
        return new OkHttpClient.Builder().addNetworkInterceptor(interceptor).build();
    }
    
    public void download(final long startsPoint)
    {
        call = newCall(startsPoint);
        call.enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                final String msg = e.getMessage();
                Platform.runLater(() -> {
                    lblUpdmsg.setText("下载出错:" + msg);
                    btnGoOn.setDisable(false);
                    btnGoOn.setText("继续下载");
                });
            }
            
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                save(response, startsPoint);
            }
        });
    }
    
    private void save(Response response, long startsPoint)
    {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        FileChannel channelOut = null;
        RandomAccessFile randomAccessFile = null;
        try
        {
            randomAccessFile = new RandomAccessFile(System.getProperty("user.dir") + "/download/autoupdate.zip", "rwd");
            channelOut = randomAccessFile.getChannel();
            MappedByteBuffer mappedBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE, startsPoint, body.contentLength());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1)
            {
                mappedBuffer.put(buffer, 0, len);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            breakPoints = totalBytes;
            Platform.runLater(() -> {
                lblUpdmsg.setText("下载出错");
                btnGoOn.setDisable(false);
                btnGoOn.setText("继续下载");
            });
        }
        finally
        {
            try
            {
                in.close();
                if (channelOut != null)
                {
                    channelOut.close();
                }
                if (randomAccessFile != null)
                {
                    randomAccessFile.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void onPreExecute(long contentLength)
    {
        Platform.runLater(() -> {
            if (this.contentLength == 0L)
            {
                this.contentLength = contentLength;
                pbUpd.setProgress(0);
                lblUpdmsg.setText("开始下载");
            }
        });
    }
    
    @Override
    public void update(long totalBytes, boolean done)
    {
        this.totalBytes = totalBytes + breakPoints;
        Platform.runLater(() -> {
            double p = (totalBytes + breakPoints) * 1.0 / contentLength;
            pbUpd.setProgress(p);
            lblUpdmsg.setText("正在下载" + (totalBytes + breakPoints) / 1024 + "kb/" + contentLength / 1024 + "kb");
            if (done)
            {
                lblUpdmsg.setText("下载完成");
                String path = System.getProperty("user.dir");
                try
                {
                    ZipUtil.unzip(path + File.separator + "download" + File.separator + "autoupdate.zip", path, false);
                    lblUpdmsg.setText("解压完成");
                    AppSettings.getInstance().setProperty("curVersion", UpdateSettings.getInstance().getProperty("autoupdate.version"));
                    AppSettings.getInstance().save();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        Platform.runLater(() -> {
            if (done)
            {
                final String cmd = UpdateSettings.getInstance().getProperty("autoupdate.runapp");
                RunCmd.runCmd(cmd);
                try
                {
                    Thread.sleep(1000);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
