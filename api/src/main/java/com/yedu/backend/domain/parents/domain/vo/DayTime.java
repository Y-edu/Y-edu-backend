package com.yedu.backend.domain.parents.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DayTime {

  private final Day day;

  @JsonFormat(pattern = "HH:mm")
  private final List<LocalTime> times;
}
