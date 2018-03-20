package com.fxclient.app.job;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import com.fxclient.framework.util.AppSettings;

public class MessageJob extends ScheduledService<String>
{
    private String messageStr;
    
    public String getMessageStr()
    {
        return messageStr;
    }
    
    public void setMessageStr(String messageStr)
    {
        this.messageStr = messageStr;
        index = 0;
    }
    
    private int index = 0;
    
    public MessageJob()
    {
        setPeriod(Duration.seconds(Integer.parseInt(AppSettings.getInstance().getProperty("MessagePeriod"))));
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
                String curmsg = "";
                if (messageStr.length() > index + 20)
                {
                    curmsg = messageStr.substring(index, index + 20);
                    index = index + 20;
                }
                else
                {
                    curmsg = messageStr.substring(index);
                    index = 0;
                }
                return curmsg;
            }
        };
    }
}
