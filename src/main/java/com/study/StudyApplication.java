package com.study;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author whh
 */
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class StudyApplication {
    protected static Logger LOGGER = LoggerFactory.getLogger(StudyApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(StudyApplication.class, args);
        } catch (Exception e) {
            LOGGER.error("启动失败", e);
        }
    }

}
