package ru.education.threadpools.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolsConfig {

    private final String mainThreadNamePrefix;
    private final int mainDefaultPoolSize;
    private final int mainMaxPoolSize;
    private final int mainKeepAlive;
    private final int mainQueueCapacity;
    private final int mainAwaitTerminationPeriod;
    private final boolean mainAllowCoreThreadTimeout;
    private final boolean mainWaitForTasksToCompleteOnShutdown;

    private final String backgroundThreadNamePrefix;
    private final int backgroundDefaultPoolSize;
    private final int backgroundMaxPoolSize;
    private final int backgroundKeepAlive;
    private final int backgroundQueueCapacity;
    private final int backgroundAwaitTerminationPeriod;
    private final boolean backgroundAllowCoreThreadTimeout;
    private final boolean backgroundWaitForTasksToCompleteOnShutdown;

    @SuppressWarnings("checkstyle:ParameterNumber")
    public ThreadPoolsConfig(
            @Value("${spring.task.execution.main-async-executor.name:main-async-executor}")
            String mainThreadNamePrefix,
            @Value("${spring.task.execution.background-async-executor.name:background-async-executor}")
            String backgroundThreadNamePrefix,
            @Value("${spring.task.execution.main-async-executor.pool.core-size:2}")
            int mainDefaultPoolSize,
            @Value("${spring.task.execution.background-async-executor.pool.core-size:2}")
            int backgroundDefaultPoolSize,
            @Value("${spring.task.execution.main-async-executor.pool.max-size:20}")
            int mainMaxPoolSize,
            @Value("${spring.task.execution.background-async-executor.pool.max-size:20}")
            int backgroundMaxPoolSize,
            @Value("${spring.task.execution.main-async-executor.pool.keep-alive:30}")
            int mainKeepAlive,
            @Value("${spring.task.execution.background-async-executor.pool.keep-alive:30}")
            int backgroundKeepAlive,
            @Value("${spring.task.execution.main-async-executor.pool.queue-capacity:10}")
            int mainQueueCapacity,
            @Value("${spring.task.execution.background-async-executor.pool.queue-capacity:10}")
            int backgroundQueueCapacity,
            @Value("${spring.task.execution.main-async-executor.shutdown.await-termination-period:30}")
            int mainAwaitTerminationPeriod,
            @Value("${spring.task.execution.background-async-executor.shutdown.await-termination-period:30}")
            int backgroundAwaitTerminationPeriod,
            @Value("${spring.task.execution.main-async-executor.pool.allow-core-thread-timeout:true}")
            boolean mainAllowCoreThreadTimeout,
            @Value("${spring.task.execution.background-async-executor.pool.allow-core-thread-timeout:true}")
            boolean backgroundAllowCoreThreadTimeout,
            @Value("${spring.task.execution.main-async-executor.shutdown.wait-for-tasks-to-complete-on-shutdown:true}")
            boolean mainWaitForTasksToCompleteOnShutdown,
            @Value("${spring.task.execution.background-async-executor.shutdown.wait-for-tasks-to-complete-on-shutdown:true}")
            boolean backgroundWaitForTasksToCompleteOnShutdown) {

        this.mainThreadNamePrefix = mainThreadNamePrefix;
        this.backgroundThreadNamePrefix = backgroundThreadNamePrefix;

        // todo: сделать проверку на maxPoolSize > defaultPoolSize
        if (mainDefaultPoolSize < 1) {
            if (Runtime.getRuntime().availableProcessors() > 2) {
                this.mainDefaultPoolSize = 2;
                log.warn("mainDefaultPoolSize cant'be < 1, setting: 2");
            } else {
                this.mainDefaultPoolSize = 1;
                log.warn("mainDefaultPoolSize cant'be < 1, setting default: 1");
            }
        } else {
            this.mainDefaultPoolSize = mainDefaultPoolSize;
        }

        // todo: сделать проверку на maxPoolSize > defaultPoolSize
        if (backgroundDefaultPoolSize < 1) {
            if (Runtime.getRuntime().availableProcessors() > 2) {
                this.backgroundDefaultPoolSize = 2;
                log.warn("backgroundDefaultPoolSize cant'be < 1, setting: 2");
            } else {
                this.backgroundDefaultPoolSize = 1;
                log.warn("backgroundDefaultPoolSize cant'be < 1, setting default: 1");
            }
        } else {
            this.backgroundDefaultPoolSize = backgroundDefaultPoolSize;
        }

        if (mainMaxPoolSize < 1) {
            log.warn("mainMaxPoolSize can't be < 1, setting default: {}", Runtime.getRuntime().availableProcessors() - 1);
            this.mainMaxPoolSize = Runtime.getRuntime().availableProcessors() - 1;
        } else {
            this.mainMaxPoolSize = mainMaxPoolSize;
        }

        if (backgroundMaxPoolSize < 1) {
            log.warn("backgroundMaxPoolSize can't be < 1, setting default: {}", Runtime.getRuntime().availableProcessors() - 1);
            this.backgroundMaxPoolSize = Runtime.getRuntime().availableProcessors() - 1;
        } else {
            this.backgroundMaxPoolSize = backgroundMaxPoolSize;
        }

        if (mainKeepAlive <= 0) {
            this.mainKeepAlive = 30;
            log.warn("mainKeepAlive can't be <= 0s, setting default: 30s");
        } else {
            this.mainKeepAlive = mainKeepAlive;
        }

        if (backgroundKeepAlive <= 0) {
            this.backgroundKeepAlive = 30;
            log.warn("keepAlive can't be <= 0s, setting default: 30s");
        } else {
            this.backgroundKeepAlive = backgroundKeepAlive;
        }

        if (mainQueueCapacity < 0) {
            this.mainQueueCapacity = 10;
            log.warn("It's not good reason to use queue capacity < 0, setting default: 10");
        } else {
            this.mainQueueCapacity = mainQueueCapacity;
        }

        if (backgroundQueueCapacity < 0) {
            this.backgroundQueueCapacity = 10;
            log.warn("It's not good reason to use queue capacity < 0, setting default: 10");
        } else {
            this.backgroundQueueCapacity = backgroundQueueCapacity;
        }

        if (mainAwaitTerminationPeriod < 0) {
            this.mainAwaitTerminationPeriod = 20;
            log.warn("mainAwaitTerminationPeriod can't be < 0s, setting default: 20s");
        } else {
            this.mainAwaitTerminationPeriod = mainAwaitTerminationPeriod;
        }

        if (backgroundAwaitTerminationPeriod < 0) {
            this.backgroundAwaitTerminationPeriod = 20;
            log.warn("backgroundAwaitTerminationPeriod can't be < 0s, setting default: 20s");
        } else {
            this.backgroundAwaitTerminationPeriod = backgroundAwaitTerminationPeriod;
        }

        this.mainAllowCoreThreadTimeout = mainAllowCoreThreadTimeout;
        this.backgroundAllowCoreThreadTimeout = backgroundAllowCoreThreadTimeout;

        this.mainWaitForTasksToCompleteOnShutdown = mainWaitForTasksToCompleteOnShutdown;
        this.backgroundWaitForTasksToCompleteOnShutdown = backgroundWaitForTasksToCompleteOnShutdown;
    }

    /**
     * Gets bean of thread pool executor.
     *
     * @return mainThreadPoolExecutor
     */
    @Primary
    @Bean("mainThreadPoolExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {

        log.info("starting initialize mainThreadPoolExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(mainThreadNamePrefix);
        executor.setCorePoolSize(mainDefaultPoolSize);
        executor.setMaxPoolSize(mainMaxPoolSize);
        executor.setThreadFactory(new ApplicationCustomThreadFactory(mainThreadNamePrefix,Thread.MAX_PRIORITY));
        executor.setQueueCapacity(mainQueueCapacity);
        executor.setKeepAliveSeconds(mainKeepAlive);
        executor.setAllowCoreThreadTimeOut(mainAllowCoreThreadTimeout);
        executor.setWaitForTasksToCompleteOnShutdown(mainWaitForTasksToCompleteOnShutdown);
        executor.setAwaitTerminationSeconds(mainAwaitTerminationPeriod);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.afterPropertiesSet();

        log.info("mainThreadPoolExecutor initialized with start pool size: {}," +
                        " max pool size: {}, executor prefix: {}, queue capacity: {}, min pool size: {}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getThreadNamePrefix(),
                mainQueueCapacity, executor.getCorePoolSize());

        return executor;
    }

    /**
     * Gets bean of thread pool executor.
     *
     * @return mainThreadPoolExecutor
     */
    @Bean("backgroundThreadPoolExecutor")
    public ThreadPoolTaskExecutor backgroundThreadPoolExecutor() {

        log.info("starting initialize backgroundThreadPoolExecutor");

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(backgroundThreadNamePrefix);
        executor.setCorePoolSize(backgroundDefaultPoolSize);
        executor.setMaxPoolSize(backgroundMaxPoolSize);
        executor.setQueueCapacity(backgroundQueueCapacity);
        executor.setThreadFactory(new ApplicationCustomThreadFactory(mainThreadNamePrefix,Thread.NORM_PRIORITY));
        executor.setKeepAliveSeconds(backgroundKeepAlive);
        executor.setAllowCoreThreadTimeOut(backgroundAllowCoreThreadTimeout);
        executor.setWaitForTasksToCompleteOnShutdown(backgroundWaitForTasksToCompleteOnShutdown);
        executor.setAwaitTerminationSeconds(backgroundAwaitTerminationPeriod);
        executor.setTaskDecorator(new BackgroundThreadPoolTaskDecoratorImpl(executor));
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.afterPropertiesSet();

        log.info("backgroundThreadPoolExecutor initialized with start pool size: {}," +
                        " max pool size: {}, executor prefix: {}, queue capacity: {}",
                executor.getCorePoolSize(), executor.getMaxPoolSize(), executor.getThreadNamePrefix(),
                executor.getThreadPoolExecutor().getQueue().remainingCapacity());

        return executor;
    }
}