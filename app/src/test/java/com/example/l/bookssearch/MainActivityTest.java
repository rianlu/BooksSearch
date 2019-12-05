package com.example.l.bookssearch;

import android.util.Log;

import com.example.l.bookssearch.model.Book;
import com.example.l.bookssearch.utils.JsoupUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class MainActivityTest {

    JsoupUtils jsoupUtils;
    @Before
    public void setUp() throws Exception {
        jsoupUtils = JsoupUtils.getInstance();
    }

    @Test
    public void analyzeHtml() {
        List<Book> list = jsoupUtils.getTotalBooks("http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?search=android&xc=3&schoolId=1082&centerDomain=&searchtype=title&page=1");
        System.out.println(list.toString());
        String url = jsoupUtils.getBookDetailUrl(1);
        System.out.println(url);
        Book book = jsoupUtils.getDetailBook(url);
        System.out.println(book);
    }
}