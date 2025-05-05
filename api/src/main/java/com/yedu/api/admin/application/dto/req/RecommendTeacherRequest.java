package com.yedu.api.admin.application.dto.req;

import java.util.List;

public record RecommendTeacherRequest(List<Long> classMatchingIds) {}
