package com.yedu.backend.domain.parents.application.mapper;

import com.yedu.backend.domain.parents.application.dto.res.ApplicationFormTimeTableResponse;
import com.yedu.backend.domain.parents.domain.entity.ApplicationFormAvailable;
import com.yedu.backend.domain.teacher.domain.entity.constant.Day;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

@UtilityClass
public class ApplicationFormAvailableMapper {

    public static ApplicationFormTimeTableResponse map(List<ApplicationFormAvailable> applicationFormAvailables) {
        if (CollectionUtils.isEmpty(applicationFormAvailables)) {
            return ApplicationFormTimeTableResponse.empty();
        }
        Map<Day, List<LocalTime>> groupedByDay = applicationFormAvailables.stream()
            .collect(Collectors.groupingBy(
                ApplicationFormAvailable::getDay,
                Collectors.mapping(ApplicationFormAvailable::getAvailableTime, Collectors.toList())
            ));

        List<ApplicationFormTimeTableResponse.Time> times = groupedByDay.entrySet().stream()
            .map(entry -> new ApplicationFormTimeTableResponse.Time(entry.getKey(), entry.getValue()))
            .toList();

        return ApplicationFormTimeTableResponse.builder()
            .applicationFormId(applicationFormAvailables.get(0).getApplicationFormId())
            .times(times)
            .build();
    }
}
