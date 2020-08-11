package ru.job4j.grabber.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        List<String> listURL = getUrl(doc);
        List<String> listDate = getDate(doc);
        for (int i = 0; i < listDate.size(); i++) {
            System.out.println(listURL.get(i) + System.lineSeparator() + listDate.get(i));
        }
    }

    public static List<String> getUrl(Document document) {
        List<String> list = new ArrayList<>();
        Elements row = document.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);

            list.add(href.attr("href") + System.lineSeparator() + href.text());
        }
        return list;
    }

    public static List<String> getDate(Document document) {
        List<String> list = new ArrayList<>();
        Elements row = document.select(".altCol");
        for (Element td : row) {
            if (td.childNodeSize() == 1) {
                list.add(td.text());
            }
        }
        return list;
    }
}
