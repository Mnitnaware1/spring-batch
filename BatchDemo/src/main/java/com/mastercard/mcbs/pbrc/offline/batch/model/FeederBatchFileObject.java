package com.mastercard.mcbs.pbrc.offline.batch.model;

import lombok.Data;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class FeederBatchFileObject implements SqlParameterSource {

    private Map<String, String> properties = new LinkedHashMap<>();

    public FeederBatchFileObject() {
	}
    
    public FeederBatchFileObject(FieldSet set) {
        Arrays.asList(set.getNames()).stream()
                .forEach(field -> setProperty(field, set.readString(field)));
    }

    @Override
    public boolean hasValue(String feederColumn) {
        return properties.get(feederColumn.toUpperCase()) == null ? Boolean.FALSE : Boolean.TRUE;
    }

    @Override
    public Object getValue(String feederColumn) {
        return properties.get(feederColumn.toUpperCase());
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, String value) {
        properties.put(key, value);
    }
}
