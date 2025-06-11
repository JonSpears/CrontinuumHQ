package com.jonspears.schedulerservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class JobConcurrencyException extends RuntimeException {
    public JobConcurrencyException() {
        super("Job is already being executed by another process");
    }

    public JobConcurrencyException(String jobId) {
        super(String.format("Job %s is already locked for execution", jobId));
    }
}