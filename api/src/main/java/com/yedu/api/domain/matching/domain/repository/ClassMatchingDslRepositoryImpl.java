package com.yedu.api.domain.matching.domain.repository;

import static com.yedu.api.domain.matching.domain.entity.QClassMatching.classMatching;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yedu.api.domain.matching.domain.entity.ClassMatching;
import com.yedu.api.domain.matching.domain.entity.constant.MatchingStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ClassMatchingDslRepositoryImpl implements ClassMatchingDslRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<ClassMatching> allByApplicationForm(String applicationFormId) {
    return queryFactory
        .selectFrom(classMatching)
        .where(classMatching.applicationForm.applicationFormId.eq(applicationFormId))
        .orderBy(statusOrderSpecifier())
        .fetch();
  }

  private OrderSpecifier<Integer> statusOrderSpecifier() {
    return new CaseBuilder()
        .when(classMatching.matchStatus.eq(MatchingStatus.전송))
        .then(1)
        .when(classMatching.matchStatus.eq(MatchingStatus.수락))
        .then(2)
        .when(classMatching.matchStatus.eq(MatchingStatus.거절))
        .then(3)
        .when(classMatching.matchStatus.eq(MatchingStatus.대기))
        .then(4)
        .otherwise(Integer.MAX_VALUE)
        .asc();
  }
}
