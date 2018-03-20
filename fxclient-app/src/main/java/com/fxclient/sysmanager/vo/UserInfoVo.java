package com.fxclient.sysmanager.vo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserInfoVo
{
    private StringProperty id = new SimpleStringProperty();
    
    public StringProperty idProperty()
    {
        return this.id;
    }
    
    public String getId()
    {
        return this.id.get();
    }
    
    public void setId(String id)
    {
        this.id.set(id);
    }
    
    private StringProperty userName = new SimpleStringProperty();
    
    public StringProperty userNameProperty()
    {
        return this.userName;
    }
    
    public String getUserName()
    {
        return this.userName.get();
    }
    
    public void setUserName(String userName)
    {
        this.userName.set(userName);
    }
    
    private StringProperty empCode = new SimpleStringProperty();
    
    public StringProperty empCodeProperty()
    {
        return this.empCode;
    }
    
    public String getEmpCode()
    {
        return this.empCode.get();
    }
    
    public void setEmpCode(String empCode)
    {
        this.empCode.set(empCode);
    }
    
    private StringProperty empName = new SimpleStringProperty();
    
    public StringProperty empNameProperty()
    {
        return this.empName;
    }
    
    public String getEmpName()
    {
        return this.empName.get();
    }
    
    public void setEmpName(String empName)
    {
        this.empName.set(empName);
    }
    
    private StringProperty sex = new SimpleStringProperty();
    
    public StringProperty sexProperty()
    {
        return this.sex;
    }
    
    public String getSex()
    {
        return this.sex.get();
    }
    
    public void setSex(String sex)
    {
        this.sex.set(sex);
    }
    
    private StringProperty tel = new SimpleStringProperty();
    
    public StringProperty telProperty()
    {
        return this.tel;
    }
    
    public String getTel()
    {
        return this.tel.get();
    }
    
    public void setTel(String tel)
    {
        this.tel.set(tel);
    }
}
