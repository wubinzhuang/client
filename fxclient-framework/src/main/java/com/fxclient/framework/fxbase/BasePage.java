package com.fxclient.framework.fxbase;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import com.fxclient.framework.appcontext.FxAppContext;

public abstract class BasePage
{
    public abstract BasePage loadPage() throws Exception;
    
    public BasePage loadPage(String fxmlPath) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(FxAppContext.getInstance().getLanguage());
        loader.setLocation(new URL("file:" + FxAppContext.getInstance().getBaseDir() + fxmlPath));
        Parent p = loader.load();
        BasePage bp = loader.getController();
        StackPane sp = new StackPane(p);
        sp.setAlignment(Pos.TOP_LEFT);
        bp.setBox(sp);
        bp.setRoot(p);
        return bp;
    }

    protected Parent    root;
    
    protected StackPane box;
    
    public StackPane getBox()
    {
        return box;
    }
    
    public void setBox(StackPane box)
    {
        this.box = box;
    }
    
    protected BasePage lastPage;
    
    public BasePage getLastPage()
    {
        return lastPage;
    }
    
    public void setLastPage(BasePage lastPage)
    {
        this.lastPage = lastPage;
    }
    
    public void setRoot(Parent root)
    {
        this.root = root;
    }
    
    public Parent getRoot()
    {
        return root;
    }

    private BasePage owner;

    public BasePage getOwner() {
        return owner;
    }

    public void setOwner(BasePage owner) {
        this.owner = owner;
    }
}
