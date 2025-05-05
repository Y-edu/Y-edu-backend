package com.yedu.api.admin.domain.service;

import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import org.springframework.stereotype.Service;

@Service
public class AdminUpdateService {
  public void updateKakaoName(Parents parents, String kakaoName) {
    parents.updateKakaoName(kakaoName);
  }

  public void updateProceedStatus(ApplicationForm applicationForm) {
    applicationForm.updateProceedStatus();
  }

  public void updateTeacherIssue(Teacher teacher, String issue) {
    teacher.updateIssue(issue);
  }

  public void updateTeacherVideo(Teacher teacher, String video) {
    teacher.updateVideo(video);
  }

  public void updateClassMatchingSend(ClassMatching classMatching) {
    classMatching.updateSend();
  }

  public void updateAlertCount(Teacher teacher) {
    teacher.updateMessageCount();
  }
}
