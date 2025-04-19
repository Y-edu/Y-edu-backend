package com.yedu.cache.support.dto;

import com.yedu.common.type.ClassType;

public record TeacherRecommend(
    Long teacherId,
    Long matchingId,
    ClassType classType
) {

}
