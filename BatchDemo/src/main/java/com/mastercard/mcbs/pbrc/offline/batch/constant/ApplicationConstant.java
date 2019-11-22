package com.mastercard.mcbs.pbrc.offline.batch.constant;

public class ApplicationConstant {

    public static final String FEEDER_STEP = "feederStep";
    public static final String FEEDER_BATCH_JOB = "feederBatchJob";
    public static final String BCX_OWNER = "BCX_OWNER.";
    public static final String PRICE_GUIDE_FLAG = "PRICE_GUIDE";
    public static final String FEEDER_FLAG = "FEEDER";

    public static final String PBLSH_END_DT = "PBLSH_END_DT";
    public static final String FINAL_DATE = "31-12-2199";
    public static final String LAST_MOD_DT = "LAST_MOD_DT";
    public static final String PBLSH_STRT_DT = "PBLSH_STRT_DT";
    public static final String STAT_CD = "STAT_CD";
    public static final String STAT = "STAT";
    public static final String ACTION_CODE = "ACTION_CODE";
    public static final String PRICE_GUIDE = "PRICE_GUIDE";
    public static final String ID = "ID";
    public static final String WORK = "work";
    public static final String ERROR = "error";
    
    /*** Exceptions.***/
    public static final String RECORD_NOT_FOUND_MSG = "No summary records/data found with these details.";
    public static final String VALIDATION_FAILED = "Validation Failed";
    public static final String SERVER_ERROR = "Server Error";
    public static final String CHECK_LOGS_ROOT_CAUSE = "Check logs for root cause";
    public static final String RECORD_NOT_FOUND = "Record Not Found";
    public static final String MESSAGE = "message";
    public static final String ERRORS = "errors";
    public static final String TIMESTAMP = "timestamp";

    public static final String BAD_REQUEST_CODE = "400";
    public static final String ERROR_DESCRIPTION = "Bad Request";
    public static final String RECORD_NOT_FOUND_CODE = "404";
    public static final String SERVER_ERROR_CODE = "500";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String INTERNAL_SERVER_ERROR_DETAIL = "Could not process the request due to Internal Server Error";

    /** country region constants. **/
    public static final String CHARGE_DETAIL = "'CHARGE_DETAIL'";
    public static final String TO_COUNTRY_CODE = "toCountryCode";
    public static final String FROM_COUNTRY_CODE = "fromCountryCode";

    public static final String SUMMARY_SEARCH_TYPE = "SUMMARYSEARCH";
    public static final String SUMMARY_SEARCH_EXCEL = "SUMMARYSEARCHEXCEL";
    public static final String DETAIL_SEARCH = "DETAILSEARCH";
    public static final String DETAIL_SEARCH_EXCEL = "DETAILSEARCHEXCEL";

    /** headers. **/
    public static final String CORRELATION_ID = "correlationId";

    public static final String SUCCESS_STR = "success";
    public static final String BILLING_EVENT_DETAIL = "_BILLING_EVENT_DETAIL EVENT";
    public static final String TRANSACTION_DETAIL = "_TRANSACTION_DETAIL TRANSACTION";
    public static final String COMMA = ",";
    public static final String AS = " AS ";
    
    /** tran detail. **/
    public static final String RULE_SEQUENCE_TBL = "'RULES_SEQUENCE'";
    public static final String SINGLE_QUOTE = "\'";
    public static final String EVENT_DETAIL_TBL = "_BILLING_EVENT_DETAIL'";
    public static final String TRANSACTION_DETAIL_TBL = "_TRANSACTION_DETAIL'";
    public static final String RULE_SEQUENCE = "RULES_SEQUENCE";
    public static final String RULE_SEQUENCE_ALIAS = " RULE";
    public static final String ELEMENT_DETAIL_NOT_FOUND_MSG = "No detail found for given role";
    public static final String EVENT_ALIAS = "EVENT.";
    public static final String TRANSACTION_ALIAS = "TRANSACTION.";
    public static final String RULE_ALIAS = "RULE.";
    public static final String EVENT_DETAIL = "_BILLING_EVENT_DETAIL";
    public static final String IS_RULE_SEQUENCE = "isRuleSequence";
    public static final String AS_QUERY = "asClause";
    
    public static final String SCHEMA_NAME = "bcx_owner.";
    public static final String SELECT_CLAUSE = "SELECT ";

    /*** Billing search field constants. ***/
    public static final String ACTIVITY_ICA = "activity_ica";
    public static final String INV_ICA = "inv_ica";
    public static final String EVENT_SRC_NAME = "event_src_nam";
    public static final String BILL_EVENT_ID = "bill_event_id";
    public static final String INV_NUM = "inv_num";
    public static final String CHRG_DT = "chrg_dt";
    public static final String ICA_LIST = "icaList";
    
    /**offline batch constants */
    public static final String RPT_TYPE_CODE_SUMMARY= "SUMMARY";

    private ApplicationConstant() {
    }
}
