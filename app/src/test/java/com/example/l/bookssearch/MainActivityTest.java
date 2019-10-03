package com.example.l.bookssearch;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void analyzeHtml() {
        String url = "http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?search=java&xc=3&schoolId=1082&centerDomain=&searchtype=title";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.select("li");
            System.out.println(elements.size());
            for (Element element : elements){
                Elements selects = element.select("p");
                for (Element select : selects){
                    System.out.println("analyzeHtml: " + select.text());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}