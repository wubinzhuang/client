package com.fxclient.app.job;

import java.util.Random;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import com.fxclient.framework.util.AppSettings;

public class NetTestJob extends ScheduledService<String>
{
    public StringProperty alertNumPorperty  = new SimpleStringProperty("0");
    
    public StringProperty netStatusPorperty = new SimpleStringProperty("0");
    
    public NetTestJob()
    {
        setPeriod(Duration.seconds(Integer.parseInt(AppSettings.getInstance().getProperty("NetTestPeriod"))));
        setRestartOnFailure(true);
    }
    
    @Override
    protected Task<String> createTask()
    {
        return new Task<String>()
        {
            @Override
            protected String call() throws Exception
            {
                setAlertNum(new Random().nextInt(99) + "");
                setNetStatus(new Random().nextBoolean());
                return null;
            }
        };
    }
    
    private void setAlertNum(String num)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                alertNumPorperty.set(num);
            }
        });
    }
    
    private void setNetStatus(boolean status)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                netStatusPorperty.set(status ? "连接成功" : "连接失败");
            }
        });
    }
}
