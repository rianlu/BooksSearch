package com.example.l.bookssearch;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {

    private List<String> types;
    private String url;
    private MutableLiveData<String> pageInfo;
    private MutableLiveData<List<Book>> bookList;

    public List<String> getTypes() {
        if (types == null) {
            types = new ArrayList<>();
            types.add("题名");
            types.add("作者");
            types.add("主题词");
            types.add("ISBN");
        }
        return types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MutableLiveData<String> getPageInfo() {
        if (pageInfo == null) {
            pageInfo = new MutableLiveData<>();
            pageInfo.setValue("暂无数据");
        }
        return pageInfo;
    }

    public MutableLiveData<List<Book>> getBookList() {
        if (bookList == null) {
            bookList = new MutableLiveData<>();
            bookList.setValue(new ArrayList<Book>());
        }
        return bookList;
    }
}
