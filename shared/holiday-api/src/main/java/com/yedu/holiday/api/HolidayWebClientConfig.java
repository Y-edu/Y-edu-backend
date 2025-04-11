package com.yedu.holiday.api;

import com.yedu.common.WebClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(HolidayWebClientProperties.class)
class HolidayWebClientConfig {


  private final HolidayWebClientProperties properties;

  @Bean
  public WebClient holidayWebClient() {
    WebClientProperties webClientProperties = properties.webClientProperties();
    HttpClient httpClient =
        HttpClient.create()
            .option(
                ChannelOption.CONNECT_TIMEOUT_MILLIS,
                webClientProperties.connectionTimeOut() * 1000)
            .doOnConnected(
                conn ->
                    conn.addHandlerLast(
                            new ReadTimeoutHandler(webClientProperties.readTimeOut(), TimeUnit.SECONDS)));

    DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(serviceKeyUriBuilderFactory());
    uriBuilderFactory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

    return WebClient.builder()
        .baseUrl(webClientProperties.baseUrl())
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .uriBuilderFactory(uriBuilderFactory)
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(clientCodecConfigurer ->
                clientCodecConfigurer.defaultCodecs()
                    .jaxb2Encoder(new Jaxb2XmlEncoder())
            ).codecs(clientCodecConfigurer ->
                clientCodecConfigurer.defaultCodecs()
                    .jaxb2Decoder(new Jaxb2XmlDecoder())
            )
            .build())
        .build();
  }

  private UriComponentsBuilder serviceKeyUriBuilderFactory() {
    return UriComponentsBuilder.fromHttpUrl(properties.webClientProperties().baseUrl())
        .queryParam("serviceKey", properties.serviceKey());
  }
}
