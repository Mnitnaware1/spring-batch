package com.mastercard.mcbs.pbrc.offline.batch.model;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummaryModel {
   
    @NotEmpty(message = "Invoice date is required.")
    @Pattern(regexp = "^(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])-[0-9]{4}$")
    private String invoiceDate;
    private String billingEvent;
    private String invoiceNumber;
    private List<String> ica;
    @Min(1)
    private int page;
}

