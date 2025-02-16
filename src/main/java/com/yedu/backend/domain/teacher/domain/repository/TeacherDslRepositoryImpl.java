package com.yedu.backend.domain.teacher.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yedu.backend.admin.application.dto.req.TeacherSearchRequest;
import com.yedu.backend.domain.parents.domain.entity.ApplicationForm;
import com.yedu.backend.domain.parents.domain.entity.constant.ClassType;
import com.yedu.backend.domain.parents.domain.entity.constant.Gender;
import com.yedu.backend.domain.teacher.domain.entity.Teacher;
import com.yedu.backend.domain.teacher.domain.entity.constant.District;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherGender;
import com.yedu.backend.domain.teacher.domain.entity.constant.TeacherStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.querydsl.core.types.dsl.Expressions.TRUE;
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
            return TRUE;
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

    @Override
    public List<Teacher> findAllSearchTeacher(TeacherSearchRequest request) {
        BooleanBuilder builder = searchLikeSpecifier(request);
        builder.and(districtSpecifier(request.districts()))
                .and(subjectSpecifier(request.subjects()))
                .and(universitySpecifier(request.universities()))
                .and(genderSpecifier(request.genders()));

        return queryFactory.select(teacher)
                .from(teacher)
                .distinct()
                .leftJoin(teacherDistrict)
                .fetchJoin()
                .on(teacher.eq(teacherDistrict.teacher))
                .where(builder) // 동적 조건 적용
                .orderBy(
                        statusOrderSpecifier(),
                        universityOrderSpecifier()
                )
                .fetch();
    }

    private BooleanBuilder searchLikeSpecifier(TeacherSearchRequest request) {
        BooleanBuilder builder = new BooleanBuilder();
        if (request.search() != null && !request.search().isEmpty()) {
            builder.and(
                    teacher.teacherInfo.nickName.like("%" + request.search() + "%")
                            .or(teacherDistrict.district.stringValue().like("%" + request.search() + "%"))
                            .or(teacher.teacherInfo.name.like("%" + request.search() + "%"))
                            .or(teacher.status.stringValue().like("%" + request.search() + "%"))
                            .or(teacher.issue.like("%" + request.search() + "%"))
                            .or(teacher.teacherSchoolInfo.major.like("%" + request.search() + "%"))
                            .or(subjectSpecifier(request.search()))
            );
        }
        return builder;
    }

    private BooleanExpression genderSpecifier(List<TeacherGender> genders) {
        if (genders == null || genders.isEmpty()) {
            return null; // where() 절에서 무시됨
        }

        return genders.stream()
                .map(teacher.teacherInfo.gender::eq)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    private BooleanExpression districtSpecifier(List<String> districts) {
        if (emptyCheck(districts)) return null; // where() 절에서 무시됨

        return districts.stream()
                .map(district -> teacherDistrict.district.eq(District.fromString(district)))
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    private BooleanExpression subjectSpecifier(List<ClassType> subjects) {
        if (subjects == null || subjects.isEmpty()) {
            return null;
        }

        return subjects.stream()
                .map(subject -> {
                    if (subject.equals(ClassType.수학))
                        return teacher.teacherClassInfo.mathPossible.isTrue();
                    if (subject.equals(ClassType.영어))
                        return teacher.teacherClassInfo.englishPossible.isTrue();
                    return null;
                })
                .filter(Objects::nonNull)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    private BooleanExpression subjectSpecifier(String search) {
        if (search == null) {
            return null;
        }
        if (search.equals(ClassType.수학.name()))
            return teacher.teacherClassInfo.mathPossible.isTrue();
        if (search.equals(ClassType.영어.name()))
            return teacher.teacherClassInfo.englishPossible.isTrue();
        return null;
    }

    private BooleanExpression universitySpecifier(List<String> universities) {
        if (emptyCheck(universities)) return null;

        return universities.stream()
                .map(university -> {
                    if (university.equals("S"))
                        return teacher.teacherSchoolInfo.university.eq("서울대학교");
                    if (university.equals("K"))
                        return teacher.teacherSchoolInfo.university.eq("고려대학교 서울캠퍼스");
                    if (university.equals("Y"))
                        return teacher.teacherSchoolInfo.university.eq("연세대학교 신촌/국제캠퍼스");
                    if (university.equals("서강"))
                        return teacher.teacherSchoolInfo.university.eq("서강대학교");
                    if (university.equals("성균"))
                        return teacher.teacherSchoolInfo.university.eq("성균관대학교");
                    if (university.equals("한양"))
                        return teacher.teacherSchoolInfo.university.eq("한양대학교 서울캠퍼스");
                    if (university.equals("기타"))
                        return teacher.teacherSchoolInfo.etc.isTrue();
                    return null;
                })
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    private static boolean emptyCheck(List<String> requests) {
        if (requests == null || requests.isEmpty()) {
            return true;
        }
        return false;
    }

    // status를 "활동중", "등록중", "일시정지", "종료" 순으로 정렬하는 OrderSpecifier 생성
    private OrderSpecifier<Integer> statusOrderSpecifier() {
        return new CaseBuilder()
                .when(teacher.status.eq(TeacherStatus.활동중)).then(1)
                .when(teacher.status.eq(TeacherStatus.등록중)).then(2)
                .when(teacher.status.eq(TeacherStatus.일시정지)).then(3)
                .when(teacher.status.eq(TeacherStatus.종료)).then(4)
                .otherwise(Integer.MAX_VALUE)
                .asc();
    }

    // university를 "서울대학교", "연세대학교 신촌/국제캠퍼스", "고려대학교 서울캠퍼스", "서강대학교", "성균관대학교", "한양대학교 서울캠퍼스" 순으로 정렬하는 OrderSpecifier 생성
    private OrderSpecifier<Integer> universityOrderSpecifier() {
        return new CaseBuilder()
                .when(teacher.teacherSchoolInfo.university.eq("서울대학교")).then(1)
                .when(teacher.teacherSchoolInfo.university.eq("연세대학교 신촌/국제캠퍼스")).then(2)
                .when(teacher.teacherSchoolInfo.university.eq("고려대학교 서울캠퍼스")).then(3)
                .when(teacher.teacherSchoolInfo.university.eq("서강대학교")).then(4)
                .when(teacher.teacherSchoolInfo.university.eq("성균관대학교")).then(5)
                .when(teacher.teacherSchoolInfo.university.eq("한양대학교 서울캠퍼스")).then(6)
                .otherwise(Integer.MAX_VALUE) // 그 외의 경우는 가장 뒤로 정렬
                .asc(); // 오름차순 정렬
    }
}