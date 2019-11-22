package com.mastercard.mcbs.pbrc.offline.batch.service;

import java.util.Map;

import com.mastercard.mcbs.pbrc.offline.batch.exception.ResourceNotFoundException;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRequest;


public interface TransactionService {
    
    public Map<String,Object> transactionDetails(OfflineRequest offlineRequest,String imeTraceId)
        throws ResourceNotFoundException;

}