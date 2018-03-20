package com.fxclient.framework.fxbase;

import javafx.stage.Stage;

public abstract class BaseStage extends Stage
{
    public Stage buildStage()
    {
        initStage();
        return this;
    }
    
    protected abstract void initStage();
}
