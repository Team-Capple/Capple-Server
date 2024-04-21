package com.server.capple.config;

import com.server.capple.global.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(1); //스케줄러 쓰레드 개수 (질문 관리에서만 사용)
        threadPoolTaskScheduler.setErrorHandler(new SchedulerErrorHandler());
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    @Slf4j
    public static class SchedulerErrorHandler implements ErrorHandler {
        @Override
        public void handleError(Throwable t) {
            log.warn(String.format("[%s] %s", ((RestApiException) t).getErrorCode().getCode(), ((RestApiException) t).getErrorCode().getMessage()));
        }
    }
}
