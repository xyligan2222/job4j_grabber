package ru.job4j.grabber.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable  {

    private Connection cnn;

    public static void main(String[] args) throws IOException {

        //SqlRuParseDescription.parsePage("https://www.sql.ru/forum/job-offers/", 1);
        SqlRuParse sqlRuParse = new SqlRuParse();
        //sqlRuParse.list("https://www.sql.ru/forum/job-offers/");
       // ConfigDB configDB = new ConfigDB("grab.properties");
        PsqlStore psqlStore = new PsqlStore();
        psqlStore.save(sqlRuParse.detail("https://www.sql.ru/forum/job-offers/"));

    }

   public PsqlStore() {
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("grab.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public void save(List<Post> post) {
        for (Post list : post) {
            try (PreparedStatement st = cnn.prepareStatement("insert into post (name, text, link, created) values (?,?,?,?);", Statement.RETURN_GENERATED_KEYS)) {
                st.setString(1, list.getName());
                st.setString(2, list.getUrl());
                st.setString(3, list.getDescription());
                st.setTimestamp(4, list.getCreateDate());

                st.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (Statement st = cnn.createStatement()) {
            ResultSet resultSet = st.executeQuery("SELECT * FROM post");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String text = resultSet.getString("text");
                String link = resultSet.getString("link");
                Timestamp created = resultSet.getTimestamp("created");
                Post post = new Post(name, text, link, created);
                list.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(list);
        return list;
    }

    @Override
    public Post findById(String id) {
        Post rsl = null;
        try (PreparedStatement st = cnn.prepareStatement("SELECT * FROM post WHERE id = ?;", Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, Integer.parseInt(id));
            ResultSet rslSet = st.executeQuery();
            if (rslSet.next()) {
                String name = rslSet.getString("name");
                String text = rslSet.getString("text");
                String link = rslSet.getString("link");
                Timestamp created = rslSet.getTimestamp("created");
                rsl = (new Post(name, text, link, created));
                return rsl;
            }
            if (rsl == null) {
                throw new NoSuchElementException("there are no vacancies with this id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}