package com.example.l.bookssearch.utils;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.l.bookssearch.data.Book;
import com.example.l.bookssearch.viewmodel.BookViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsoupUtil {

    // example
    // http://agentdockingopac.featurelib.libsou.com/showhome/search/showSearch?schoolId=1082
    // http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?search=java&xc=3&schoolId=1082&centerDomain=&searchtype=title
    // http://agentdockingopac.featurelib.libsou.com/showhome/searchlist/opacSearchList?search=java&xc=3&schoolId=1082&centerDomain=&searchtype=title&page=1

    private final String bathUrl = "http://agentdockingopac.featurelib.libsou.com/";

    private static JsoupUtil jsoupUtil = null;
    private List<String> bookUrlList = null;
    private String previousUrl = null;
    private String nextUrl = null;
    private String TAG = "JsoupUtil";

    private JsoupUtil() {
    }

    public static synchronized JsoupUtil getInstance() {
        if (jsoupUtil == null) {
            jsoupUtil = new JsoupUtil();
        }
        return jsoupUtil;
    }

    //获取搜索结果的数目
    public int getTotalCount(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            String info = doc.getElementsByClass("num").text();
            return Integer.parseInt(info.substring(info.indexOf("共") + 1, info.indexOf("条")));
        }
        return 0;
    }

    //显示所有书的信息
    public List<Book> getTotalBooks(String url) {
        bookUrlList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            return getTotalBooks(doc);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Book> getTotalBooks(Document doc) {
        bookUrlList = new ArrayList<>();
        List<Book> list = new ArrayList<>();
        Elements elements = doc.select("li");
        for (Element element : elements) {
            Book book = new Book();
            String title = element.select("div.title").text();
            book.setTitle(title);
            Elements selects = element.select("p");
            for (int i = 0; i < selects.size(); i++) {
                String content = selects.get(i).text();
                if (content.contains("书目信息")) {
                    book.setPublish(content);
                }
                if (content.contains("馆藏信息")) {
                    book.setCount(content);
                }
                if (content.contains("索书号")) {
                    if (content.length() <= 4) {
                        book.setNum(content + "暂无");
                    } else {
                        book.setNum(content);
                    }
                }
            }
            list.add(book);

            // 保存详情链接
            String detailUrl = element.select("a").attr("href");
            bookUrlList.add(bathUrl + detailUrl);
        }

        // 保存上下页url
        Element previousElement = doc.select("a.last").first();
        if (previousElement != null) {
            previousUrl = previousElement.attr("abs:href");
        } else {
            previousUrl = null;
        }
        Element nextElement = doc.select("a.next").first();
        if (nextElement != null) {
            nextUrl = nextElement.attr("abs:href");
        } else {
            nextUrl = null;
        }
        return list;
    }

    // 获取详情信息的url
    public String getBookDetailUrl(int position) {
        if (bookUrlList != null) {
            return bookUrlList.get(position);
        } else {
            return null;
        }
    }

    //获取查询链接
    public String getSearchUrl(String key, int typeId) {
        String type = "";
        switch (typeId) {
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
        return bathUrl + "showhome/searchlist/opacSearchList?" + "search=" + key + "&xc=3&schoolId=1082&centerDomain=&searchtype=" + type + "&page=1";
    }

    //获取上一页的url
    public String getPreviousUrl() {
        return previousUrl;
    }

    //获取下一页的url
    public String getNextUrl() {
        return nextUrl;
    }

    public Book getDetailBook(String url) {

        Book book = new Book();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            String title = null;
            String author = null;
            if (doc.select("div.tit").hasClass("tit")) {
                title = doc.select("div.tit").select("h1").text();
                author = doc.select("div.tit").select("p").text().trim();
                author = author.substring(author.indexOf(":") + 1);
            }
            String num = null;
            String location = null;
            // 馆藏地
            if (doc.select("div.tableCon").hasClass("tableCon")) {
                num = doc.select("div.tableCon").select("td").get(0).text();
                location = doc.select("div.tableCon").select("td").get(3).text();
            }
            String publish = null;
            String isbn = null;
            String description = null;
            if (doc.select("div.catalog").hasClass("catalog")) {
                Elements pElements = doc.select("div.catalog").select("p");
                publish = pElements.get(0).text().trim();
                if (publish.contains("出版发行项")) {
                    publish = publish.substring(publish.indexOf(":") + 1);
                } else {
                    return null;
                }
                isbn = pElements.get(1).text().trim();
                if (isbn.contains("ISBN及定价")) {
                    // 部份书没有isbn
                    if (isbn.substring(isbn.indexOf(":")).length() > 13) {
                        isbn = isbn.replace("-", "");
                        isbn = "ISBN：" + isbn.substring(isbn.indexOf(":") + 1, isbn.indexOf(":") + 13);
                    }
                } else {
                    return null;
                }
                boolean hasDescription = false;
                for (int i = 0; i < pElements.size(); i ++) {
                    String content = pElements.get(i).text().trim();
                    if (content.contains("提要文摘附注")) {
                        description = content.substring(content.indexOf(":") + 1);
                        hasDescription = true;
                    }
                }
                if (!hasDescription) {
                    return null;
                }
            } else {
                return null;
            }
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublish(publish);
            book.setNum(num);
            book.setLocation(location);
            book.setIsbn(isbn);
            book.setDescription(description);
            return book;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // (无法获取)
    @Deprecated
    public static String getImageFromDouBan(String isbn) throws IOException {
        String url = getBookDetailFromDouBan(isbn);
        if (url != null) {
            Document doc = Jsoup.connect(url).get();
            return doc.select("a.bng").attr("src");
        } else {
            return null;
        }
    }

    // (无法获取)
    @Deprecated
    private static String getBookDetailFromDouBan(String isbn) throws IOException {
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

    // 使用百度百科方式
    public String getBookImageFromBaiduBike(String title) {
        String url = "https://baike.baidu.com/item/" + title;
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            return doc.select("div.summary-pic").select("img").attr("src");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 检查图书格式的合法性
    public boolean checkBookFormat(String detailUrl, FragmentActivity activity) {
        Book book = getDetailBook(detailUrl);
        if (book != null) {
            BookViewModel viewModel = ViewModelProviders.of(activity).get(BookViewModel.class);
            viewModel.getDetailBook().postValue(book);
            return true;
        } else {
            return false;
        }
    }
}
