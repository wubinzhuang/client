package com.fxclient.framework.appcontext;

import java.util.ResourceBundle;

import javafx.stage.Stage;

import org.springframework.context.ApplicationContext;

import com.fxclient.framework.util.AppSettings;

public class FxAppContext
{
    private static FxAppContext appContext;
    
    private ResourceBundle      language;
    
    private String              baseDir;
    
    private Stage               currentStage;
    
    private ApplicationContext  applicationContext;
    
    private FxAppContext()
    {
    }
    
    public static FxAppContext getInstance()
    {
        if (appContext == null)
        {
            appContext = new FxAppContext();
        }
        return appContext;
    }
    
    public String getBaseDir()
    {
        if (baseDir == null)
        {
            baseDir = AppSettings.getInstance().getProperty("config.path");
        }
        return baseDir;
    }
    
    public void setBaseDir(String baseDir)
    {
        this.baseDir = baseDir;
    }
    
    public ResourceBundle getLanguage()
    {
        return language;
    }
    
    public void setLanguage(ResourceBundle language)
    {
        this.language = language;
    }
    
    public Stage getCurrentStage()
    {
        return currentStage;
    }
    
    public void setCurrentStage(Stage currentStage)
    {
        this.currentStage = currentStage;
    }
    
    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
    
    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }
}
