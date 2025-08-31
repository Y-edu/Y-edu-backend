package com.yedu.bizppurio.support.config;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BizpurrioTemplate {
  YEDU_TUTOR_TEACHER_NOTICE_COMPLETE_TALK_CHANGE(
      "bizp_2025060311502712112401815", "완료톡 변경 공지 v3", Profile.YEDU_TUTOR),
  YEDU_TUTOR_TEACHER_NOTICE_COMPLETE_TALK_CHANGE_WITH_GUIDELINE(
      "bizp_2025060200172312112417167", "완료톡 변경 공지 v2", Profile.YEDU_TUTOR),
  YEDU_TUTOR_TEACHER_WITH_SCHEDULE_COMPLETE_TALK(
      "bizp_2025052520331312112850111", "시간/날짜 등록된 경우 완료톡", Profile.YEDU_TUTOR),
  YEDU_TUTOR_TEACHER_WITH_NO_SCHEDULE_COMPLETE_TALK(
      "bizp_2025052520343512112144966", "시간/날짜 미등록된 경우 완료톡", Profile.YEDU_TUTOR),
  YEDU_TUTOR_TEACHER_WITH_SCHEDULE_COMPLETE_TALK_REMIND(
      "bizp_2025052623071512112734968", "완료톡 리마인드", Profile.YEDU_TUTOR),
  YEDU_TUTOR_TEACHER_WITH_SCHEDULE_COMPLETE_TALK_REMIND_WEEKLY(
        "bizp_2025081800301332533108566", "완료톡 주간 리마인드 v1", Profile.YEDU_TUTOR),
  YEDU_TUTOR_TEACHER_WITH_SCHEDULE_COMPLETE_TALK_REMIND_MONTHLY(
        "bizp_2025081800232232533180630", "완료톡 월정산 리마인드 v1", Profile.YEDU_TUTOR),

  YEDU_APPLY_AGREE(
      "bizp_2025022410400114555103928", "[선생님 등록 3] 약관 동의 (ver.2)", Profile.YEDU_APPLY),
  YEDU_APPLY_PHOTO_HURRY(
      "bizp_2025020511005224487095577", "[선생님 등록 2-1] 사진 & 영상제출 재촉", Profile.YEDU_APPLY),
  YEDU_APPLY_PHOTO_SUBMIT(
      "bizp_2025022410365784552487332", "[선생님 등록 2] 사진 & 영상 제출 (ver.2)", Profile.YEDU_APPLY),

  YEDU_MATCHING_NOTIFY_CLASS(
      "bizp_2025042823440544482981452", "[선생님 과외 신청1] 과외 알림 ver.5", Profile.YEDU_MATCHING),
  YEDU_MATCHING_ACCEPT_CASE(
      "bizp_2025020522265092092681184", "[선생님 과외 신청 2] 신청한 경우", Profile.YEDU_MATCHING),
  YEDU_MATCHING_REFUSE_CASE(
      "bizp_2025020522104292092487279", "[선생님 과외 신청 3] 거절한 경우", Profile.YEDU_MATCHING),
  YEDU_MATCHING_REFUSE_CASE_NOW(
      "bizp_2025031216154647794533661",
      "[선생님 과외 신청 5] 아래 이유로 거절한 경우 - 지금은 수업이 불가해요",
      Profile.YEDU_MATCHING),
  YEDU_MATCHING_REFUSE_CASE_DISTRICT(
      "bizp_2025031216144547794841124",
      "[선생님 과외 신청 4] 아래 이유로 거절한 경우 - 가능한 지역이 아니에요",
      Profile.YEDU_MATCHING),
  YEDU_MATCHING_CHANNEL(
      "bizp_2025022410452884552252565", "[선생님 등록 완료1] 매칭 알림톡 채널 (ver.2)", Profile.YEDU_MATCHING),
  YEDU_MATCHING_TEACHER_SETTING(
      "bizp_2025050715145844482923258", "가능시간 등록 요청 v2", Profile.YEDU_MATCHING),
  YEDU_MATCHING_TEACHER_CLASS_REMIND(
      "bizp_2025040501073700717769661", "[선생님 매칭 완료 3]상담 결과 제출 리마인드", Profile.YEDU_MATCHING),
  YEDU_MATCHING_CLASS_GUIDE(
      "bizp_2025040501113425399074394", "[선생님 매칭 완료 2] 활동 규정 안내", Profile.YEDU_MATCHING),
  YEDU_MATCHING_TEACHER_CLASS_NOTIFY_INFO(
      "bizp_2025042823474694076276726", "[선생님 매칭 완료 1] 과외 매칭 성사 결과 안내 v5", Profile.YEDU_MATCHING),
  YEDU_MATCHING_TEACHER_SCHEDULE(
      "bizp_2025042823360644482249131", "[선생님 매칭 완료 1] 전화번호 교환 v3", Profile.YEDU_MATCHING),

  YEDU_OFFICIAL_RECOMMEND_GUIDE(
      "bizp_2025020611533792092427877", "[학부모 상담 신청 5] 추천 후 안내", Profile.YEDU_OFFICIAL),
  YEDU_OFFICIAL_RECOMMEND_TEACHER(
      "bizp_2025042823340844482117873", "[학부모 상담 신청 4] 선생님 추천 v3", Profile.YEDU_OFFICIAL),
  YEDU_OFFICIAL_NOTIFY_CALLING(
      "bizp_2025020512374424487954938", "[학부모 상담신청 2] 전화 상담 예고", Profile.YEDU_OFFICIAL),
  YEDU_OFFICIAL_PARENTS_EXCHANGE(
      "bizp_2025040422403825399870684", "[학부모 매칭 완료 1] 전화번호 교환", Profile.YEDU_OFFICIAL),
  YEDU_OFFICIAL_PARENTS_CLASS_NOTICE(
      "bizp_2025040422415400717451722", "[학부모 매칭 완료 2] 수업 규정 안내", Profile.YEDU_OFFICIAL),
  YEDU_OFFICIAL_PARENTS_CLASS_INFO(
      "bizp_2025040422464800717675248", "[학부모 매칭 완료 3] 전화 상담 내용 안내", Profile.YEDU_OFFICIAL),
  YEDU_OFFICIAL_PAY_NOTIFICATION(
      "bizp_2025050119355644482892135", "[학부모 매칭 완료 1] 계좌 입금 요청 v2", Profile.YEDU_OFFICIAL),
  YEDU_OFFICIAL_PARENT_COMPLETE_TALK_NOTIFY(
      "bizp_2025081800160232533510346", "학부모 과외 리뷰 v1", Profile.YEDU_OFFICIAL),
  YEDU_TUTOR_TEACHER_CLASS_PAUSE(
      "bizp_2025081800192320034810359", "선생님 과외 중단 안내 v1", Profile.YEDU_TUTOR),
  YEDU_TUTOR_TEACHER_CLASS_RESUME(
      "bizp_2025081800215420034451357", "선생님 과외 재개 안내 v1", Profile.YEDU_TUTOR)
  ;

  private final String code;

  private final String desc;

  private final Profile senderProfile;

  public static BizpurrioTemplate of(String code) {
    return Arrays.stream(values()).filter(it -> it.code.equals(code)).findFirst().get();
  }

  public boolean hasSameCode(String code) {
    return this.code.equals(code);
  }

  @RequiredArgsConstructor
  @Getter
  public enum Profile {
    YEDU_TUTOR("Y-Edu 선생님 지원"),
    YEDU_APPLY("Y-Edu 선생님 등록"),
    YEDU_MATCHING("Y-Edu 매칭 알림톡"),
    YEDU_OFFICIAL("Y-Edu");

    private final String decs;
  }
}
