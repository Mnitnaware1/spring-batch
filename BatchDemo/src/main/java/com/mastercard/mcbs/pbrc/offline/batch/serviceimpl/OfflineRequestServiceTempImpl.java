package com.mastercard.mcbs.pbrc.offline.batch.serviceimpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastercard.mcbs.pbrc.offline.batch.repository.OfflineRequestRepo;
import com.mastercard.mcbs.pbrc.offline.batch.service.OfflineRequestServiceTemp;
@Service
public class OfflineRequestServiceTempImpl implements OfflineRequestServiceTemp {

	@Autowired
	private OfflineRequestRepo repo;

	@Override
	public Map<String, Object> getOfflineRequest() {
		return repo.executeOfflineRequest();
	}

}
