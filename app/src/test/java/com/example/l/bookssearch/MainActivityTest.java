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

import static org.junit.Assert.*;

public class MainActivityTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void analyzeHtml() {
        try {
            String imageUrl = JsoupUtils.getBookDetailFromDouBan("9787121269394");
            System.out.println(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}