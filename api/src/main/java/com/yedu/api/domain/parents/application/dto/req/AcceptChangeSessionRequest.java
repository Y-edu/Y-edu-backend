package com.yedu.api.domain.parents.application.dto.req;

import com.yedu.api.domain.parents.domain.entity.constant.Gender;
import com.yedu.api.domain.parents.domain.entity.constant.Online;
import com.yedu.api.domain.parents.domain.entity.constant.SessionChangeType;
import com.yedu.api.domain.parents.domain.vo.DayTime;
import com.yedu.common.type.ClassType;
import java.util.List;

public record AcceptChangeSessionRequest(
    Long sessionId,
    SessionChangeType type
) {

}
