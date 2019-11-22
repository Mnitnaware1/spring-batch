package com.mastercard.mcbs.pbrc.offline.batch.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class FeederBatchAudit implements Serializable {

    private static final long serialVersionUID = 7595221872355228935L;
    private Long auditId;
    private String fileName;
    private BigDecimal indexRecordCount;
    private FeederFileStatus feederFileStatus;
    private String targetTableName;
    private String fileType;
    private String fileLocation;
    private BigDecimal jobExecutionId;
    private BigDecimal readCount;
    private BigDecimal writeCount;
    private BigDecimal readSkipCount;
    private BigDecimal writeSkipCount;
    private BigDecimal rollbackCount;
    private String exitCode;
    private byte[] exitDescription;
    private Date batchStartTime;
    private Date batchEndTime;
    private Date createdDate;
    private String targetColumns;
    private String sourceColumns;
}
