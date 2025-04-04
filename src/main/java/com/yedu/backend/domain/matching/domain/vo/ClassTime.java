package com.yedu.backend.domain.matching.domain.vo;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ClassTime{

  private LocalTime start;

  private Integer classMinute;

}
