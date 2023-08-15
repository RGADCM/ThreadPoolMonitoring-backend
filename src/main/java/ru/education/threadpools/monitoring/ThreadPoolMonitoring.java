package ru.education.threadpools.monitoring;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.education.threadpools.dto.ThreadPoolDto;

import java.lang.management.ManagementFactory;
import java.util.List;

@Component
@RestControllerEndpoint(id = "threadPoolMonitoring")
public class ThreadPoolMonitoring {

    private final ThreadPoolTaskExecutor backgroundThreadPoolTaskExecutor;
    private final ThreadPoolTaskExecutor mainThreadPoolTaskExecutor;
    private final OperatingSystemMXBean osBean;

    /**
     * todo: Необходимо перенести из контроллера всю логику создания рандомных задач
     *
     * @param mainThreadPoolTaskExecutor - autoinject
     * @param backgroundThreadPoolTaskExecutor - autoinject
     */
    public ThreadPoolMonitoring(@Qualifier("backgroundThreadPoolExecutor")
                                ThreadPoolTaskExecutor backgroundThreadPoolTaskExecutor,
                                @Qualifier("mainThreadPoolExecutor")
                                ThreadPoolTaskExecutor mainThreadPoolTaskExecutor) {
        this.osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        this.mainThreadPoolTaskExecutor = mainThreadPoolTaskExecutor;
        this.backgroundThreadPoolTaskExecutor = backgroundThreadPoolTaskExecutor;
    }

    @GetMapping("/backgroundThreadPoolExecutor")
    public @ResponseBody ResponseEntity<ThreadPoolDto> backgroundThreadPoolExecutorHealth() {

        ThreadPoolDto threadPoolDto = prepareStatisticForBackgroundThreadPoolExecutor();

        return new ResponseEntity<>(threadPoolDto, HttpStatus.OK);
    }

    @GetMapping("/allThreadPoolExecutors")
    public @ResponseBody ResponseEntity<List<ThreadPoolDto>> getAllThreadPoolExecutorsHealth() {

        ThreadPoolDto backgroundThreadPoolDto = prepareStatisticForBackgroundThreadPoolExecutor();
        ThreadPoolDto mainThreadPoolDto = prepareStatisticForMainThreadPoolExecutor();

        return new ResponseEntity<>(List.of(backgroundThreadPoolDto, mainThreadPoolDto), HttpStatus.OK);
    }

    @GetMapping("/mainThreadPoolExecutor")
    public @ResponseBody ResponseEntity<ThreadPoolDto> mainThreadPoolExecutorHealth() {

        ThreadPoolDto threadPoolDto = prepareStatisticForMainThreadPoolExecutor();

        return new ResponseEntity<>(threadPoolDto, HttpStatus.OK);
    }

    private ThreadPoolDto prepareStatisticForMainThreadPoolExecutor() {
        return new ThreadPoolDto(
                mainThreadPoolTaskExecutor.getThreadNamePrefix(),
                osBean.getAvailableProcessors(),
                mainThreadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount(),
                mainThreadPoolTaskExecutor.getMaxPoolSize(),
                mainThreadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size(),
                mainThreadPoolTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity(),
                mainThreadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount(),
                osBean.getCpuLoad() * 100,
                osBean.getProcessCpuLoad() * 100);
    }

    private ThreadPoolDto prepareStatisticForBackgroundThreadPoolExecutor() {
        return new ThreadPoolDto(
                backgroundThreadPoolTaskExecutor.getThreadNamePrefix(),
                osBean.getAvailableProcessors(),
                backgroundThreadPoolTaskExecutor.getThreadPoolExecutor().getActiveCount(),
                backgroundThreadPoolTaskExecutor.getMaxPoolSize(),
                backgroundThreadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size(),
                backgroundThreadPoolTaskExecutor.getThreadPoolExecutor().getQueue().remainingCapacity(),
                backgroundThreadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount(),
                osBean.getCpuLoad() * 100,
                osBean.getProcessCpuLoad() * 100);
    }
}

