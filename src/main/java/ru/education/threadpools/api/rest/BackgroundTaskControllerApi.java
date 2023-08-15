package ru.education.threadpools.api.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface BackgroundTaskControllerApi {

    @GetMapping("/api/jobs/check/cancel/{jobId}")
    void cancel(@PathVariable Long jobId);

    @GetMapping("/api/jobs/startRandomJob")
    void startRandomJob();
}
