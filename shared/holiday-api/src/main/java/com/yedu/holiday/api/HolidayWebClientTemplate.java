package com.yedu.holiday.api;

import com.yedu.holiday.api.dto.res.HolidayResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class HolidayWebClientTemplate{

  private final WebClient holidayWebClient;

  public Mono<HolidayResponse> getHoliday(LocalDate holidayMonth) {
    return holidayWebClient.get()
        .uri(uriBuilder ->
            uriBuilder.queryParam("solYear",holidayMonth.getYear())
                .queryParam("solMonth", String.format("%02d", holidayMonth.getMonthValue()))
                .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<>() {});
  }
}
