package com.mastercard.mcbs.pbrc.offline.batch.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mastercard.mcbs.pbrc.offline.batch.model.Country;
import com.mastercard.mcbs.pbrc.offline.batch.service.CountryService;

@Configuration
public class CountryRegionConfig {
    
    /*** .
     * @param countryService .
     * @return country
     */
    @Bean
    public Country getCountryRegionCode(CountryService countryService) {
        Map<String, Object> countryRegions = countryService.getCountryRegion();
        Country country = new Country();
        country.setCountryMap(countryRegions);
        return country;
    }

}
