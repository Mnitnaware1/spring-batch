package com.mastercard.mcbs.pbrc.offline.batch.serviceimpl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mastercard.mcbs.pbrc.offline.batch.model.CountryRegion;
import com.mastercard.mcbs.pbrc.offline.batch.repository.CountryRepository;
import com.mastercard.mcbs.pbrc.offline.batch.service.CountryService;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public Map<String, Object> getCountryRegion() {
        List<CountryRegion> countryRegions = countryRepository.getCountry();

        return countryRegions.stream()
                .collect(Collectors.toMap(CountryRegion::getAlphaCntryNum, CountryRegion::getAlphaCntryCd));

    }

}
