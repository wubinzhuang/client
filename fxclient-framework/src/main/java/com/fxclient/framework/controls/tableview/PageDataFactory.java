package com.fxclient.framework.controls.tableview;

public interface PageDataFactory<T>
{
    public PageData<T> queryPageData(int page, int pageSize);
}
