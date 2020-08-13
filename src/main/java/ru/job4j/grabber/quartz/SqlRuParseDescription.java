package ru.job4j.grabber.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParseDescription {

    public static void main(String[] args) throws Exception {


        for (int i = 1; i <= 5; i++) {
            List<String> url = listUrl("https://www.sql.ru/forum/job-offers/" + i);
            List<Timestamp> page = getDate("https://www.sql.ru/forum/job-offers/" + i);
            List<String> description = descriptionVacancy("https://www.sql.ru/forum/job-offers/" + i);
        }
    }

    public static List<String> listUrl(String url) throws IOException {
        List<String> list = new ArrayList<>();
        Document document = getUrlPage(url);
        Elements row = document.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            list.add(href.attr("href"));
        }
        return list;
    }

    public static List<Timestamp> getDate(String url) throws IOException {
        DateParse dateParse = new DateParse();
        List<Timestamp> list = new ArrayList<>();
        Document document = getUrlPage(url);
        Elements row = document.select(".altCol");
        for (Element td : row) {
            if (td.childNodeSize() == 1) {
                Timestamp date = dateParse.parseString(String.valueOf(td.text()));
                list.add(date);
            }
        }
        return list;
    }

    /*public static List<String> parsePage(String url, int page) throws IOException {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= page; i++) {
            List<String> urlList = listUrl(url + i);
            List<Timestamp> pageList = getDate(url + i);
            List<String> descriptionList = descriptionVacancy(url + i);
           posts.add(new )
        }
    } */

    public static List<String> descriptionVacancy(String string) throws IOException {
            List<String> list = listUrl(string);
            List<String> description = listUrl(string);
        for (String lists : list) {
            Document doc = Jsoup.connect(lists).get();
            Element row = doc.getElementsByClass("msgBody").get(1);
            Element row2 = doc.select(".msgTable").get(0).getElementsByTag("td").get(0);
            description.add(row2.text() + System.lineSeparator() + row.text());
            System.out.println(row2.text() + System.lineSeparator() + row.text());
        }
        return description;
    }

    public static Document getUrlPage(String url) throws IOException {
        return Jsoup.connect(url).get();
    }
}
