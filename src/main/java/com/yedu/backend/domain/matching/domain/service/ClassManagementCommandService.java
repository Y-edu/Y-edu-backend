package com.yedu.backend.domain.matching.domain.service;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.entity.ClassSchedule;
import com.yedu.backend.domain.matching.domain.repository.ClassManagementRepository;
import com.yedu.backend.domain.matching.domain.vo.ClassTime;
import com.yedu.backend.global.exception.matching.ClassManagementNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ClassManagementCommandService {

    private final ClassManagementRepository classManagementRepository;

    private final ClassMatchingGetService classMatchingGetService;


    public Long schedule(ClassScheduleMatchingRequest request){
        ClassMatching classMatching = classMatchingGetService.getById(request.classMatchingId());
        classMatching.schedule();

        ClassManagement classManagement = ClassManagement.builder()
            .classMatching(classMatching)
            .build();

        classManagementRepository.save(classManagement);

        return classManagement.getClassManagementId();
    }

    public void delete(ClassScheduleRefuseRequest request, Long id) {
        ClassManagement classManagement = queryById(id);

        classManagement.refuse(request.refuseReason());

        classManagementRepository.delete(classManagement);
    }

    public void confirm(ClassScheduleConfirmRequest request, Long id) {
        ClassManagement classManagement = findClassManagementWithSchedule(request, id);

        classManagement.updateManagement(
            request.textBook(),
            request.firstDay().date(),
            new ClassTime(request.firstDay().start(), request.firstDay().classMinute())
        );
    }

    private ClassManagement findClassManagementWithSchedule(ClassScheduleConfirmRequest request, Long id) {
        ClassManagement classManagement = queryById(id);

        request.schedules().stream().map(schedule-> ClassSchedule.builder()
                .classManagement(classManagement)
                .day(schedule.day())
                .classTime(new ClassTime(schedule.start(), schedule.classMinute()))
                .build())
            .forEach(classManagement::addSchedule);

        return classManagement;
    }

    private ClassManagement queryById(Long id){
        return classManagementRepository.findById(id)
            .orElseThrow(()-> new ClassManagementNotFoundException("일치하는 매칭 관리 정보를 찾을 수 없습니다"));
    }
}
