package com.yedu.common.event.bizppurio;

public record ParentCompleteTalkNotifyEvent(String nickName, String parentPhoneNumber, Integer parentRoundNumber, String reviewContent, String homework) {

}
