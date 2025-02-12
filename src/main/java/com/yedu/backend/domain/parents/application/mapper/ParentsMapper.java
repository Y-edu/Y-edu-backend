package com.yedu.backend.domain.parents.application.mapper;

import com.yedu.backend.domain.parents.application.dto.req.ApplicationFormRequest;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.Goal;
import com.yedu.backend.domain.parents.domain.entity.Parents;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;

public class ParentsMapper {
    public static Parents mapToParents(ApplicationFormRequest request) {
        return Parents.builder()
                .phoneNumber(request.phoneNumber())
                .build();
    }

    public static ApplicationForm mapToApplicationForm(Parents parents, ApplicationFormRequest request) {
        // 가격 계산식 = 4주 기준 분 * 600

        char total = (char) (parents.getCount() + 96);
        int classCount = getClassCount(request.classCount());
        int classTime = getClassTime(request.classTime());
        return ApplicationForm.builder()
                .applicationFormId(District.fromString(request.district()) + String.valueOf(parents.getParentsId()) + total)
                .parents(parents)
                .age(request.age())
                .online(request.online())
                .district(District.fromString(request.district()))
                .dong(request.dong())
                .wantedSubject(request.wantedSubject())
                .favoriteStyle(request.favoriteStyle())
                .favoriteGender(request.favoriteGender())
                .wantTime(request.wantTime())
                .classCount(request.classCount())
                .classTime(request.classTime())
                .source(request.source())
                .pay(classTime*classCount*4*600)
                .build();
    }

    private static int getClassTime(String time) {
        int classTime = 0;
        if (time.equals("50분"))
            classTime = 50;
        if (time.equals("60분"))
            classTime = 60;
        if (time.equals("75분"))
            classTime = 75;
        if (time.equals("100분"))
            classTime = 100;
        if (time.equals("120분"))
            classTime = 120;
        return classTime;
    }

    private static int getClassCount(String count) {
        int classCount = 0;
        if (count.equals("주 1회"))
            classCount = 1;
        else if (count.equals("주 2회"))
            classCount = 2;
        else if (count.equals("주 3회"))
            classCount = 3;
        else if (count.equals("주 4회"))
            classCount = 4;
        return classCount;
    }

    public static Goal mapToGoal(ApplicationForm applicationForm, String classGoal) {
        return Goal.builder()
                .applicationForm(applicationForm)
                .classGoal(classGoal)
                .build();
    }
}
