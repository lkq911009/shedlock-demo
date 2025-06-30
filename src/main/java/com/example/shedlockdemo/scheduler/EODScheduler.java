package com.example.shedlockdemo.scheduler;

import com.example.shedlockdemo.service.EODService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EODScheduler {

    private static final Logger logger = LoggerFactory.getLogger(EODScheduler.class);

    @Autowired
    private EODService eodService;

    /**
     * Scheduled job that runs at 11:30 PM EST, Monday to Friday only.
     * Uses ShedLock to ensure only one instance runs in a distributed environment.
     * 
     * Cron expression breakdown:
     * - 0 30 23: 30 minutes past 23:00 (11:30 PM)
     * - ? * MON-FRI: Any day of month, but only Monday to Friday
     * - *: Any month
     * - ?: Any day of week (overridden by MON-FRI)
     * 
     * Timezone is set to America/New_York (EST/EDT)
     */
    @Scheduled(cron = "0 30 23 ? * MON-FRI", zone = "America/New_York")
    @SchedulerLock(name = "eod-job", lockAtLeastFor = "1m", lockAtMostFor = "30m")
    public void markEODJob() {
        logger.info("Starting scheduled EOD job at 11:30 PM EST");
        
        try {
            eodService.markEODForCurrentBusinessDate();
            logger.info("EOD job completed successfully");
        } catch (Exception e) {
            logger.error("Error occurred during EOD job execution", e);
            throw e; // Re-throw to ensure ShedLock marks the job as failed
        }
    }
} 