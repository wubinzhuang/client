package com.fxclient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.fxclient.app.page.LoginPage;
import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.fxbase.BasePage;
import com.fxclient.framework.util.AppSettings;

public class FXClientMain extends Application
{
    public static void main(String[] args)
    {
        PropertyConfigurator.configureAndWatch("classpath:log4j.properties");
        Logger logger = LoggerFactory.getLogger(FXClientMain.class);
        logger.info("客户端启动");
        // 初始化Spring
        args = new String[]{"classpath:spring-context.xml"};
        FxAppContext.getInstance().setApplicationContext(new FileSystemXmlApplicationContext(args));
        logger.info("Spring初始化完成");
        String language = AppSettings.getInstance().getProperty("app.i18n");
        logger.info("language:"+language);
        if ("en_US".equals(language))
        {
            FxAppContext.getInstance().setLanguage(getResourceBundle("en_US"));
        }
        else
        {
            FxAppContext.getInstance().setLanguage(getResourceBundle("zh_CN"));
        }
        logger.info("国际化文件加载完成");
        Application.launch(FXClientMain.class);
        logger.info("程序关闭");
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.getIcons().add(
                new Image(new File(FxAppContext.getInstance().getBaseDir() + File.separator + "image" + File.separator + "user.png").toURI().toURL().toExternalForm()));
        primaryStage.setTitle(FxAppContext.getInstance().getLanguage().getString("mainpage.SystemName"));
        primaryStage.setHeight(326);
        primaryStage.setWidth(700);
        BasePage bp = new LoginPage().loadPage();
        Scene s = new Scene(bp.getBox(), 700, 300);
        primaryStage.setScene(s);
        FxAppContext.getInstance().setCurrentStage(primaryStage);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private static ResourceBundle getResourceBundle(String string) {
        ResourceBundle rb;
        BufferedInputStream inputStream;
        String proFilePath = System.getProperty("user.dir")+ File.separator + "bin"+ File.separator + "fxclient" + File.separator + "i18n"+ File.separator + "fxclient_"
                + string + ".properties";
        try {
            inputStream = new BufferedInputStream(new FileInputStream(proFilePath));
            rb = new PropertyResourceBundle(inputStream);
            inputStream.close();
            return rb;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
