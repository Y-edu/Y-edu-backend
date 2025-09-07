package com.yedu.api.domain.matching.application.usecase;

import com.yedu.api.domain.matching.application.dto.req.MatchingTimeTableRequest;
import com.yedu.api.domain.matching.application.dto.req.MatchingTimeTableRetrieveByTokenRequest;
import com.yedu.api.domain.matching.application.dto.req.MatchingTimeTableRetrieveRequest;
import com.yedu.api.domain.matching.application.dto.res.MatchingTimetableRetrieveResponse;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.MatchingTimetable;
import com.yedu.api.domain.matching.domain.service.MatchingTimetableCommandService;
import com.yedu.api.domain.matching.domain.service.MatchingTimetableQueryService;
import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import com.yedu.cache.support.dto.MatchingTimeTableDto;
import com.yedu.cache.support.storage.KeyStorage;
import com.yedu.common.event.bizppurio.PayNotificationEvent;
import com.yedu.payment.api.PaymentTemplate;
import com.yedu.payment.api.dto.SendBillRequest;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingTimetableUseCase {
  private final MatchingTimetableQueryService matchingTimetableQueryService;
  private final MatchingTimetableCommandService matchingTimetableCommandService;
  private final KeyStorage<MatchingTimeTableDto> matchingTimetableKeyStorage;

  private final PaymentTemplate paymentTemplate;

  @Value("${app.yedu.url}")
  public String serverUrl;

  private final ApplicationEventPublisher eventPublisher;

  public MatchingTimetableRetrieveResponse retrieveMatchingTimetable(
      MatchingTimeTableRetrieveRequest request) {
    List<MatchingTimetable> timetables =
        matchingTimetableQueryService.query(request.classMatchingId());
    SortedMap<Day, List<LocalTime>> sortedTimetable = getDayListSortedMap(timetables);
    return new MatchingTimetableRetrieveResponse(sortedTimetable);
  }

  public MatchingTimetableRetrieveResponse retrieveMatchingTimetable(
      MatchingTimeTableRetrieveByTokenRequest request) {
    MatchingTimeTableDto matchingTimeTableDto =
        matchingTimetableKeyStorage.get(request.classMatchingToken());
    List<MatchingTimetable> timetables =
        matchingTimetableQueryService.query(matchingTimeTableDto.matchingId());
    SortedMap<Day, List<LocalTime>> sortedTimetable = getDayListSortedMap(timetables);
    return new MatchingTimetableRetrieveResponse(sortedTimetable);
  }

  private SortedMap<Day, List<LocalTime>> getDayListSortedMap(List<MatchingTimetable> timetables) {
    SortedMap<Day, List<LocalTime>> availableTimes =
        new TreeMap<>(Comparator.comparingInt(Day::getDayNum));
    Arrays.stream(Day.values()).forEach(day -> availableTimes.put(day, new ArrayList<>()));

    timetables.forEach(
        timetable -> {
          Day day = timetable.getDay();
          List<LocalTime> availables = availableTimes.get(day);
          availables.add(timetable.getTimetableTime());
        });
    return availableTimes;
  }

  public void matchingTimetable(MatchingTimeTableRequest request) {
    MatchingTimeTableDto matchingTimeTableDto =
        matchingTimetableKeyStorage.get(request.classMatchingToken());
    ClassMatching classMatching =
        matchingTimetableCommandService.matchingTimetable(
            matchingTimeTableDto.matchingId(), request.dayTimes());

    ApplicationForm applicationForm = classMatching.getApplicationForm();
    PayNotificationEvent event =
        new PayNotificationEvent(
            applicationForm.getParents().getPhoneNumber(),
            classMatching.getTeacher().getTeacherInfo().getNickName(),
            applicationForm.getPay());

    eventPublisher.publishEvent(event);

    String teacherNickname = classMatching.getTeacher().getTeacherInfo().getNickName();

    SendBillRequest sendBillRequest = new SendBillRequest("학부모",
        applicationForm.getParents().getPhoneNumber(),
        """
        {name} 선생님 수업료
        """
            .replace("{name}", teacherNickname),
        """
        ☑️ {name} 선생님 수업 결제 안내
        
        어머님 안녕하세요. 선생님과 수업 진행을 위한 결제 안내 드립니다.   결제 진행 후, 선생님과 전화 상담이 진행되며, 전화상담으로 교재, 수업시간을 확정 후 수업이 진행됩니다.
       
        문의사항이 있으시다면 언제든 Y-Edu 채널을 통해 문의사항 말씀해주세요.   감사합니다!\s
        """
            .replace("{name}", teacherNickname),
        BigDecimal.valueOf(applicationForm.getPay()),
        serverUrl + "/matchings/"+ classMatching.getClassMatchingId()+ "/pay"
    );

    paymentTemplate.sendBill(sendBillRequest);
  }
}
