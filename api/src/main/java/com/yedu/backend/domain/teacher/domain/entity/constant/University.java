package com.yedu.backend.domain.teacher.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum University {
  // 서울 고려 연세 서강 한양 성균관
  서울대("서울대학교"),
  고려대("고려대학교 서울캠퍼스"),
  연세대("연세대학교 신촌/국제캠퍼스"),
  서강대("서강대학교"),
  한양대("한양대학교 서울캠퍼스"),
  성균관대("성균관대학교");

  private final String description;

  public static boolean checkEtc(String univ) {
    for (University university : values()) {
      if (univ.equals(university.description)) return false;
    }
    return true;
  }
}
