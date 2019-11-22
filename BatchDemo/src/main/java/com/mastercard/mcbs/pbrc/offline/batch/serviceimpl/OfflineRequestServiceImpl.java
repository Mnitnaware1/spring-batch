package com.mastercard.mcbs.pbrc.offline.batch.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRequest;
import com.mastercard.mcbs.pbrc.offline.batch.repository.OfflineRequestRepository;
import com.mastercard.mcbs.pbrc.offline.batch.service.OfflineRequestService;

@Service
public class OfflineRequestServiceImpl implements OfflineRequestService {

	@Autowired
	private  OfflineRequestRepository offlineRequestRepository;
	

	public OfflineRequest getOfflineRequests() {
		return offlineRequestRepository.getOfflineRequest();
	}

}
