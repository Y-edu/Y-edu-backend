package com.yedu.backend.domain.matching.domain.vo;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseRate {

  private static final int NOT_RESPONSE_HOUR = 24;

  private final Long teacherId;

  private final Integer responseTime;

  private final Integer total;

  public static Map<Long, ResponseRate> calculate(List<ClassMatching> classMatchings) {
    return classMatchings.stream()
        .collect(Collectors.groupingBy(classMatching -> classMatching.getTeacher().getTeacherId()))
        .entrySet()
        .stream()
        .map(ResponseRate::from)
        .collect(Collectors.toMap(ResponseRate::getTeacherId, Function.identity()));
  }

  private static ResponseRate from(Map.Entry<Long, List<ClassMatching>> entry) {
    long teacherId = entry.getKey();
    List<ClassMatching> classMatchings = entry.getValue();

    int total = classMatchings.size();
    int responseCount = (int) classMatchings.stream()
        .filter(respondCondition())
        .count();

    return new ResponseRate(teacherId, responseCount, total);
  }

  private static Predicate<ClassMatching> respondCondition() {
    return classMatching -> classMatching.getMatchStatus() != MatchingStatus.대기 && classMatching.getUpdatedAt().isBefore(classMatching.getCreatedAt().plusHours(NOT_RESPONSE_HOUR));

  }

}
