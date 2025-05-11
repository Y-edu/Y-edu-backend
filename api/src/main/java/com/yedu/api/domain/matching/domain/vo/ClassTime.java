package com.yedu.api.domain.matching.domain.vo;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ClassTime {

  @With private LocalTime start;

  private Integer classMinute;

  public ClassTime(LocalTime start) {
    this.start = start;
  }
}
