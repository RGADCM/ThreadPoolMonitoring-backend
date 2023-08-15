package ru.education.threadpools.api.rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/")
@Api("REST-api для работы с фоновыми задачами")
public class StartTaskController implements BackgroundTaskControllerApi {

    private final ThreadPoolTaskExecutor mainThreadPoolTaskExecutor;
    private final ThreadPoolTaskExecutor backgroundThreadPoolTaskExecutor;

    /**
     * todo: Необходимо перенести из контроллера всю логику создания рандомных задач
     *
     * @param mainThreadPoolTaskExecutor - autoinject
     * @param backgroundThreadPoolTaskExecutor - autoinject
     */
    public StartTaskController(@Qualifier("mainThreadPoolExecutor") ThreadPoolTaskExecutor mainThreadPoolTaskExecutor,
                               @Qualifier("backgroundThreadPoolExecutor") ThreadPoolTaskExecutor backgroundThreadPoolTaskExecutor) {
        this.backgroundThreadPoolTaskExecutor = backgroundThreadPoolTaskExecutor;
        this.mainThreadPoolTaskExecutor = mainThreadPoolTaskExecutor;
    }

    @Override
    public void startRandomJob() {
        for (int i = 0; i < ThreadLocalRandom.current().nextLong(1000, 3000); i++) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextLong(10, 100000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, backgroundThreadPoolTaskExecutor);
            } else {
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextLong(10, 100000));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }, mainThreadPoolTaskExecutor);
            }
        }
    }

    @Override
    public void cancel(Long jobId) {

    }
}
