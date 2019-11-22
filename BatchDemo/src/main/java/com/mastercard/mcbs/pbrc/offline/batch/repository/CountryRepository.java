package com.mastercard.mcbs.pbrc.offline.batch.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mastercard.mcbs.pbrc.offline.batch.model.CountryRegion;


@Repository
public class CountryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
   
    @Transactional(readOnly = true)
    public List<CountryRegion> getCountry() {
        String sqlQuery = "SELECT alpha_cntry_cd,alpha_cntry_num FROM bcx_owner.country_region";
        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(CountryRegion.class));
    }

}
