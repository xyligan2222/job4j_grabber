package ru.job4j.grabber.quartz;

import java.util.List;

public interface Parse {
    /*
    this method loads the list of posts
    @param URL from site
     */
    List<Post> list(String link);
    /*
    this method loads the description one posts
    @param URL from post
     */
    Post detail(String link);
}