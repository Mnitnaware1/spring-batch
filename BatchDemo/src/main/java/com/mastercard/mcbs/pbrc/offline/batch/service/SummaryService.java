package com.mastercard.mcbs.pbrc.offline.batch.service;

import java.util.Map;

import com.mastercard.mcbs.pbrc.offline.batch.exception.ResourceNotFoundException;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRequest;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineSearchCriteria;



public interface SummaryService {

	Map<String, Object> getSummaryData(OfflineRequest offlineRequest, OfflineSearchCriteria offlineSearchCriteria) throws ResourceNotFoundException;
   
}
