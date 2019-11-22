package com.mastercard.mcbs.pbrc.offline.batch.model;

import lombok.Data;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.mastercard.mcbs.pbrc.offline.batch.service.SequenceService;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.ACTION_CODE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.FINAL_DATE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.ID;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.LAST_MOD_DT;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.PBLSH_END_DT;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.PBLSH_STRT_DT;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.PRICE_GUIDE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.STAT;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.STAT_CD;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



@Data
public class PriceGuideBatchFileObject implements SqlParameterSource {


    private Map<String, String> properties = new HashMap<>();
    private String commaSeparatedVariables;
    private SequenceService sequenceService;
    private String tableName;

    private SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");


    /**
     * This is a constructor .
     *
     * @param set             .
     * @param tableName       .
     * @param sequenceService .
     */
    public PriceGuideBatchFileObject(FieldSet set, String tableName, SequenceService sequenceService) {
        this.tableName = tableName;
        this.sequenceService = sequenceService;
        Arrays.asList(set.getNames()).stream().forEach(field -> setProperty(field, set.readString(field)));
    }

    @Override
    public boolean hasValue(String priceGuideColumn) {
        return properties.get(priceGuideColumn.toUpperCase()) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Object getValue(String priceGuideValue) {
        if (PBLSH_END_DT.equalsIgnoreCase(priceGuideValue.toUpperCase())) {
            return FINAL_DATE;
        }
        if (STAT_CD.equalsIgnoreCase(priceGuideValue.toUpperCase())
                || STAT.equalsIgnoreCase(priceGuideValue.toUpperCase())) {
            return properties.get(ACTION_CODE);
        }
        if (LAST_MOD_DT.equalsIgnoreCase(priceGuideValue.toUpperCase())
                || PBLSH_STRT_DT.equalsIgnoreCase(priceGuideValue.toUpperCase())) {
            return formatter.format(new Date());
        }
        if (priceGuideValue.toUpperCase().startsWith(PRICE_GUIDE) && priceGuideValue.toUpperCase().endsWith(ID)) {
            return sequenceService.getNextSeries(tableName);
        }
        return properties.get(priceGuideValue.toUpperCase());
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
