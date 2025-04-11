package com.yedu.holiday.api;

import com.yedu.common.WebClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("holiday")
record HolidayWebClientProperties(
    WebClientProperties webClientProperties,
    String serviceKey
) {

}
