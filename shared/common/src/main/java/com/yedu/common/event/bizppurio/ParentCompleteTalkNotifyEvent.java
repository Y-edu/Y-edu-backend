package com.yedu.common.event.bizppurio;

import java.time.LocalDate;

public record ParentCompleteTalkNotifyEvent(LocalDate sessionDate, String nickName, String parentPhoneNumber, Integer parentRoundNumber, String reviewContent, String homework) {

}
