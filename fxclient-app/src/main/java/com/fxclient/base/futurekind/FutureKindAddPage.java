package com.fxclient.base.futurekind;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fxclient.framework.appcontext.FxAppContext;
import com.fxclient.framework.controls.dialog.MessageBox;
import com.fxclient.framework.controls.dialog.PageDialog;
import com.fxclient.framework.controls.overlay.TaskRunner;
import com.fxclient.framework.controls.tableview.TablePager;
import com.fxclient.framework.fxbase.BasePage;
import com.fxclient.framework.util.HttpUtils;
import com.fxclient.sysmanager.util.Apis;
import com.uams.rpc.base.model.FutureKindModel;
import com.uams.rpc.base.model.MarketInfoModel;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FutureKindAddPage extends BasePage {
    @Override
    public BasePage loadPage() throws Exception {
        //return loadPage(File.separator + "static" + File.separator + "page" + File.separator + "base" + File.separator + "futureKind" + File.separator + "futureKindAddPage.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(FxAppContext.getInstance().getLanguage());
        loader.setLocation(new File(FxAppContext.getInstance().getBaseDir() + File.separator + "static" + File.separator + "page" + File.separator + "base"
                + File.separator + "futureKind" + File.separator + "futureKindAddPage.fxml").toURI().toURL());
        Parent p = loader.load();
        BasePage bp = loader.getController();
        bp.setRoot(p);
        return bp;
    }

   /* @FXML
    private TextField marketName;
*/
    private  FutureKindPage futureKindPage;

    @FXML
    private ChoiceBox marketNameList;

    @FXML
    private TextField kindCode;

    @FXML
    private TextField kindName;

    @FXML
    private TextField marketName;

    @FXML
    private TextField marketId;

    @FXML
    private TextField Id;

    private final Map<String, String> params = new HashMap<>();

    /*private TableView<FutureKindModel> tvwUserInfo;
    private TablePager<FutureKindModel> pageFutureKindInfo;*/
    @FXML
    void saveUserInfo(ActionEvent event) {
        FutureKindPage fkp=new FutureKindPage();
        Service<String> task = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        Map<String, String> params = new HashMap<>();
                        if(null!=Id.getText()&&!"".equals(Id.getText())){
                            params.put("Id", Id.getText());
                        }
                        params.put("marketId", marketId.getText());
                        params.put("kindCode", kindCode.getText());
                        params.put("kindName", kindName.getText());
                        String result = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.FutureKind_ADD_UPDATE, params, true);

                        return result;
                    }
                };
            }
        };
        task.setOnSucceeded((e) -> {
            if ("OK".equals(task.getValue())) {
                getOwner();
                ((PageDialog) lastPage).closeDialog();
                if(null==Id.getText()||"".equals(Id.getText())){
                    MessageBox.showInfo("保存成功！");
                    futureKindPage.pageFutureKindInfo.goPage();

                }else {
                    MessageBox.showInfo("修改成功！");
                    futureKindPage.pageFutureKindInfo.goPage();

                }

            } else {
                MessageBox.showWarning(task.getValue());
            }
        });
        TaskRunner.run(this.getLastPage(), task);
    }

    @Override
    public BasePage getOwner() {
        futureKindPage=(FutureKindPage)super.getOwner();
        return futureKindPage;
    }

    @FXML
    void initialize() {
        getOwner();
        try {
            List<MarketInfoModel> mimList = new ArrayList<MarketInfoModel>();
            String marketInfoModelList = HttpUtils.httpPostToString(Apis.getHttpHost() + Apis.FutureKind_all, params, true);
            mimList = JSON.parseObject(marketInfoModelList, new TypeReference<List<MarketInfoModel>>() {
            });

            String [] name= new String[mimList.toArray().length+1];
            name[0]="请选择交易市场";
            for (int i=0;i<mimList.toArray().length;i++){
                name[i+1]=mimList.subList(0,mimList.toArray().length).get(i).getMarketName();
            }

            Long [] marketIdHidden= new Long[mimList.toArray().length+1];
            marketIdHidden[0]=Long.valueOf(0);

            for (int i=0;i<mimList.toArray().length;i++){
                marketIdHidden[i+1]=mimList.subList(0,mimList.toArray().length).get(i).getId();
            }
            marketNameList.getItems().addAll(name);
            marketNameList.getSelectionModel().select(0);

            marketNameList.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) ->{
                marketId.setText(marketIdHidden[new_val.intValue()].toString());
                marketName.setText(name[new_val.intValue()].toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(FutureKindModel selectedItem) {
        marketNameList.setValue(selectedItem.getMarketName());
        kindCode.setText(selectedItem.getKindCode());
        kindName.setText(selectedItem.getKindName());
        Id.setText(selectedItem.getId().toString());
    }

}