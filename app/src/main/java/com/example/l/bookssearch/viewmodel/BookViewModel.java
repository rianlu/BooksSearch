package com.example.l.bookssearch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.l.bookssearch.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookViewModel extends ViewModel {

    private String url;
    private MutableLiveData<String> pageInfo;
    private MutableLiveData<List<Book>> bookList;
    private MutableLiveData<Book> detailBook;
    private boolean isDestroyed = false;

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
            bookList.setValue(new ArrayList<>());
        }
        return bookList;
    }

    public MutableLiveData<Book> getDetailBook() {
        if (detailBook == null) {
            detailBook = new MutableLiveData<>();
            detailBook.postValue(new Book());
        }
        return detailBook;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
