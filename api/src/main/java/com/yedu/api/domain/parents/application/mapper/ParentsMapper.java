package com.yedu.api.domain.parents.application.mapper;

import com.yedu.api.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Goal;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.teacher.domain.entity.constant.District;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParentsMapper {
  public static Parents mapToParents(ApplicationFormRequest request) {
    return Parents.builder().phoneNumber(request.phoneNumber()).build();
  }

  public static ApplicationForm mapToApplicationForm(
      Parents parents, ApplicationFormRequest request) {
    // 가격 계산식 = 4주 기준 분 * 600

    char total = (char) (parents.getCount() + 96);
    int classCount = getClassCount(request.classCount().trim());
    log.info("classCount : {}", classCount);
    int classTime = getClassTime(request.classTime().trim());
    log.info("classTime : {}", classTime);
    int pay = classTime * classCount * 4 * 600;
    log.info("pay : {}", pay);
    return ApplicationForm.builder()
        .applicationFormId(
            District.fromString(request.district())
                + String.valueOf(parents.getParentsId())
                + total)
        .parents(parents)
        .age(request.age())
        .online(request.online())
        .district(District.fromString(request.district()))
        .dong(request.dong())
        .wantedSubject(request.wantedSubject())
        .favoriteStyle(request.favoriteStyle())
        .favoriteGender(request.favoriteGender())
        .classCount(request.classCount())
        .classTime(request.classTime())
        .source(request.source())
        .pay(pay)
        .build();
  }

  private static int getClassTime(String time) {
    if (time.equals("50분")) return 50;
    if (time.equals("60분")) return 60;
    if (time.equals("75분")) return 75;
    if (time.equals("100분")) return 100;
    if (time.equals("120분")) return 120;
    if (time.equals("150분")) return 150;
    if (time.equals("200분")) return 200;
    return 0;
  }

  public static int getClassCount(String count) {
    if (count.equals("주 1회")) return 1;
    else if (count.equals("주 2회")) return 2;
    else if (count.equals("주 3회")) return 3;
    else if (count.equals("주 4회")) return 4;
    if (count.equals("주 5회")) return 5;
    return 0;
  }

  public static Goal mapToGoal(ApplicationForm applicationForm, String classGoal) {
    return Goal.builder().applicationForm(applicationForm).classGoal(classGoal).build();
  }
}
