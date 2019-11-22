package com.mastercard.mcbs.pbrc.offline.batch.serviceimpl;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.FROM_COUNTRY_CODE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.TO_COUNTRY_CODE;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mastercard.mcbs.pbrc.offline.batch.exception.ResourceNotFoundException;
import com.mastercard.mcbs.pbrc.offline.batch.model.Country;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineRequest;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineSearchCriteria;
import com.mastercard.mcbs.pbrc.offline.batch.repository.SummaryRepository;
import com.mastercard.mcbs.pbrc.offline.batch.service.SummaryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SummaryServiceImpl implements SummaryService {

	@Autowired
	private SummaryRepository summaryRepository;

	@Autowired
	private Country country;

	/**
	 * .
	 *
	 * @param roleName     .
	 * @param userId       .
	 * @param searchType   .
	 * @param summaryModel .
	 * @return .
	 * @throws ResourceNotFoundException .
	 */
	public Map<String, Object> getSummaryData(OfflineRequest offlineRequest,
			OfflineSearchCriteria offlineSearchCriteria) throws ResourceNotFoundException {
		Optional<Map<String, Object>> summaryResponses = Optional
				.ofNullable(summaryRepository.findPageableRecords(offlineSearchCriteria, offlineRequest.getRlNam()));
		Map<String, Object> offlineRequestSummaryQuery = null;
		if (summaryResponses.isPresent()) {
			log.info("Offline request summary query {}",summaryResponses.get());
			offlineRequestSummaryQuery = summaryResponses.get();
		}
		return offlineRequestSummaryQuery;
	}

	/**
	 * . This code converts numeric country code to alpha numeric code which we get
	 * from country-region table
	 **/
	private void updateNumericCountryCode(List<Map<String, Object>> summaryResponse) {
		summaryResponse.stream().forEach(summaryResponseObject -> {
			Set<String> keySet = summaryResponseObject.keySet();
			for (String key : keySet) {
				if (key.equalsIgnoreCase(TO_COUNTRY_CODE)) {
					updateCountry(summaryResponseObject, TO_COUNTRY_CODE);

				} else if (key.equalsIgnoreCase(FROM_COUNTRY_CODE)) {
					updateCountry(summaryResponseObject, FROM_COUNTRY_CODE);
				}
			}
		});
	}

	private void updateCountry(Map<String, Object> summaryResponseObject, String key) {
		Map<String, Object> countryRegion = country.getCountryMap();
		summaryResponseObject.put(key, countryRegion.get(summaryResponseObject.get(key)));
	}
}