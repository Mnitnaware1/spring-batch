package com.mastercard.mcbs.pbrc.offline.batch.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OfflineRowMapper implements RowMapper<Map<String, Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		Map<String, Object> offlineRequestRowData = new LinkedHashMap<>(columns);
		for (int i = 1; i <= columns; ++i) {
			offlineRequestRowData.put(md.getColumnName(i), rs.getObject(i));
		}
		log.info("Offline request row data {}", offlineRequestRowData);
		return offlineRequestRowData;
	}
}
