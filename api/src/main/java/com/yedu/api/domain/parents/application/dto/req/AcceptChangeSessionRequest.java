package com.yedu.api.domain.parents.application.dto.req;

import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;

public record AcceptChangeSessionRequest(Long sessionId, SessionChangeType type) {}
