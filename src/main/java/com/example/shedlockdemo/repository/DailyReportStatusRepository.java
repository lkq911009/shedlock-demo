package com.example.shedlockdemo.repository;

import com.example.shedlockdemo.entity.DailyReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyReportStatusRepository extends JpaRepository<DailyReportStatus, LocalDate> {

    @Query("SELECT d FROM DailyReportStatus d WHERE d.businessDate = :businessDate")
    Optional<DailyReportStatus> findByBusinessDate(@Param("businessDate") LocalDate businessDate);

    @Query("SELECT COUNT(d) > 0 FROM DailyReportStatus d WHERE d.businessDate = :businessDate AND d.statusFlag = 'EOD'")
    boolean existsByBusinessDateAndStatusFlagEOD(@Param("businessDate") LocalDate businessDate);
} 