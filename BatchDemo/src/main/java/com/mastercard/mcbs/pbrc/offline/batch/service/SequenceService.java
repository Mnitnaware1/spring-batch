package com.mastercard.mcbs.pbrc.offline.batch.service;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.PRICE_GUIDE;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SequenceService(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * .
     * This method returns the next value of the series.
     *
     * @param tableName .
     * @return Long value.
     */
    public Long getNextSeries(String tableName) {
        String sequence = tableName.trim().replace(PRICE_GUIDE, "PG") + "_ID_SEQ";
        String query = " SELECT ".concat(sequence).concat(".NEXTVAL FROM dual ");
        List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
        return Long.valueOf(list.get(0).get("NEXTVAL").toString());
    }

}
