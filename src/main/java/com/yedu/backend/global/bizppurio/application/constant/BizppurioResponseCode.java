package com.yedu.backend.global.bizppurio.application.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum BizppurioResponseCode {
    DELIVERED(7000, "전달"),
    INVALID_KAKAO_FORMAT(7101, "카카오 형식 오류"),
    INVALID_SENDER_KEY(7103, "Sender key (발신프로필키) 유효하지 않음"),
    SENDER_KEY_NOT_FOUND(7105, "Sender key (발신프로필키) 존재하지 않음"),
    DELETED_SENDER_KEY(7106, "삭제된 Sender key (발신프로필키)"),
    BLOCKED_SENDER_KEY(7107, "차단 상태 Sender key (발신프로필키)"),
    BLOCKED_KAKAO_CHANNEL(7108, "차단 상태 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)"),
    CLOSED_KAKAO_CHANNEL(7109, "닫힌 상태 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)"),
    DELETED_KAKAO_CHANNEL(7110, "삭제된 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)"),
    PENDING_DELETE_KAKAO_CHANNEL(7111, "삭제 대기 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)"),
    INVALID_BUSINESS_NUMBER(7112, "유효하지 않은 사업자번호"),
    MESSAGE_BLOCKED_KAKAO_CHANNEL(7125, "메시지 차단 상태의 카카오톡 채널 (카카오톡 채널 운영툴에서 확인)"),
    NOT_A_FRIEND(7203, "친구톡 전송 시 친구 대상 아님"),
    TEMPLATE_MISMATCH(7204, "템플릿 불일치"),
    OTHER_ERROR(7300, "기타 에러"),
    UNCERTAIN_SUCCESS(7305, "성공 불확실(30일 이내 수신 가능)"),
    KAKAO_SYSTEM_ERROR(7306, "카카오 시스템 오류"),
    INVALID_PHONE_NUMBER(7308, "전화번호 오류"),
    MESSAGE_NOT_FOUND(7311, "메시지가 존재하지 않음"),
    MESSAGE_LENGTH_EXCEEDED(7314, "메시지 길이 초과"),
    TEMPLATE_NOT_FOUND(7315, "템플릿 없음"),
    MESSAGE_CANNOT_BE_SENT(7318, "메시지를 전송할 수 없음"),
    RESTRICTED_SEND_TIME(7322, "메시지 발송 불가 시간"),
    MESSAGE_GROUP_NOT_FOUND(7323, "메시지 그룹 정보를 찾을 수 없음"),
    NO_RETRANSMISSION_MESSAGE(7324, "재전송 메시지 존재하지 않음"),
    VARIABLE_LENGTH_EXCEEDED(7325, "변수 글자수 제한 초과"),
    BUTTON_TEXT_LENGTH_EXCEEDED(7326, "상담/봇 전환 버튼 글자수 제한 초과"),
    BUTTON_TEMPLATE_MISMATCH(7327, "버튼/바로 연결 내용과 템플릿 불일치"),
    EMPHASIS_TITLE_MISMATCH(7328, "메시지 강조 표기 타이틀과 템플릿 불일치"),
    EMPHASIS_TITLE_LENGTH_EXCEEDED(7329, "메시지 강조 표기 타이틀 길이 제한 초과 (50 자)"),
    EMPHASIS_TYPE_MISMATCH(7330, "메시지 타입과 템플릿 강조유형이 일치하지 않음"),
    HEADER_MISMATCH(7331, "헤더가 템플릿과 일치하지 않음"),
    HEADER_LENGTH_EXCEEDED(7332, "헤더 길이 제한 초과(16 자)"),
    ITEM_HIGHLIGHT_MISMATCH(7333, "아이템 하이라이트가 템플릿과 일치하지 않음"),
    ITEM_HIGHLIGHT_TITLE_LENGTH_EXCEEDED(7334, "아이템 하이라이트 타이틀 길이 제한 초과"),
    ITEM_HIGHLIGHT_DESC_LENGTH_EXCEEDED(7335, "아이템 하이라이트 디스크립션 길이 제한 초과"),
    ITEM_LIST_MISMATCH(7336, "아이템 리스트가 템플릿과 일치하지 않음"),
    ITEM_LIST_DESC_LENGTH_EXCEEDED(7337, "아이템 리스트의 아이템의 디스크립션 길이 제한 초과(23 자)"),
    ITEM_SUMMARY_MISMATCH(7338, "아이템 요약정보가 템플릿과 일치하지 않음"),
    ITEM_SUMMARY_DESC_LENGTH_EXCEEDED(7339, "아이템 요약정보의 디스크립션 길이 제한 초과(14 자)"),
    ITEM_SUMMARY_INVALID_CHARACTER(7340, "아이템 요약정보의 디스크립션에 허용되지 않은 문자 포함"),
    WIDE_ITEM_LIST_COUNT_MISMATCH(7341, "와이드 아이템 리스트 갯수, 최소 최대 갯수 불일치"),
    MAIN_LINK_MISMATCH(7342, "대표링크가 템플릿과 일치하지 않음"),
    CAROUSEL_ITEM_COUNT_MISMATCH(7351, "캐러셀 아이템 리스트 갯수, 최소 최대 갯수 불일치"),
    CAROUSEL_MESSAGE_LENGTH_EXCEEDED(7352, "캐러셀 아이템 메시지 길이 OVER"),
    TIMEOUT(7421, "타임아웃"),
    DUPLICATE_SEND_LIMIT(7521, "중복발신제한"),
    INVALID_MESSAGE(2000, "메시지가 유효하지 않음"),
    UNREGISTERED_IP(3000, "비즈 뿌리오 계정에 접속 허용 아이피가 등록되어있지 않음"),
    INVALID_BASIC_AUTH(3001, "인증 토큰 발급 호출 시, Basic Authentication 정보가 유효하지 않음"),
    INVALID_TOKEN(3002, "토큰이 유효하지 않음"),
    INVALID_IP(3003, "아이피가 유효하지 않음"),
    INVALID_ACCOUNT(3004, "계정이 유효하지 않음"),
    INVALID_BEARER_AUTH(3005, "인증 정보가 유효하지 않음 (bearer)"),
    ACCOUNT_NOT_FOUND(3006, "비즈뿌리오 계정이 존재하지 않음"),
    INVALID_ACCOUNT_PASSWORD(3007, "비즈뿌리오 계정의 암호가 유효하지 않음"),
    CONNECTION_LIMIT_EXCEEDED(3008, "비즈뿌리오에 허용된 접속 수를 초과함"),
    ACCOUNT_SUSPENDED(3009, "비즈뿌리오 계정이 중지 상태임"),
    IP_MISMATCH(3010, "비즈뿌리오 계정에 등록된 접속 허용 IP와 일치하지 않음"),
    UNKNOWN_BIZ_ERROR(3011, "비즈뿌리오 내에서 알 수 없는 오류가 발생됨"),
    MESSAGE_NOT_EXIST(3012, "비즈뿌리오에 존재하지 않은 메시지 (예: 보관 주기 35일이 지난 메시지)"),
    MESSAGE_NOT_COMPLETED(3013, "완료 처리 되지 않은 메시지 (예: 통신사로부터 결과 미 수신)"),
    REQUEST_FAILURE(5000, "발송 결과 재 요청 실패"),
    URI_NOT_FOUND(5001, "요청한 URI 리소스가 존재하지 않음"),
    UNKNOWN_ERROR(9000, "알 수 없는 오류 발생"),
    UNKNOWN_ERROR_502(5003, "알 수 없는 오류 발생"),
    TOO_MANY_CONNECTIONS(5004, "너무 많은 커넥션"),
    UNKNOWN_ERROR_504(5005, "알 수 없는 오류 발생"),
    PREPAID_BALANCE_LACK(9070, "선불 잔액 부족 (API 응답이 아닌 리포트로 전달)");

    private final int code;
    private final String message;

    // 코드로 Enum 찾기
    public static Optional<BizppurioResponseCode> findByCode(int code) {
        return Arrays.stream(values())
                .filter(e -> e.code == code)
                .findFirst();
    }
}
