package com.yedu.backend.domain.teacher.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Gender;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.yedu.backend.domain.teacher.domain.entity.QTeacher.teacher;
import static com.yedu.backend.domain.teacher.domain.entity.QTeacherDistrict.teacherDistrict;

@Repository
@RequiredArgsConstructor
public class TeacherDslRepositoryImpl implements TeacherDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Teacher> findAllMatchingApplicationForm(ApplicationForm applicationForm) {
        // 지역, 성별, 과목
        District district = applicationForm.getDistrict();
        Gender favoriteGender = applicationForm.getFavoriteGender();
        ClassType subject = applicationForm.getWantedSubject();

        return queryFactory.select(teacher)
                .from(teacher)
                .distinct()
                .leftJoin(teacherDistrict)
                .fetchJoin()
                .on(teacher.eq(teacherDistrict.teacher))
                .where(genderSpecifier(favoriteGender)
                        .and(subjectSpecifier(subject))
                        .and(teacherDistrict.district.eq(district)))
                .fetch();
    }

    private BooleanExpression genderSpecifier(Gender favoriteGender) {
        if (favoriteGender == Gender.상관없음)
            return null;
        return favoriteGender == Gender.남
                ? teacher.teacherInfo.gender.eq(TeacherGender.남)
                : teacher.teacherInfo.gender.eq(TeacherGender.여);
    }

    private BooleanExpression subjectSpecifier(ClassType classType) {
        if (classType == ClassType.영어)
            return teacher.teacherClassInfo.englishPossible.eq(true);
        if (classType == ClassType.수학)
            return teacher.teacherClassInfo.mathPossible.eq(true);
        return null;
    }
}
