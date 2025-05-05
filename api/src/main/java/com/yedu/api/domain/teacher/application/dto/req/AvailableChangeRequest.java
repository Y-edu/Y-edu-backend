package com.yedu.api.domain.teacher.application.dto.req;

import java.util.List;

public record AvailableChangeRequest(
    String name, String phoneNumber, List<List<String>> available) {}
