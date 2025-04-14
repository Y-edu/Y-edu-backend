package com.yedu.common.webclient;

public record WebClientProperties(
    String baseUrl,
    int connectionTimeOut,
    int readTimeOut,
    int writeTimeOut) {
}
