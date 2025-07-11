package com.yedu.api.domain.teacher.application.dto.req;

import java.util.Set;

public record CompleteTalkNoticeRequest(
    Set<Long> teacherIds
) {}
