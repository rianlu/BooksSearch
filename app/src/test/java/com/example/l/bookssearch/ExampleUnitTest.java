package com.example.l.bookssearch;

import com.example.l.bookssearch.model.Book;
import com.example.l.bookssearch.utils.JsoupUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Document doc = null;
    private String url = "http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?search=java&xc=3&schoolId=1082&centerDomain=&searchtype=author";

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getList() {

        try {
            doc = Jsoup.connect(url).get();
            List<Book> bookList = JsoupUtils.getTotalBooks(doc);
            System.out.println(bookList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            // 获得图书的基本信息
            List<Book> bookList = JsoupUtils.getTotalBooks(doc);
            bookList.forEach(System.out::println);
        }
    }
}