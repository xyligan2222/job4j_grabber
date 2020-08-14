package ru.job4j.grabber.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

public interface Grab {
    /*
    this method for run application
     */
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}