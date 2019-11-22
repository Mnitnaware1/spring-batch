package com.mastercard.mcbs.pbrc.offline.batch.service;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.RPT_TYPE_CODE_SUMMARY;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.mastercard.mcbs.pbrc.offline.batch.exception.ResourceNotFoundException;
import com.mastercard.mcbs.pbrc.offline.batch.model.JobParamModel;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRequest;
import com.mastercard.mcbs.pbrc.offline.batch.repository.OfflineRequestRepo;
import com.mastercard.mcbs.pbrc.offline.batch.util.ObjectMapperUtil;

import lombok.extern.slf4j.Slf4j;;

@Service
@Slf4j
public class OfflineBatchService {

	@Autowired(required = true)
	private JobLauncher jobLauncher;

	@Autowired
	private JobParamModel jobParamModel;

	@Autowired(required = true)
	@Qualifier(value = "feederBatchJob")
	private Job offlineBatchJob;

	@Autowired
	private OfflineRequestService offlineRequestService;

	@Autowired
	private SummaryService summaryService;

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private OfflineRequestServiceTemp temp;

	public OfflineBatchService(JobLauncher launcher, Job offlineBatchJob) {
		this.jobLauncher = launcher;
		this.offlineBatchJob = offlineBatchJob;

	}

	/**
	 * .
	 * 
	 * @param jobLauncher                .
	 * @param feederBatchJob             .
	 * @param restTemplate               .
	 * @param jobParamModel
	 * @param priceGuideConfigProperties
	 */

	/**
	 * . scheduleFixedDelayTask is method to schedule time delay
	 * 
	 * @throws ResourceNotFoundException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Scheduled(fixedDelay = 30000)
	public void scheduleFixedDelayTask()
			throws ResourceNotFoundException, JsonParseException, JsonMappingException, IOException {

		OfflineRequest offlineRequest = offlineRequestService.getOfflineRequests();
		//get data in map instead of poj0
		Map<String, Object> offlineReq = temp.getOfflineRequest();
		
		if (offlineRequest != null) {
			Map<String, Object> offlineRequestParams = null;
			log.info("Offline request data found {}", offlineRequest);
			try {
				if (offlineRequest.getRptTypeCd().equals(RPT_TYPE_CODE_SUMMARY)) {
					offlineRequestParams = summaryService.getSummaryData(offlineRequest,
							ObjectMapperUtil.convertObjectToOfflineSearchCriteria(offlineRequest.getSrchCrtrTxt()));
				} else {
					offlineRequestParams = transactionService.transactionDetails(offlineRequest, ObjectMapperUtil
							.convertObjectToOfflineSearchCriteria(offlineRequest.getSrchCrtrTxt()).getImeTraceId());
				}
				jobParamModel.setQueryBuilder(offlineRequestParams.get("query").toString());
				Pattern pattern = Pattern.compile(",");
				String[] headers = pattern.split(offlineRequestParams.get("headers").toString());
				jobParamModel.setHeaders(headers);

				JobParametersBuilder batchJobParam = new JobParametersBuilder();
				batchJobParam.addDate("date", new Date());

				jobLauncher.run(offlineBatchJob, batchJobParam.toJobParameters());
			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException exception) {
				log.error("Unable to process offline request! Job Failed! : {}", exception.getLocalizedMessage());
			}
		} else {
			log.info("No file found with 'READY' status.");
		}
	}

}
