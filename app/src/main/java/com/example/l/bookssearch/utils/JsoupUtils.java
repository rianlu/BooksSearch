package com.example.l.bookssearch.utils;

import com.example.l.bookssearch.model.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
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

    public static Book getDetailBook(Document doc) {
        Book book = new Book();
        String title = doc.select("div.tit").select("h1").text();
        String author = doc.select("div.tit").select("p").text();
        String publish = doc.select("div.catalog").select("p").get(0).text();
        String num = doc.select("div.tableCon").select("td").get(0).text();
        String location = doc.select("div.tableCon").select("td").get(3).text();
        String isbn_price = doc.select("div.catalog").select("p").get(1).text();
        String isbn_title = isbn_price.substring(0, isbn_price.indexOf("及"));
        String isbn_id = isbn_price.substring(isbn_price.indexOf(":") + 1, isbn_price.lastIndexOf("/")).replace("-", "").trim();
        String isbn = isbn_title + ": " + isbn_id;
        String description = doc.select("div.catalog").select("p").size() > 9
                ? doc.select("div.catalog").select("p").get(9).text()
                : "暂无描述信息";
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublish(publish);
        book.setNum(num);
        book.setLocation(location);
        book.setIsbn(isbn);
        book.setDescription(description);
        return book;
    }

    public static String getImageFromDouBan(String isbn) throws IOException {
        String url = getBookDetailFromDouBan(isbn);
        if (url != null) {
            Document doc = Jsoup.connect(url).get();
            return doc.select("a.bng").attr("src");
        } else {
            return null;
        }
    }

    public static String getBookDetailFromDouBan(String isbn) throws IOException {
        //https://search.douban.com/book/subject_search?search_text=9787121342875&cat=1001
        String url = "https://search.douban.com/book/subject_search?search_text=" + isbn + "&cat=1001";
        System.out.println(url);
        Document doc = Jsoup.connect(url).get();
        if (doc != null) {
            return doc.html();
        } else {
            return null;
        }
    }
}
