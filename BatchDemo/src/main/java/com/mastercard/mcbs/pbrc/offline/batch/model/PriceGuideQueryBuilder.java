package com.mastercard.mcbs.pbrc.offline.batch.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.PRICE_GUIDE;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PriceGuideQueryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceGuideQueryBuilder.class);

    private String ignoreColumns;


    /**
     * getTableMetaDataModel is a method .
     *
     * @param tableName      .
     * @param columnsListStr .
     * @param ignoreColumns  .
     * @return TableMetaDataModel .
     */
    public FeederQueryBuilder getTableMetaDataModel(final String tableName, String columnsListStr, String ignoreColumns) {
        this.ignoreColumns = ignoreColumns;
        FeederQueryBuilder dataModel = new FeederQueryBuilder();
        List<String> columnsList = Lists.newArrayList(Splitter.on(",").split(columnsListStr));
        String[] columns = columnsList.stream().filter(str -> !str.startsWith(PRICE_GUIDE))
                .toArray(String[]::new);
        dataModel.setFileHeaders(columns);
        dataModel.setValuesHeaders(buildValuesHeaderFromDbColumns(columnsList));
        dataModel.setTableName(tableName);
        dataModel.setBuildInsertQuery(buildInsertQuery(dataModel));
        dataModel.setBuildUpdateQuery(buildUpdateQuery(dataModel));
        LOGGER.info("Insert query : {}", dataModel.getBuildInsertQuery());
        return dataModel;
    }

    private String buildInsertQuery(FeederQueryBuilder dataModel) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(dataModel.getTableName());
        query.append (" (");
        query.append(dataModel.getInsertHeaders());
        query.append(", STAT_CD, LAST_MOD_DT) VALUES (");
        query.append(dataModel.getValuesHeaders());
        query.append(", :STAT_CD, TO_DATE(:LAST_MOD_DT, 'DD-MM-YYYY'))");
        return query.toString();
    }

    private String buildUpdateQuery(FeederQueryBuilder dataModel) {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(dataModel.getTableName()).append(" SET ");
        sql.append(Arrays.asList(dataModel.getFileHeaders())
                .stream()
                .filter(s -> ignoreColumns.indexOf(s) == -1)
                .filter(s -> s.indexOf("_DT") == -1)
                .map(s -> s.toUpperCase() + " = :" + s.toLowerCase() + ", ")
                .collect(Collectors.joining()));
        return sql + " PBLSH_STRT_DT = TO_DATE(:PBLSH_STRT_DT, 'DD-MM-YYYY'), PBLSH_END_DT = TO_DATE(:PBLSH_END_DT, 'DD-MM-YYYY')"
                + " WHERE BILL_EVENT_CNTRY_ID=:BILL_EVENT_CNTRY_ID ";
    }

    private String buildValuesHeaderFromDbColumns(final List<String> columnsList) {
        String valueFields = String.join(",", columnsList
                .stream()
                .filter(s -> ignoreColumns.indexOf(s) == -1)
                .filter(s -> s.indexOf("_DT") == -1)
                .map(name -> (":" + name))
                .collect(Collectors.toList()));

        valueFields += ",TO_DATE(:PBLSH_STRT_DT, 'DD-MM-YYYY')";
        return valueFields;
    }
}
