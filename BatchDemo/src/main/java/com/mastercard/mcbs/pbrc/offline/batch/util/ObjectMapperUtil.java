package com.mastercard.mcbs.pbrc.offline.batch.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.mcbs.pbrc.offline.batch.model.OfflineSearchCriteria;

public class ObjectMapperUtil {
	
	private ObjectMapperUtil() {
	    throw new IllegalStateException("Utility class");
	  }

	
	public static OfflineSearchCriteria convertObjectToOfflineSearchCriteria(String searchCriteria) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(searchCriteria, OfflineSearchCriteria.class);
	}

}
