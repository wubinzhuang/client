package com.fxclient.app.model;

import java.io.Serializable;
import java.util.List;

public class MenuItemBo implements Serializable
{
    private static final long serialVersionUID  = 1L;
    
    public static final int   MENU_TYPE_DIR     = 0;
    
    public static final int   MENU_TYPE_FXPAGE  = 1;
    
    public static final int   MENU_TYPE_NETPAGE = 2;
    
    public MenuItemBo()
    {
    }
    
    public MenuItemBo(String menuId, String menuName, String menuCode, int level, int menuType, String bean, String url, List<MenuItemBo> childMenus, String icon)
    {
        super();
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuCode = menuCode;
        this.level = level;
        this.menuType = menuType;
        this.bean = bean;
        this.url = url;
        this.childMenus = childMenus;
        this.icon = icon;
    }
    
    private String           menuId;
    
    private String           menuName;
    
    private String           menuCode;
    
    private String           icon;
    
    private int              level;
    
    private int              menuType;
    
    private String           bean;
    
    private String           url;
    
    private List<MenuItemBo> childMenus;
    
    public String getMenuId()
    {
        return menuId;
    }
    
    public void setMenuId(String menuId)
    {
        this.menuId = menuId;
    }
    
    public String getMenuName()
    {
        return menuName;
    }
    
    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }
    
    public String getMenuCode()
    {
        return menuCode;
    }
    
    public void setMenuCode(String menuCode)
    {
        this.menuCode = menuCode;
    }
    
    public int getLevel()
    {
        return level;
    }
    
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    public int getMenuType()
    {
        return menuType;
    }
    
    public void setMenuType(int menuType)
    {
        this.menuType = menuType;
    }
    
    public String getBean()
    {
        return bean;
    }
    
    public void setBean(String bean)
    {
        this.bean = bean;
    }
    
    public String getIcon()
    {
        return icon;
    }
    
    public void setIcon(String icon)
    {
        this.icon = icon;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public List<MenuItemBo> getChildMenus()
    {
        return childMenus;
    }
    
    public void setChildMenus(List<MenuItemBo> childMenus)
    {
        this.childMenus = childMenus;
    }
    
    @Override
    public String toString()
    {
        return menuName;
    }
}
