package com.lege;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.spring.autoconfigure.EnableSimbot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableSimbot
@Slf4j
@SpringBootApplication
public class LegeRobotApplication {

    public static void main(String[] args) {

        SpringApplication.run(LegeRobotApplication.class, args);
        log.info("机器人启动成功!");
    }

}
