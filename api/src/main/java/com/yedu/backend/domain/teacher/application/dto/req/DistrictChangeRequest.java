package com.yedu.backend.domain.teacher.application.dto.req;

import java.util.List;

public record DistrictChangeRequest(String name, String phoneNumber, List<String> districts) {}
