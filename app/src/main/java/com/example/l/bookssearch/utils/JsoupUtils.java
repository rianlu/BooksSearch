package com.example.l.bookssearch.utils;

import android.util.Log;

import com.example.l.bookssearch.Book;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JsoupUtils {

    // http://agentdockingopac.featurelib.libsou.com/showhome/search/showSearch?schoolId=1082
    // http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?search=java&xc=3&schoolId=1082&centerDomain=&searchtype=title
    // http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?search=java&xc=3&schoolId=1082&centerDomain=&searchtype=title&page=1

    private static String url = "http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?";

    //显示搜索结果的数目
    public static String getTotalCount(Document doc){
        return doc.getElementsByClass("num").text();
    }

    //显示所有书的链接
    public static List<String> getTotalBooksLinks(Document doc){
        List<String> list = new ArrayList<>();
        Elements elements = doc.select("a[href]");
        for (Element element : elements){
            String href = element.attr("abs:href");
            list.add(href);
        }
        return list;
    }

    //显示所有书的信息
    public static List<Book> getTotalBooks(Document doc) {
        List<Book> list = new ArrayList<>();
        Elements elements = doc.select("li");
        System.out.println(elements.size());
        for (Element element : elements){
            Book book = new Book();
            String title = element.select("div.title").text();
            book.setTitle(title);
            Elements selects = element.select("p");
            book.setPublish(selects.get(0).text());
            book.setCount(selects.get(1).text());
            book.setNum(selects.get(2).text());
            list.add(book);
        }
        return list;
    }

    //获取查询链接
    public static String getSearchUrl(String key, int typeId){
        String type = "";
        switch (typeId){
            case 0:
                type = "title";
                break;
            case 1:
                type = "author";
                break;
            case 2:
                type = "subject";
                break;
            case 3:
                type = "isbn";
                break;
        }
        return url + "search=" + key + "&xc=3&schoolId=1082&centerDomain=&searchtype=" + type + "&page=1";
    }

    //获取上一页的url
    public static String getPreviousUrl(Document doc){
        Element element = doc.select("a.last").first();
        if (element != null){
            return element.attr("abs:href");
        }
        return null;
    }

    //获取下一页的url
    public static String getNextUrl(Document doc){
        Element element = doc.select("a.next").first();
        if (element != null){
            return element.attr("abs:href");
        }
        return null;
    }
}
