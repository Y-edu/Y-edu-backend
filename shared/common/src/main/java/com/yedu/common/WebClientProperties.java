package com.yedu.common;

public record WebClientProperties(
    String baseUrl,
    int connectionTimeOut,
    int readTimeOut,
    int writeTimeOut) {
}
