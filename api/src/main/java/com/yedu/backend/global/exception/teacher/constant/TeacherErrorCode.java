package com.yedu.backend.global.exception.teacher.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TeacherErrorCode {
  INACTIVE_TEACHER("EX101", "선생님이 활동 상태가 아닙니다 - teacherId : %s"),
  NOTFOUND_BY_ID("EX102", "해당하는 선생님을 찾을 수 없습니다 - teacherId : %s"),
  NOTFOUND_BY_NAME_NICKNAME("EX103", "해당하는 선생님을 찾을 수 없습니다 - name : %s nickName : %s"),
  NOTFOUND_BY_PHONE("EX104", "해당하는 선생님을 찾을 수 없습니다 - phoneNumber : %s"),
  NOTFOUND_ENGLISH("EX105", "선생님이 영어 수업을 진행하지 않습니다 - teacherId : %s"),
  NOTFOUND_MATH("EX106", "선생님이 수학 수업을 진행하지 않습니다 - teacherId : %s"),
  LOGIN_FAIL("EX107", "선생님 로그인에 실패하였습니다 - name : %s phoneNumber : %s");

  private final String code;
  private final String message;
}
