package com.yedu.api.domain.parents.domain.repository;

import com.yedu.api.domain.parents.domain.entity.ApplicationForm;
import com.yedu.api.domain.parents.domain.entity.Parents;
import com.yedu.api.domain.parents.domain.entity.constant.Gender;
import com.yedu.api.domain.parents.domain.entity.constant.Level;
import com.yedu.api.domain.parents.domain.entity.constant.Online;
import com.yedu.api.domain.teacher.domain.entity.constant.District;
import com.yedu.common.type.ClassType;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationFormRepository extends JpaRepository<ApplicationForm, String> {
  void deleteAllByParents_PhoneNumber(String phoneNumber);

  Optional<ApplicationForm> findByParentsAndDistrictAndWantedSubjectAndAgeAndClassCountAndCreatedAtAfter(Parents parents, District district, ClassType wantedSubject,
      String age, String classCount, LocalDateTime before);

  @Query("SELECT af FROM ApplicationForm af WHERE " +
         "af.parents = :parents AND " +
         "af.age = :age AND " +
         "af.online = :online AND " +
         "af.district = :district AND " +
         "af.dong = :dong AND " +
         "af.wantedSubject = :wantedSubject AND " +
         "af.level = :level AND " +
         "af.favoriteCondition = :favoriteCondition AND " +
         "af.educationImportance = :educationImportance AND " +
         "af.favoriteStyle = :favoriteStyle AND " +
         "af.favoriteGender = :favoriteGender AND " +
         "af.favoriteDirection = :favoriteDirection AND " +
         "af.wantTime = :wantTime AND " +
         "af.classCount = :classCount AND " +
         "af.classTime = :classTime AND " +
         "af.source = :source AND " +
         "af.proceedStatus = :proceedStatus AND " +
         "af.pay = :pay")
  Optional<ApplicationForm> findByAllFieldsExceptId(
      @Param("parents") Parents parents,
      @Param("age") String age,
      @Param("online") Online online,
      @Param("district") District district,
      @Param("dong") String dong,
      @Param("wantedSubject") ClassType wantedSubject,
      @Param("level") Level level,
      @Param("favoriteCondition") String favoriteCondition,
      @Param("educationImportance") Level educationImportance,
      @Param("favoriteStyle") String favoriteStyle,
      @Param("favoriteGender") Gender favoriteGender,
      @Param("favoriteDirection") String favoriteDirection,
      @Param("wantTime") String wantTime,
      @Param("classCount") String classCount,
      @Param("classTime") String classTime,
      @Param("source") String source,
      @Param("proceedStatus") boolean proceedStatus,
      @Param("pay") int pay);
}
