package com.mastercard.mcbs.pbrc.offline.batch.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ElementMappings implements Serializable {
    /**.
     *
     */
    private static final long serialVersionUID = 1L;
    private String dataElementNam;
    private String asfieldsTxt;
    private char enableSw;
    private String tableNam;

    public ElementMappings() {
        super();
    }


    
}