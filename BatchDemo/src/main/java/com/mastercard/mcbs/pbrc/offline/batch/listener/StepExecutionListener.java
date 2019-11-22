package com.mastercard.mcbs.pbrc.offline.batch.listener;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.ERROR;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.WORK;

import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.item.file.FlatFileParseException;

import com.mastercard.mcbs.pbrc.offline.batch.model.FeederBatchFileObject;

public class StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StepExecutionListener.class);

    private String filePath;

    public StepExecutionListener(String filePath) {
        this.filePath = filePath;
    }

    /**
     * This method will be executed when there is a skip in reading the records.
     * @param exception .
     */
    @OnSkipInRead
    public void onSkipInRead(Throwable exception) {
        if (exception instanceof FlatFileParseException) {
            FlatFileParseException flatFileParseException = (FlatFileParseException) exception;
            onSkip(flatFileParseException.getInput());
        } else {
            LOGGER.error("Exception while reading file need manually interaction. file {}, exception {}",
                filePath, exception.getMessage());
        }
    }

    /**
     * This is a method which will be executed when the skips writing the records.
     * @param item .
     * @param exception .
     */
    @OnSkipInWrite
    public void onSkipInWrite(FeederBatchFileObject item, Throwable exception) {
        StringJoiner joiner = new StringJoiner(",");
        for (String s : item.getProperties().values()) {
            joiner.add(s);
        }
        onSkip(joiner.toString());
    }

    private void onSkip(String item) {
        String moveDestDir = String.valueOf(filePath).replace(WORK, ERROR);
         
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
