package com.mastercard.mcbs.pbrc.offline.batch.listener;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mastercard.mcbs.pbrc.offline.batch.model.FeederBatchAudit;
import com.mastercard.mcbs.pbrc.offline.batch.model.FeederFileStatus;
import com.mastercard.mcbs.pbrc.offline.batch.model.JobParamModel;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class FeederJobExecutionListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeederJobExecutionListener.class);

    @Value("${file.watcher.rest.endpoint}")
    private String fileWatcherRestEndpoint;
    
    @Autowired
    private JobParamModel jobParamModel;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        LOGGER.info("Job Started At : {}", jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logResult(jobExecution);
        Optional<StepExecution> findFirst = jobExecution.getStepExecutions().stream().findFirst();
        if (findFirst.isPresent()) {
            StepExecution stepExecution = findFirst.get();
            LOGGER.info("Total Records Read from file: {}", stepExecution.getReadCount());
            LOGGER.info("Total Records Write to Database : {}", stepExecution.getWriteCount());
            LOGGER.info("Total Records Skipped due to error: {}", stepExecution.getReadSkipCount());
            LOGGER.info("Total Records Write Skipped to Database : {}", stepExecution.getWriteSkipCount());
//            updateFeederAuditBatch(stepExecution);
        }
    }

    private void logResult(JobExecution jobExecution) {
        DateTime dateTimeStart = new DateTime(jobExecution.getStartTime());
        DateTime dateTimeEnd = new DateTime(jobExecution.getEndTime());
        LOGGER.info("Job Completed At : {}, Exit Status : {}, Job Detail : {}",
                jobExecution.getEndTime(),
                jobExecution.getStatus(),
                jobExecution);
        LOGGER.info("Job Executed for : {} hours, {} minutes, {}seconds.",
                Hours.hoursBetween(dateTimeStart, dateTimeEnd).getHours() % 24,
                Minutes.minutesBetween(dateTimeStart, dateTimeEnd).getMinutes() % 60,
                Seconds.secondsBetween(dateTimeStart, dateTimeEnd).getSeconds() % 60);
    }

    /**
     * This method is to update feeder auditBatch.
     * @param stepExecution .
     */
    public void updateFeederAuditBatch(StepExecution stepExecution) {
        FeederBatchAudit feederBatchAudit = buildFeederBatchAudit(stepExecution);
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity(fileWatcherRestEndpoint, feederBatchAudit, FeederBatchAudit.class);
        } catch (Exception exception) {
            LOGGER.error("Exception occurred while saving to file watcher : {}", exception.getMessage());
        }
    }

    private FeederBatchAudit buildFeederBatchAudit(StepExecution stepExecution) {
        FeederBatchAudit audit = jobParamModel.getFeederBatchAudit();
        audit.setFeederFileStatus(FeederFileStatus.valueOf(stepExecution.getExitStatus().getExitCode()));
        audit.setJobExecutionId(new BigDecimal(stepExecution.getJobExecutionId()));
        audit.setReadCount(new BigDecimal(stepExecution.getReadCount()));
        audit.setWriteCount(new BigDecimal(stepExecution.getWriteCount()));
        audit.setReadSkipCount(new BigDecimal(stepExecution.getReadSkipCount()));
        audit.setWriteSkipCount(new BigDecimal(stepExecution.getWriteSkipCount()));
        audit.setRollbackCount(new BigDecimal(stepExecution.getRollbackCount()));
        audit.setExitCode(stepExecution.getExitStatus().getExitCode());
        audit.setExitDescription(stepExecution.getExitStatus().getExitDescription().getBytes());
        audit.setBatchStartTime(stepExecution.getStartTime());
        audit.setBatchEndTime(stepExecution.getEndTime());
        return audit;
    }

    public void setFileWatcherRestEndpoint(String fileWatcherRestEndpoint) {
        this.fileWatcherRestEndpoint = fileWatcherRestEndpoint;
    }
}
