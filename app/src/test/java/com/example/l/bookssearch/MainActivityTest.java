package com.example.l.bookssearch;

import com.example.l.bookssearch.data.Book;
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
        String url = "http://agentdockingopac.featurelib.libsou.com/showhome/searchdetail/opacSearchDetail;jsessionid=7D25249406985557C80656308B405E28?opacUrl=http%3a%2f%2fopac.zjweu.edu.cn%3a8000%2fopac%2fitem.php%3fmarc_no%3d625231525a4d4941584c6154373267746a2b414a48513d3d%26list%3d1&search=java&centerDomain=&schoolId=1082&searchtype=title&page=1&xc=3";
        Book book = jsoupUtil.getDetailBook(url);
        System.out.println(book);
    }


}