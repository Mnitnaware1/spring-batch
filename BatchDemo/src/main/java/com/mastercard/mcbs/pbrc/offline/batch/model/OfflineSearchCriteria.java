package com.mastercard.mcbs.pbrc.offline.batch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfflineSearchCriteria {


    private String invoiceDate;
    private String billingEvent;
    private String invoiceNumber;
    private String imeTraceId;
    private List<String> ica;

}
