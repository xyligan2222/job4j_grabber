package ru.job4j.grabber.quartz;

import java.sql.Timestamp;
import java.util.Objects;

public class Post {
    private String url;
    private String description;
    private Timestamp createDate;

    public Post() {
    }

    public Post(String url, String description, Timestamp createDate) {
        this.url = url;
        this.description = description;
        this.createDate = createDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(url, post.url)
                && Objects.equals(description, post.description)
                && Objects.equals(createDate, post.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, description, createDate);
    }
}
