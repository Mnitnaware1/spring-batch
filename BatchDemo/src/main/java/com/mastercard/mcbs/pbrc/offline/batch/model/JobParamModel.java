package com.mastercard.mcbs.pbrc.offline.batch.model;

import lombok.Data;

@Data
public class JobParamModel {

	private FeederBatchAudit feederBatchAudit;
	private String queryBuilder;
	private String[] headers;
	
}
