package com.example.l.bookssearch;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.l.bookssearch.model.Book;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {

    private String url;
    private MutableLiveData<String> pageInfo;
    private MutableLiveData<List<Book>> bookList;
    private MutableLiveData<Book> detailBook;

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public MutableLiveData<String> getPageInfo() {
        if (pageInfo == null) {
            pageInfo = new MutableLiveData<>();
            pageInfo.setValue("暂无数据");
        }
        return pageInfo;
    }

    MutableLiveData<List<Book>> getBookList() {
        if (bookList == null) {
            bookList = new MutableLiveData<>();
            bookList.setValue(new ArrayList<Book>());
        }
        return bookList;
    }

    public MutableLiveData<Book> getDetailBook() {
        if (detailBook == null) {
            detailBook = new MutableLiveData<>();
            Book book = new Book();
            book.setTitle("暂无数据");
            detailBook.setValue(book);
        }
        return detailBook;
    }
}
