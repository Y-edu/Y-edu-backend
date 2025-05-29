package com.yedu.api.domain.matching.application.dto.res;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationFormResponse {
  private String applicationFormId;
  private Long matchingId;
  private String childAge;
  private Boolean isOnline;
  private String district;
  private String dong;
  private String subject;
  private String favoriteStyle;
  private String favoriteGender;
  private String source;
  private String classCount;
  private Integer pay;
  private List<AvailableTime> availableTimes;
  private Parents parent;
  private Teacher teacher;
  private String matchingStatus;
  private String matchingRefuseReason;
  private ClassManagement classManagement;

  @Builder
  public static class AvailableTime {
    private Long availableId;
    private String day;
    private String time;
  }

  @Builder
  public static class Parents {
    private Long parentId;
    private String kakaoName;
    private String phoneNumber;
    private Boolean marketingAgree;
  }

  @Builder
  public static class Teacher {
    private Long teacherId;
    private String realName;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String university;
    private String major;
    private String highSchool;
    private String birth;
    private String gender;
    private String profileImage;
    private String video;
    private String introduce;
    private String comment;
    private List<String> teachingStyle;
    private List<String> teachingStyleDescription;
    private TeacherEnglish english;
    private TeacherMath math;
  }

  @Builder
  public static class TeacherEnglish {
    private Boolean possible;
    private String experience;
    private String foreignExperience;
    private String teachingStyle;
    private Integer teachingHistory;
  }

  @Builder
  public static class TeacherMath {
    private Boolean possible;
    private String experience;
    private String teachingStyle;
    private Integer teachingHistory;
  }

  @Builder
  public static class ClassManagement {
    private Long classManagementId;
    private String textBook;
    private String firstDay;
    private List<Schedule> schedule;
    private List<Session> sessions;
  }

  @Builder
  public static class Schedule {
    private Long classScheduleId;
    private String day;
    private String start;
    private Integer classMinute;
  }

  @Builder
  public static class Session {
    private Long classSessionId;
    private String date;
    private String start;
    private Integer classMinute;
    private String understanding;
    private Integer homeworkPercentage;
    private Boolean cancel;
    private String cancelReason;
    private Boolean completed;
  }
}
