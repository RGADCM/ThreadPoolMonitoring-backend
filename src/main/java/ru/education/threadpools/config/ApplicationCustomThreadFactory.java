package ru.education.threadpools.config;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;


public class ApplicationCustomThreadFactory extends CustomizableThreadFactory {

    public ApplicationCustomThreadFactory (String threadNamePrefix, int threadPriority){
        super.setThreadNamePrefix(threadNamePrefix);
        super.setThreadPriority(threadPriority);
    }
}
