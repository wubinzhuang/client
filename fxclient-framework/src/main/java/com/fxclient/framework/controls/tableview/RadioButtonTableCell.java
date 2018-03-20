package com.fxclient.framework.controls.tableview;

import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;

import com.fxclient.framework.vo.SelectedVoBase;

public class RadioButtonTableCell extends TableCell<SelectedVoBase, Boolean>
{
    private final RadioButton radioButton;
    
    public RadioButtonTableCell(ToggleGroup g)
    {
        radioButton = new RadioButton();
        radioButton.setToggleGroup(g);
    }
    
    public static Callback<TableColumn<SelectedVoBase, Boolean>, TableCell<SelectedVoBase, Boolean>> getCallBack(ToggleGroup g)
    {
        return new Callback<TableColumn<SelectedVoBase, Boolean>, TableCell<SelectedVoBase, Boolean>>()
        {
            @Override
            public TableCell<SelectedVoBase, Boolean> call(TableColumn<SelectedVoBase, Boolean> param)
            {
                return new RadioButtonTableCell(g);
            }
        };
    }
    
    @Override
    protected void updateItem(Boolean item, boolean empty)
    {
        super.updateItem(item, empty);
        if (empty)
        {
            setText(null);
            setGraphic(null);
            return;
        }
        setGraphic(this.radioButton);
        radioButton.setSelected(item);
        SelectedVoBase vo = (SelectedVoBase) getTableRow().getItem();
        if (vo != null)
        {
            vo.selectedProperty().unbind();
            vo.selectedProperty().bind(radioButton.selectedProperty());
        }
    };
}
