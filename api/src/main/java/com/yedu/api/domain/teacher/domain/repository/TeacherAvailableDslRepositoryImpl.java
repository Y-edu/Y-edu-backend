package com.yedu.api.domain.teacher.domain.repository;

import static com.yedu.api.domain.teacher.domain.entity.QTeacherAvailable.teacherAvailable;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yedu.api.domain.teacher.domain.entity.Teacher;
import com.yedu.api.domain.teacher.domain.entity.TeacherAvailable;
import com.yedu.api.domain.teacher.domain.entity.constant.Day;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeacherAvailableDslRepositoryImpl implements TeacherAvailableDslRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<TeacherAvailable> allAvailableByTeacher(Teacher teacher) {
    return queryFactory
        .selectFrom(teacherAvailable)
        .where(teacherAvailable.teacher.eq(teacher))
        .orderBy(statusOrderSpecifier())
        .orderBy(teacherAvailable.availableTime.asc())
        .fetch();
  }

  private OrderSpecifier<Integer> statusOrderSpecifier() {
    return new CaseBuilder()
        .when(teacherAvailable.day.eq(Day.월))
        .then(1)
        .when(teacherAvailable.day.eq(Day.화))
        .then(2)
        .when(teacherAvailable.day.eq(Day.수))
        .then(3)
        .when(teacherAvailable.day.eq(Day.목))
        .then(4)
        .when(teacherAvailable.day.eq(Day.금))
        .then(5)
        .when(teacherAvailable.day.eq(Day.토))
        .then(6)
        .when(teacherAvailable.day.eq(Day.일))
        .then(7)
        .otherwise(Integer.MAX_VALUE)
        .asc();
  }
}
