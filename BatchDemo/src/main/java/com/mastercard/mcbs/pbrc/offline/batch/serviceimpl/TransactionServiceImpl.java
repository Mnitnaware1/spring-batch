package com.mastercard.mcbs.pbrc.offline.batch.serviceimpl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastercard.mcbs.pbrc.offline.batch.exception.ResourceNotFoundException;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRequest;
import com.mastercard.mcbs.pbrc.offline.batch.repository.TransactionRepository;
import com.mastercard.mcbs.pbrc.offline.batch.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Map<String,Object> transactionDetails(OfflineRequest offlineRequest,String imeTraceId) throws ResourceNotFoundException {

		Optional<Map<String, Object>> transactionDetail = Optional.ofNullable(
				transactionRepository.getTransactionDetail(offlineRequest.getRlNam(),offlineRequest.getFdrTypeCd(),imeTraceId));
		Map<String, Object> offlineRequestTransactionQuery = null;
		if (transactionDetail.isPresent()) {
			offlineRequestTransactionQuery = transactionDetail.get();
		}
		return offlineRequestTransactionQuery;
	}
}