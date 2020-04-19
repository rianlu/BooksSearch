package com.example.l.bookssearch;

import com.example.l.bookssearch.model.Book;
import com.example.l.bookssearch.utils.JsoupUtil;

import org.junit.Before;
import org.junit.Test;

public class MainActivityTest {

    private JsoupUtil jsoupUtil;
    @Before
    public void setUp() throws Exception {
        jsoupUtil = JsoupUtil.getInstance();
    }

    @Test
    public void analyzeHtml() {
        Book book = jsoupUtil.getDetailBook("http://agentdockingopac.featurelib.libsou.com/showhome/searchdetail/opacSearchDetail?opacUrl=http%3a%2f%2fopac.zjweu.edu.cn%3a8000%2fopac%2fitem.php%3fmarc_no%3d6b434672354b3636434b2f6b575233335a5276684a513d3d%26list%3d1&search=%e8%a5%bf%e6%b8%b8%e8%ae%b0&centerDomain=&schoolId=1082&searchtype=title&page=1&xc=3");
        System.out.println(book);
    }


}