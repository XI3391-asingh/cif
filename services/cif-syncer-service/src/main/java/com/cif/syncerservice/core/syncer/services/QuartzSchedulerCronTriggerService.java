package com.cif.syncerservice.core.syncer.services;

import com.cif.syncerservice.core.syncer.component.ErrorRecordProcessingJob;
import com.cif.syncerservice.core.syncer.component.JobTriggerBuilder;
import com.cif.syncerservice.db.SyncerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.Serializable;
import java.net.http.HttpClient;
import java.text.ParseException;
import java.util.Map;
import java.util.Properties;

public class QuartzSchedulerCronTriggerService implements Serializable {

    private boolean overwriteExistingJobs = false;
    private final SyncerRepository syncerRepository;
    private final HttpClient httpClient;
    private final Map<String, String> quartzConfiguration;


    public QuartzSchedulerCronTriggerService(SyncerRepository syncerRepository, HttpClient httpClient, Map<String, String> quartzConfiguration) {
        this.syncerRepository = syncerRepository;
        this.httpClient = httpClient;
        this.quartzConfiguration = quartzConfiguration;
    }

    public void fireJob() throws SchedulerException, InterruptedException, ParseException, JsonProcessingException {
        Properties properties = new Properties();
        properties.putAll(quartzConfiguration);
        StdSchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();
        stdSchedulerFactory.initialize(properties);

        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        scheduler.getContext().put("repository", syncerRepository);
        scheduler.getContext().put("httpclient", httpClient);
        scheduler.start();

        JobBuilder jobBuilder = JobBuilder.newJob(ErrorRecordProcessingJob.class);
        JobDetail jobDetail = jobBuilder.withIdentity("errorRecordProcessingJob", "integrationApiJobGroup").build();

        scheduler.deleteJob(new JobKey("errorRecordProcessingJob", "integrationApiJobGroup"));
        scheduler.scheduleJob(jobDetail, JobTriggerBuilder.fireAfterAMinEveryDayStartingAt());
    }
}
