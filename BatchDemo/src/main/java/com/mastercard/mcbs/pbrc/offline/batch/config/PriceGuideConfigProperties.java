package com.mastercard.mcbs.pbrc.offline.batch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties
public class PriceGuideConfigProperties {

    private  Map<String,String> priceGuide;

}
