package com.cif.syncerservice.core.syncer.component;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.Trigger;

import java.text.ParseException;
import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;


public final class JobTriggerBuilder {

    private JobTriggerBuilder() {
    }

    private static final String GROUP_NAME = "SyncerServiceCron";

    public static Trigger fireAfterAMinEveryDayStartingAt() throws ParseException {
        Trigger trigger = newTrigger()
                .withIdentity("fireEverydayIntervalOfFiveMinute", GROUP_NAME)
                .startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("0 0/5 * 1/1 * ? *")))
                .build();
        return trigger;
    }

}
