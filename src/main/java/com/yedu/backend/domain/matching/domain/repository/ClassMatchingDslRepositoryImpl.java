package com.yedu.backend.domain.matching.domain.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yedu.backend.domain.matching.domain.entity.ClassMatching;
import com.yedu.backend.domain.matching.domain.entity.constant.MatchingStatus;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yedu.backend.domain.matching.domain.entity.QClassMatching.classMatching;

@RequiredArgsConstructor
@Repository
public class ClassMatchingDslRepositoryImpl implements ClassMatchingDslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ClassMatching> allByApplicationForm(ApplicationForm applicationForm) {
        return queryFactory.selectFrom(classMatching)
                .where(classMatching.applicationForm.eq(applicationForm))
                .orderBy(statusOrderSpecifier())
                .fetch();
    }

    private OrderSpecifier<Integer> statusOrderSpecifier() {
        return new CaseBuilder()
                .when(classMatching.matchStatus.eq(MatchingStatus.수락)).then(1)
                .when(classMatching.matchStatus.eq(MatchingStatus.거절)).then(2)
                .when(classMatching.matchStatus.eq(MatchingStatus.대기)).then(3)
                .when(classMatching.matchStatus.eq(MatchingStatus.전송)).then(4)
                .otherwise(Integer.MAX_VALUE)
                .asc();
    }
}
