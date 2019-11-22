package com.mastercard.mcbs.pbrc.offline.batch.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FeederQueryBuilder {

    public static final String PROCESS_DATE = "prcss_dt";
    public static final String FORMAT_PROCESS_DATE_TO_DATE = "to_date(:prcss_dt,'yyyymmdd')";
    private String[] fileHeaders;
    private String insertHeaders;
    private String valuesHeaders;
    private String buildQuery;
    private String tableName;
    private String buildInsertQuery;
    private String buildUpdateQuery;

    /**
     * This is a constructor.
     *
     * @param tableName    .
     * @param tableColumns .
     */
    public FeederQueryBuilder(String tableName, String tableColumns) {
        List<String> columnsList = Lists.newArrayList(Splitter.on(",").split(tableColumns));
        setFileHeaders(tableColumns.split(","));
        setInsertHeaders(tableColumns);
        setValuesHeaders(buildValuesHeaders(columnsList));
        setTableName(tableName);
        setBuildQuery(buildInsertQuery());
        setBuildInsertQuery(buildInsertQuery());
    }

    public FeederQueryBuilder() {
    }

    private String buildInsertQuery() {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(getInsertHeaders())
                .append(" ) VALUES ( ")
                .append(getValuesHeaders())
                .append(")");
        return queryBuilder.toString();
    }

    private String buildValuesHeaders(final List<String> columnsList) {
        return String.join(
                ",",
                columnsList.stream()
                        .map(
                            name ->
                                        name.equalsIgnoreCase(PROCESS_DATE) ? FORMAT_PROCESS_DATE_TO_DATE : ":" + name)
                        .collect(Collectors.toList()));
    }
}
