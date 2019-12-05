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

}