package com.yedu.api.domain.matching.domain.repository;

import static com.yedu.api.domain.matching.domain.entity.QClassSession.classSession;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yedu.api.domain.matching.domain.entity.ClassManagement;
import com.yedu.api.domain.matching.domain.entity.ClassSession;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ClassSessionRepositoryImpl implements CustomClassSessionRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<ClassSession> findByClassManagementAndSessionDateBetween(
      ClassManagement classManagement, LocalDate startDate, LocalDate endDate, Pageable pageable) {
    LocalDate now = LocalDate.now();
    List<ClassSession> results =
        queryFactory
            .selectFrom(classSession)
            .where(searchCondition(classManagement, null, now, startDate, endDate))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .fetch();

    Long total =
        queryFactory
            .select(classSession.count())
            .from(classSession)
            .where(searchCondition(classManagement, null, now, startDate, endDate))
            .fetchOne();

    return new PageImpl<>(results, pageable, total != null ? total : 0);
  }

  @Override
  public Page<ClassSession> findByClassManagementAndSessionDateBetweenAndCompleted(
      ClassManagement classManagement,
      LocalDate startDate,
      LocalDate endDate,
      boolean completed,
      Pageable pageable) {
    LocalDate now = LocalDate.now();
    List<ClassSession> results =
        queryFactory
            .selectFrom(classSession)
            .where(searchCondition(classManagement, completed, now, startDate, endDate))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifiers(pageable.getSort()))
            .fetch();

    Long total =
        queryFactory
            .select(classSession.count())
            .from(classSession)
            .where(searchCondition(classManagement, completed, now, startDate, endDate))
            .fetchOne();

    return new PageImpl<>(results, pageable, total != null ? total : 0);
  }

  private Predicate searchCondition(
      ClassManagement classManagement,
      Boolean completed,
      LocalDate now,
      LocalDate startDate,
      LocalDate endDate) {
    BooleanExpression predicate =
        classSession
            .classManagement
            .eq(classManagement)
            .and(
                classSession
                    .sessionDate
                    .goe(now)
                    .and(classSession.sessionDate.between(startDate, endDate))
                    .or(
                        classSession
                            .sessionDate
                            .before(now)
                            .and(classSession.sessionDate.between(startDate, endDate))
                            .and(classSession.cancel.isFalse()))
                    .or(classSession.sessionDate.between(now.minusDays(14), endDate)))
            .and(
                classSession
                    .classManagement
                    .classMatching
                    .matchStatus
                    .ne(MatchingStatus.일시중단)
                    .and(
                        classSession.classManagement.classMatching.matchStatus.ne(
                            MatchingStatus.중단))
                    .or(
                        (classSession
                                .classManagement
                                .classMatching
                                .matchStatus
                                .eq(MatchingStatus.일시중단)
                                .or(
                                    classSession.classManagement.classMatching.matchStatus.eq(
                                        MatchingStatus.중단)))
                            .and(
                                classSession.sessionDate.loe(
                                    Expressions.dateTemplate(
                                        LocalDate.class,
                                        "date({0})",
                                        classSession.classManagement.classMatching.pausedAt)))));

    if (completed == null) {
      return predicate;
    }

    return predicate.and(classSession.completed.eq(completed));
  }

  private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
    PathBuilder<ClassSession> pathBuilder = new PathBuilder<>(ClassSession.class, "classSession");

    return sort.stream()
        .map(
            order ->
                new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.getComparable(order.getProperty(), Comparable.class)))
        .toArray(OrderSpecifier[]::new);
  }
}
