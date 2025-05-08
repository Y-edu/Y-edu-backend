package com.yedu.bizppurio.support.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BizpurrioTemplate {
  YEDU_TUTOR_INTRODUCE_FINISH_TALK(
      "bizp_2025040523400000717272288", "[선생님 매칭 완료 4] 완료톡 안내", Profile.YEDU_TUTOR),
  YEDU_TUTOR_INTRODUCE_WRITE_FINISH_TALK(
      "bizp_2025040523425900717346483", "[선생님 매칭 완료 5] 완료톡 안내", Profile.YEDU_TUTOR),

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
      "bizp_2025050119355644482892135", "[학부모 매칭 완료 1] 계좌 입금 요청 v2", Profile.YEDU_OFFICIAL);

  private final String code;

  private final String desc;

  private final Profile senderProfile;

  public enum Profile {
    YEDU_TUTOR,
    YEDU_APPLY,
    YEDU_MATCHING,
    YEDU_OFFICIAL
  }
}
