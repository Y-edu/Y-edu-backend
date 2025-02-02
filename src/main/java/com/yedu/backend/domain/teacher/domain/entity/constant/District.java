package com.yedu.backend.domain.teacher.domain.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Getter
@Slf4j
public enum District {
    온라인("온라인"),
    강남구("강남구"),
    강동구("강동구"),
    강북구("강북구"),
    강서구("강서구"),
    관악구("관악구"),
    구로구("구로구"),
    광진구("광진구"),
    금천구("금천구"),
    노원구("노원구"),
    도봉구("도봉구"),
    동대문구("동대문구"),
    동작구("동작구"),
    마포구("마포구"),
    서대문구("서대문구"),
    서초구("서초구"),
    성동구("성동구"),
    성북구("성북구"),
    송파구("송파구"),
    양천구("양천구"),
    영등포구("영등포구"),
    용산구("용산구"),
    은평구("은평구"),
    종로구("종로구"),
    중구("중구"),
    중랑구("중랑구"),
    안양("경기도 안양"),
    용인("경기도 용인"),
    분당("분당"),
    고양("경기도 고양"),
    인천("인천"),
    경기지역("그 외 경기지역"),
    ERROR("선택에 없는 항목");

    private final String description;

    public static District fromString(String teachingDistrict){
        for (District district : values()) {
            if (teachingDistrict.equals(district.description))
                return district;
        }
        log.error("District 처리 예외 발생");
        return ERROR;
    }
}
