package org.example.service;

import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class TaskService {

    private static final Logger logger = Logger.getLogger(TaskService.class.getName());


    @Scheduled(cron = "${task.report:0 25 10 * * *}")
    public void cronDailyReport() {
        logger.info("Start daily report task...");
    }
}