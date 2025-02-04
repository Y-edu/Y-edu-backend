package com.yedu.backend.domain.parents.domain.application.mapper;

import com.yedu.backend.domain.parents.domain.application.dto.req.ApplicationFormRequest;
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
        char count = (char) (parents.getCount() + 96);
        return ApplicationForm.builder()
                .applicationFormId(District.fromString(request.district()) + String.valueOf(parents.getParentsId()) + count)
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
                .build();
    }

    public static Goal mapToGoal(ApplicationForm applicationForm, String classGoal) {
        return Goal.builder()
                .applicationForm(applicationForm)
                .classGoal(classGoal)
                .build();
    }
}
