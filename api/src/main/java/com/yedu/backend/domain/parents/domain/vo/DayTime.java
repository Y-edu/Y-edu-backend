package com.yedu.backend.domain.parents.domain.vo;

import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DayTime {

  private final Day day;

  private final List<LocalTime> times;
}
