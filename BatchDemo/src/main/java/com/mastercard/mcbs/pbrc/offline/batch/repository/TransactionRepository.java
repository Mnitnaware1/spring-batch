package com.mastercard.mcbs.pbrc.offline.batch.repository;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.AS;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.AS_QUERY;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.BILLING_EVENT_DETAIL;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.COMMA;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.EVENT_ALIAS;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.EVENT_DETAIL;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.EVENT_DETAIL_TBL;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.IS_RULE_SEQUENCE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.RULE_ALIAS;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.RULE_SEQUENCE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.RULE_SEQUENCE_ALIAS;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.RULE_SEQUENCE_TBL;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.SCHEMA_NAME;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.SELECT_CLAUSE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.SINGLE_QUOTE;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.TRANSACTION_ALIAS;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.TRANSACTION_DETAIL;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.TRANSACTION_DETAIL_TBL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mastercard.mcbs.pbrc.offline.batch.exception.ResourceNotFoundException;
import com.mastercard.mcbs.pbrc.offline.batch.model.ElementMappings;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class TransactionRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * .
	 * 
	 * @param roleName     .
	 * @param feederType   .
	 * @param totalRecords per page.
	 * @param imeTraceId   .
	 * @param pageNumber   .
	 */
	public Map<String, Object> getTransactionDetail(String roleName, String feederType, String imeTraceId)
			throws ResourceNotFoundException {
		log.debug("Transaction Detail request parameters are : {} {}", feederType);
		Stream<List<ElementMappings>> elementMappingResult = Stream.of(
				elementsMapping(roleName, feederType, EVENT_DETAIL_TBL),
				elementsMapping(roleName, feederType, TRANSACTION_DETAIL_TBL),
				elementsMapping(roleName, feederType, RULE_SEQUENCE_TBL));
		// the code can be optimised more and it is to get the headers
		Map<String, Object> offlineRequestParams = new HashMap<>();
		Map<String, Object> generateElementMappingDetails = generateElementMappingDetails(elementMappingResult);
		offlineRequestParams.put("query", buildFinalQuery(feederType, generateElementMappingDetails, imeTraceId));
		offlineRequestParams.put("headers", generateElementMappingDetails.get("offline_request_headers"));
		return offlineRequestParams;
	}

	/**
	 * This method fetch the element details based on the role name.
	 * 
	 * @param roleName   .
	 * @param feederType .
	 * @param tableName  .
	 */
	private List<ElementMappings> elementsMapping(String roleName, String feederType, String tableName) {
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append("SELECT data_element_nam, asfields_txt, table_nam FROM bcx_owner.element_mapping "
				+ "WHERE role_nam = ? AND enable_sw = 'Y' AND table_nam = ")
				.append(buildTableNameUsingFeederType(tableName, feederType));
		log.info("Fetching elementMappingDetails with role {} and feederType", roleName, feederType);
		log.info("Fetching elementMappingDetails with query {}", sqlQuery);
		List<ElementMappings> elementMappings = jdbcTemplate.query(sqlQuery.toString(), new Object[] { roleName },
				new BeanPropertyRowMapper<ElementMappings>(ElementMappings.class));
		List<ElementMappings> transFormElementMappings = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(elementMappings)) {
			elementMappings.stream().forEach(element -> {
				if (element.getTableNam().contains(EVENT_DETAIL)) {
					element.setDataElementNam(EVENT_ALIAS + element.getDataElementNam());
				} else if (element.getTableNam().contains("_TRANSACTION_DETAIL")) {
					element.setDataElementNam(TRANSACTION_ALIAS + element.getDataElementNam());
				} else if (element.getTableNam().contains(RULE_SEQUENCE)) {
					element.setDataElementNam(RULE_ALIAS + element.getDataElementNam());
				}
				transFormElementMappings.add(element);
			});
		}
		return transFormElementMappings;
	}

	/** This method build table name based on the feederType. */
	private String buildTableNameUsingFeederType(String tableName, String feederType) {
		StringBuilder singleQuote = new StringBuilder(SINGLE_QUOTE);
		if (tableName.equals(RULE_SEQUENCE_TBL)) {
			return RULE_SEQUENCE_TBL;
		}
		return singleQuote.append(feederType).append(tableName).toString();
	}

	/**
	 * This method generate AS fields for fields for event,transaction and rule
	 * tables.
	 * 
	 * @param elementMappingListResult .
	 */
	private Map<String, Object> generateElementMappingDetails(Stream<List<ElementMappings>> elementMappingListResult) {
		StringBuilder elementMappingFinalResult = new StringBuilder();
		StringBuilder headers = new StringBuilder();
		Map<String, Object> elementMappingsForTranDetails = new HashMap<>();
		elementMappingListResult.forEach(elementMapResult -> {
			List<Object> detailFields = Arrays
					.asList(elementMapResult.stream().map(ElementMappings::getDataElementNam).toArray());
			List<Object> asFields = Arrays
					.asList(elementMapResult.stream().map(ElementMappings::getAsfieldsTxt).toArray());
			if (elementMapResult.stream().anyMatch(element -> element.getTableNam().equals(RULE_SEQUENCE))) {
				elementMappingsForTranDetails.put(IS_RULE_SEQUENCE, true);
			}

			if (CollectionUtils.isNotEmpty(detailFields) && CollectionUtils.isNotEmpty(asFields)) {
				for (int i = 0; i < asFields.size(); i++) {
					Object asField = asFields.get(i);
					Object detailField = detailFields.get(i);
					elementMappingFinalResult.append(detailField).append(AS).append("\"" + asField + "\"");
					headers.append(asField).append(COMMA);
					if (i < asFields.size() - 1) {
						elementMappingFinalResult.append(COMMA);
					}
				}
			}
			elementMappingFinalResult.append(COMMA);
		});
		elementMappingFinalResult.replace(elementMappingFinalResult.length() - 1, elementMappingFinalResult.length(),
				"");
		headers.replace(headers.length() - 1, headers.length(), "");
		elementMappingsForTranDetails.put("offline_request_headers", headers);
		elementMappingsForTranDetails.put(AS_QUERY, elementMappingFinalResult);
		return elementMappingsForTranDetails;
	}

	/**
	 * . This is the final query to fetch billing detail for the given feeder type
	 *
	 * @param tranDetailAsFields .
	 * @param imeTraceId
	 * @param pageNumber         .
	 * @param totalRecords       .
	 */
	private String buildFinalQuery(String feederType, Map<String, Object> tranDetailAsFields, String imeTraceId) {
		StringBuilder sqlQuery = new StringBuilder();
		sqlQuery.append(SELECT_CLAUSE).append(tranDetailAsFields.get(AS_QUERY)).append(" FROM ")
				.append(buildTableNamesUingFeederType(feederType, tranDetailAsFields))
				.append(" WHERE event.ime_trace_id = transaction.ime_trace_id \r\n")
				.append(" AND event.GUIDE_POINT_PARTY_ID = transaction.GUIDE_POINT_PARTY_ID")
				.append(" AND event.ime_trace_id = '" + imeTraceId + "' AND ")
				.append(buildWhereClause(tranDetailAsFields));

		log.info("DB call with pageable query string '{}'", sqlQuery);
		return sqlQuery.toString();
	}

	private String buildWhereClause(Map<String, Object> tranDetailAsFields) {
		String whereClause = "";
		if (tranDetailAsFields.get(IS_RULE_SEQUENCE).equals(true)) {
			whereClause = "event.rule_seq_num =rule.rule_seq_num AND ROWNUM <=5";
		}
		return whereClause;
	}

	private String buildTableNamesUingFeederType(String feederType, Map<String, Object> tranDetailAsFields) {
		StringBuilder feederTypeStr = new StringBuilder(SCHEMA_NAME + feederType);
		StringBuilder schemaStr = new StringBuilder(SCHEMA_NAME);

		feederTypeStr.append(BILLING_EVENT_DETAIL).append(COMMA).append(schemaStr).append(feederType)
				.append(TRANSACTION_DETAIL).append(COMMA);

		if (tranDetailAsFields.get("isRuleSequence").equals(true)) {
			feederTypeStr.append(schemaStr + RULE_SEQUENCE).append(RULE_SEQUENCE_ALIAS);
		}
		return feederTypeStr.toString();
	}

}