package com.mastercard.mcbs.pbrc.offline.batch.repository;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository
public class OfflineRequestRepo {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, Object> executeOfflineRequest() {
		String sqlQuery = " SELECT * FROM bcx_owner.offline_requests u WHERE u.stat_cd = 'REQUEST_RECEIVED' ORDER BY  u.lst_updt_ts ASC FETCH FIRST ROW ONLY";
		return jdbcTemplate.queryForMap(sqlQuery);
	}
}
