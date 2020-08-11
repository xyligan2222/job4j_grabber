package ru.job4j.grabber.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;

import java.sql.*;


import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public static void main(String[] args) {
        try (ConfigDB configDB = new ConfigDB("rabbit.properties")) {
            configDB.init();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", configDB.init());
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(10)
                   // .withIntervalInSeconds((configDB.getProperties("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();

        } catch (SchedulerException | IOException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("shutdown");
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("test");
            try (Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("connect")) {
                PreparedStatement st = connection.prepareStatement("insert into rabbit (created_date) values (?)");
                st.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                st.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}