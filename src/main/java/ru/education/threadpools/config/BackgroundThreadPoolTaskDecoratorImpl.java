package ru.education.threadpools.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
public class BackgroundThreadPoolTaskDecoratorImpl implements TaskDecorator {

    private final ThreadPoolTaskExecutor backgroundThreadPoolTaskExecutor;

    public BackgroundThreadPoolTaskDecoratorImpl(ThreadPoolTaskExecutor backgroundThreadPoolTaskExecutor) {
        this.backgroundThreadPoolTaskExecutor = backgroundThreadPoolTaskExecutor;
    }

    @Override
    public Runnable decorate(Runnable runnable) {
//        log.info("ThreadPoolMonitoring. name: {}, active/max: {}/{}, queue: {}, completed tasks: {}",
//                backgroundThreadPoolTaskExecutor.getThreadNamePrefix(),
//                backgroundThreadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount(),
//                backgroundThreadPoolTaskExecutor.getMaxPoolSize(),
//                backgroundThreadPoolTaskExecutor.getThreadPoolExecutor().getTaskCount(),
//                backgroundThreadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount());
        return runnable;
    }
}
