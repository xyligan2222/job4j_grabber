package ru.job4j.grabber.quartz;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConfigDB implements AutoCloseable {
    private String name;
    private Properties config = new Properties();
    private Connection cn;

    public ConfigDB(String name) {
        this.name = name;
    }

    public Connection init() throws ClassNotFoundException, SQLException {
            properties(this.name);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );

    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    public  Properties properties(String name)  {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream(name)) {
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }
    public int getProperties(String string) {
        return Integer.parseInt(config.getProperty(string));
    }


}
