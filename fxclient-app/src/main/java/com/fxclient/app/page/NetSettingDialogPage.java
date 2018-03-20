package com.fxclient.app.page;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import com.fxclient.app.viewmodel.NetSettingVo;
import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.controls.dialog.PageDialog;
import com.fxclient.framework.fxbase.BasePage;

public class NetSettingDialogPage extends BasePage
{
    @FXML
    private TextField    txtPort;
    
    @FXML
    private Button       btnconfirm;
    
    @FXML
    private TextField    txtIp;
    
    private NetSettingVo vo;
    
    @FXML
    void saveNetSetting(ActionEvent event)
    {
        if (vo == null)
        {
            vo = new NetSettingVo();
            vo.setSelected(false);
        }
        vo.setIp(txtIp.getText());
        vo.setPort(txtPort.getText());
        ((LoginPage) lastPage.getLastPage()).saveNetSetting(vo);
        ((PageDialog) lastPage).closeDialog();
    }
    
    @Override
    public BasePage loadPage() throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(FxAppContext.getInstance().getLanguage());
        loader.setLocation(new File(FxAppContext.getInstance().getBaseDir() + File.separator + "static" + File.separator + "page" + File.separator + "login"
                + File.separator + "netsettingdialog" + File.separator + "netsettingdialog.fxml").toURI().toURL());
        Parent p = loader.load();
        BasePage bp = loader.getController();
        bp.setRoot(p);
        return bp;
    }
    
    public void modify(NetSettingVo vo)
    {
        this.txtIp.setText(vo.getIp());
        this.txtPort.setText(vo.getPort());
        this.vo = vo;
    }
}
