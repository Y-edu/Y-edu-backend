package com.yedu.backend.domain.matching.application.usecase;

import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleConfirmRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleMatchingRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRefuseRequest;
import com.yedu.backend.domain.matching.application.dto.req.ClassScheduleRetrieveRequest;
import com.yedu.backend.domain.matching.application.dto.res.ClassScheduleRetrieveResponse;
import com.yedu.backend.domain.matching.domain.entity.ClassManagement;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.service.ClassManagementCommandService;
import com.yedu.backend.domain.matching.domain.service.ClassManagementKeyStorage;
import com.yedu.backend.domain.matching.domain.service.ClassManagementQueryService;
import com.yedu.backend.global.discord.DiscordWebhookUseCase;
import com.yedu.backend.global.event.publisher.BizppurioEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.yedu.backend.global.event.mapper.EventMapper.*;


@RequiredArgsConstructor
@Component
@Slf4j
public class ClassScheduleMatchingUseCase {

  private final ClassManagementCommandService managementCommandService;

  private final ClassManagementQueryService managementQueryService;

  private final ClassManagementKeyStorage keyStorage;

  private final DiscordWebhookUseCase discordWebhookUseCase;

  private final BizppurioEventPublisher bizppurioEventPublisher;

  public String schedule(ClassScheduleMatchingRequest request) {
    ClassManagement classManagement = managementCommandService.schedule(request);
    String key = keyStorage.storeAndGet(classManagement.getClassManagementId());

    bizppurioEventPublisher.publishMatchingEvent(
            mapToMatchingParentsEvent(classManagement),
            mapToTeacherExchangeEvent(classManagement)
    );
    return key;
  }

  public void refuse(ClassScheduleRefuseRequest request) {
    keyStorage.getAndExpire(request.classScheduleManagementId(),
            key -> {
              ClassMatching classMatching = managementCommandService.delete(request, key);
              discordWebhookUseCase.sendScheduleCancel(classMatching.getTeacher(), classMatching.getApplicationForm(), request.refuseReason());
            }
    );
  }

  public void confirm(ClassScheduleConfirmRequest request) {
    keyStorage.getAndExpire(request.classScheduleManagementId(), key -> {
              ClassManagement classManagement = managementCommandService.confirm(request, key);
              bizppurioEventPublisher.publishMatchingConfirmEvent(
                      mapToParentsClassInfoEvent(classManagement), //todo mapper완성 필요
                      mapToMatchingConfirmTeacherEvent(classManagement)
              ); //학부모 수업 정보, 공유 선생님 규정 안내, 완료톡 안내 전송
            }
    );
  }

  public ClassScheduleRetrieveResponse retrieve(ClassScheduleRetrieveRequest request) {
    Long id = keyStorage.get(request.classScheduleManagementId());

    return managementQueryService.query(request, id)
            .map(ClassScheduleRetrieveResponse::of)
            .orElse(ClassScheduleRetrieveResponse.empty());
  }
}
