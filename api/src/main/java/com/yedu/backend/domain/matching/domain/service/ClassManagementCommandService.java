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


    public ClassManagement schedule(ClassScheduleMatchingRequest request){
        ClassMatching classMatching = classMatchingGetService.getById(request.classMatchingId());
        classMatching.startSchedule();

        return classManagementRepository
            .findByClassMatching_ClassMatchingId(request.classMatchingId())
            .orElseGet(() -> {
                ClassManagement newClassManagement = ClassManagement.builder()
                    .classMatching(classMatching)
                    .build();
                return classManagementRepository.save(newClassManagement);
            });
    }

    public ClassMatching delete(ClassScheduleRefuseRequest request, Long id) {
        ClassManagement classManagement = queryById(id);

        classManagement.refuse(request.refuseReason());

        classManagementRepository.delete(classManagement);

        return classManagement.getClassMatching().initializeProxy();
    }

    public ClassManagement confirm(ClassScheduleConfirmRequest request, Long id) {
        ClassManagement classManagement = findClassManagementWithSchedule(request, id);

        classManagement.confirm(
            request.textBook(),
            request.firstDay().date(),
            new ClassTime(request.firstDay().start())
        );
        return classManagement;
    }

    public void completeRemind(ClassManagement classManagement) {
        classManagement.completeRemind();
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
