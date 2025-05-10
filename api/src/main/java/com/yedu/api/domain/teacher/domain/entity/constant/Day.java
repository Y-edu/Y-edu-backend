package com.yedu.api.domain.teacher.domain.entity.constant;

import java.time.LocalDate;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Day {
  월(0),
  화(1),
  수(2),
  목(3),
  금(4),
  토(5),
  일(6);

  private final int dayNum;

  public static Day byInt(int day) {
    return Arrays.stream(values())
        .filter(d -> d.dayNum == day)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid day number: " + day));
  }

  public static Day byDate(LocalDate date) {
    int dayOfWeek = date.getDayOfWeek().getValue();
    return Day.byInt(dayOfWeek - 1);
  }
}
