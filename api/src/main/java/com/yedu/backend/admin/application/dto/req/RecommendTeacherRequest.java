package com.yedu.backend.admin.application.dto.req;

import java.util.List;

public record RecommendTeacherRequest(List<Long> classMatchingIds) {}
