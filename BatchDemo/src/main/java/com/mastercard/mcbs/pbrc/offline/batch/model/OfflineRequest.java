package com.mastercard.mcbs.pbrc.offline.batch.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "offline_requests")
public class OfflineRequest {
	@Id
	private String rqstId;

	private String fdrTypeCd;

	private String rptTypeCd;

	private String srchCrtrTxt;

	private String userId;
	
	private String rlNam;

	/*
	 * private String createdTimestamp;
	 * 
	 * private String lastUpdatedTimestamp;
	 * 
	 * private String status;
	 * 
	 * private String path;
	 * 
	 * private String role;
	 * 
	 * private String isDeleted;
	 */
}
