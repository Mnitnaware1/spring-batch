package com.mastercard.mcbs.pbrc.offline.batch.processor;

import java.util.Map;
import org.springframework.batch.item.ItemProcessor;

public class HeadersProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {

	@Override
	public Map<String, Object> process(Map<String, Object> item) throws Exception {
		return item;
	}
}