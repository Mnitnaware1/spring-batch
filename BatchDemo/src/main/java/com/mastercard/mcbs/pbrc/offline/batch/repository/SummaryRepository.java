package com.mastercard.mcbs.pbrc.offline.batch.repository;

import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.AS;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.CHARGE_DETAIL;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.COMMA;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.ELEMENT_DETAIL_NOT_FOUND_MSG;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.RECORD_NOT_FOUND;
import static com.mastercard.mcbs.pbrc.offline.batch.constant.ApplicationConstant.SELECT_CLAUSE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import com.mastercard.mcbs.pbrc.offline.batch.exception.ResourceNotFoundException;
import com.mastercard.mcbs.pbrc.offline.batch.model.ElementMappings;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineSearchCriteria;
import com.mastercard.mcbs.pbrc.offline.batch.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SummaryRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
 
    public Map<String, Object> findPageableRecords(OfflineSearchCriteria offlineSearchCriteria, String roleName) throws ResourceNotFoundException {
        log.debug("Summary Details {} for given role are {}", offlineSearchCriteria,roleName);
        return buildPageableQueryString(getElementMappingDetails(roleName), offlineSearchCriteria);
    }
    
    private Map<String, Object> buildPageableQueryString(List<ElementMappings> elementMappingDetails, OfflineSearchCriteria offlineSearchCriteria) {

        log.info("BuildPageableQueryString with data {} and total record per page want '{}'", offlineSearchCriteria);
        Object[] elements = elementMappingDetails.stream().map(ElementMappings :: getDataElementNam).toArray();
        Object[] asFields = elementMappingDetails.stream().map(ElementMappings :: getAsfieldsTxt).toArray();
        String selectQuery = buildSelectQuery(elements, asFields);
        StringBuilder selectClause = new StringBuilder();
        selectClause.append(SELECT_CLAUSE).append(selectQuery).append(" FROM bcx_owner.charge_detail cd WHERE cd.chrg_dt = to_date('")
                .append(offlineSearchCriteria.getInvoiceDate()).append("','MM-DD-YYYY')");
        String whereClause = buildAndClauseString(offlineSearchCriteria);
        String orderClause = " ORDER BY chrg_dt desc";
        log.info("DB call with pageable query string '{}{}{}'", selectClause, whereClause, orderClause);
        
        Map<String, Object> offlineRequestParams = new HashMap<>();
        offlineRequestParams.put("query", selectClause.append(whereClause).append(orderClause).toString());
		offlineRequestParams.put("headers", StringUtil.convertArrayToString(asFields) );
        return offlineRequestParams;
    }
    
    /** This method fetch the element mapping details for given role.
     * @throws ResourceNotFoundException **/
    private List<ElementMappings> getElementMappingDetails(String roleName) throws ResourceNotFoundException {
        log.info("Fetch elementMappingDetails with role {} ", roleName);
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("roleName", roleName);
        StringBuilder sqlQuery = new StringBuilder("SELECT data_element_nam,asfields_txt");
        sqlQuery.append( "  FROM bcx_owner.element_mapping WHERE table_nam=").append(CHARGE_DETAIL)
            .append(" AND role_nam=(:roleName) AND enable_sw='Y'");
        List<ElementMappings> elementMappings = namedParameterJdbcTemplate.query(sqlQuery.toString(), sqlParameterSource,
            new BeanPropertyRowMapper<ElementMappings>(ElementMappings.class));
        if (elementMappings.isEmpty()) {
            log.info("{} : {}/query above.", RECORD_NOT_FOUND, ELEMENT_DETAIL_NOT_FOUND_MSG);
            throw new ResourceNotFoundException(ELEMENT_DETAIL_NOT_FOUND_MSG);
        }
        return elementMappings;
    }
    
    /** This method build select query for element_mapping fields. **/
    private String buildSelectQuery(Object[] elements, Object[] asFields) {
        StringBuilder stringBuilder = new StringBuilder();
        if (elements.length == asFields.length) {
            for (int i = 0; i < asFields.length; i++) {
                Object asField = asFields[i];
                Object detailField = elements[i];
                stringBuilder.append(detailField).append(AS).append("\"" + asField + "\"");
                if (i != asFields.length - 1) {
                    stringBuilder.append(COMMA);
                }
            }
            return stringBuilder.toString();
        }
        return "*";
    }
    
    private String buildAndClauseString(OfflineSearchCriteria offlineSearchCriteria) {
        String andClause = "";

        if (StringUtils.isNotEmpty(offlineSearchCriteria.getBillingEvent())) {
            andClause += " AND cd.bill_event_id ='" + offlineSearchCriteria.getBillingEvent() + "'";
        }
        if (CollectionUtils.isNotEmpty(offlineSearchCriteria.getIca())) {
            andClause += " AND (inv_ica IN ("+offlineSearchCriteria.getIca().toString().replace("[", "").replace("]", "")+") OR activity_ica IN ("+offlineSearchCriteria.getIca().toString().replace("[", "").replace("]", "")+"))";
        }
        if (StringUtils.isNotEmpty(offlineSearchCriteria.getInvoiceNumber())) {
            andClause += " AND cd.inv_num ='" + offlineSearchCriteria.getInvoiceNumber() + "'";
        }
        return andClause;
    }
}
