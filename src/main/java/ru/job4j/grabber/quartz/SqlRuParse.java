package ru.job4j.grabber.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import java.util.List;

public class SqlRuParse implements Parse {
    private static final int POST_LIST_TOPIC = 1;

    private static final int DATE_TIME = 5;

    private static final int LINK = 0;

    private static final int MSG_TEXT = 1;


    @Override
    public List<Post> list(String link) {
        DateParse dateParse = new DateParse();
        List<Post> post = new ArrayList<>();
        try {
            for (int i = 1; i <= 5; i++) {
                Document doc = Jsoup.connect(String.format("%s%d", link, i)).get();
                Elements tableRows = doc.select(".forumTable").first().getElementsByTag("tr");
                for (Element row : tableRows) {
                    Element postListTopic = row.child(POST_LIST_TOPIC);
                    if (postListTopic.hasClass("postslisttopic")) {
                        Node date = row.child(DATE_TIME).childNode(0);
                        Timestamp createDate = dateParse.parseString(String.valueOf(date));
                        Element linkElement = postListTopic.child(LINK);
                        String links = linkElement.attr("href");
                        String name = linkElement.text();
                        post.add(new Post(name, links, " ", createDate));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return post;
    }

    @Override
    public List<Post> detail(String link) {
        DateParse dateParse = new DateParse();
        List<Post> post = new ArrayList<>();
        try {
            for (Post posts : list(link)) {
                Document doc = Jsoup.connect(posts.getUrl()).get();
                Elements comments = doc.select(".msgTable");
                String description = String.valueOf(comments.first().select(".msgBody").get(MSG_TEXT));
                String name = comments.first().select(".messageHeader").text();
                String date = comments.last().select(".msgFooter").text();
                date = date.substring(0, date.indexOf('[') - 1);
                post.add(new Post(
                        name, posts.getUrl(), description, dateParse.parseString(date))
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}
