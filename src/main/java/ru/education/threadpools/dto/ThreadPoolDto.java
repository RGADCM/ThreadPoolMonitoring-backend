package ru.education.threadpools.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ThreadPoolDto {

    String threadNamePrefix;
    int availableProcessors;
    int activeThreadsCount;
    int maxPoolSize;
    int queuedTask;
    int queueRemainingCapacity;
    long completedTaskCount;
    double systemCpuLoadPercent;
    double processCpuLoadPercent;
}
