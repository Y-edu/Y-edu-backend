package com.yedu.api.domain.parents.domain.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ApplicationFormTest {

  @Nested
  @DisplayName("maxRoundNumber()는")
  class Describe_maxRoundNumber {

    @Test
    @DisplayName("applicationFormId가 T-16이면 무조건 6을 반환한다")
    void it_returns_6_when_applicationFormId_is_T16() {
      // given
      ApplicationForm form = ApplicationForm.builder()
          .applicationFormId("T-16")
          .classCount("주 3회")
          .build();

      // when
      Integer result = form.maxRoundNumber();

      // then
      assertThat(result).isEqualTo(6);
    }

    @ParameterizedTest(name = "{index} => classCount={0}, expected={1}")
    @CsvSource({
        "주 4회, 16",
        "주 3회, 12",
        "5회, 20",
        "7, 28"
    })
    @DisplayName("applicationFormId가 T-16이 아닐 경우 classCount의 숫자 * 4를 반환한다")
    void it_calculates_based_on_classCount(String classCount, int expected) {
      // given
      ApplicationForm form = ApplicationForm.builder()
          .applicationFormId("강남구12")
          .classCount(classCount)
          .build();

      // when
      Integer result = form.maxRoundNumber();

      // then
      assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("숫자를 찾을 수 없으면 null을 반환한다")
    void it_returns_null_when_no_number_found() {
      // given
      ApplicationForm form = ApplicationForm.builder()
          .applicationFormId("강남구12")
          .classCount("없음")
          .build();

      // when
      Integer result = form.maxRoundNumber();

      // then
      assertThat(result).isNull();
    }
  }
}
