package com.yedu.backend.domain.teacher.domain.aggregate;

import com.yedu.backend.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.TeacherAvailable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeacherWithAvailable {

  private final Map<Teacher, List<TeacherAvailable>> teachers;

  /**
   * 주 n회차일때, 과외/선생님 가능한 시간 중 n회가 겹치면 가능한 선생님으로 분류
   *
   * @param classCount n회차
   * @param formAvailables 과외 가능한 시간
   * @return 가능한 선생님 리스트
   */
  public List<Teacher> availableTeacher(
      Integer classCount, List<ApplicationFormAvailable> formAvailables) {
    return teachers.entrySet().stream()
        .filter(
            entry -> {
              int matchCount =
                  entry.getValue().stream()
                      .filter(
                          teacherAvailable ->
                              formAvailables.stream().anyMatch(teacherAvailable::isSameTo))
                      .map(TeacherAvailable::getDay)
                      .collect(Collectors.toSet())
                      .size();

              return matchCount >= classCount;
            })
        .map(Map.Entry::getKey)
        .toList();
  }
}
