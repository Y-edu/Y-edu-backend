package com.yedu.backend.domain.matching.domain.vo;

import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseRate {

  public static final int RESPONSE_HOUR = 12;

  private final Long teacherId;

  private final Integer accept;

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
        .filter(ClassMatching::responseComplete)
        .count();

    return new ResponseRate(teacherId, responseCount, total);
  }

}
