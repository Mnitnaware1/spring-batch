package com.mastercard.mcbs.pbrc.offline.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

public class FileVerificationSkipper implements SkipPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileVerificationSkipper.class);

    @Override
    public boolean shouldSkip(Throwable exception, int skipCount) {
        if (exception instanceof FlatFileParseException) {
            FlatFileParseException flatFileParseException = (FlatFileParseException) exception;
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("An error occurred while processing the "
                + flatFileParseException.getLineNumber()
                + " line of the file. Below was the faulty "
                + "input.\n");
            errorMessage.append(flatFileParseException.getInput() + "\n");
            errorMessage.append("Error cause :" + flatFileParseException.getCause().getMessage() + "\n");
            LOGGER.error("{}", errorMessage.toString());
        } else {
            LOGGER.error("An error occurred details : {}", exception.getCause());
        }
        return true;
    }
}
