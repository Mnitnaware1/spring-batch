package com.mastercard.mcbs.pbrc.offline.batch.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.stereotype.Component;

@Component
public class JobExceptionHandler implements ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExceptionHandler.class);

    @Override
    public void handleException(RepeatContext repeatContext, Throwable throwable) throws Throwable {
        LOGGER.info("Unable to process job: {}", throwable.getLocalizedMessage());
    }
}
