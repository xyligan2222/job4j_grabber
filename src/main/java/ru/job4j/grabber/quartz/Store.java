package ru.job4j.grabber.quartz;

import java.util.List;
import java.util.function.Predicate;

public interface Store {
   /*
    this method saves the post in the database
    */
    void save(Post post);
    /*
    this method get the post in the database
    */
    List<Post> getAll();
}