package com.example.shedlockdemo.service;

import com.example.shedlockdemo.entity.DailyReportStatus;
import com.example.shedlockdemo.repository.DailyReportStatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class EODService {

    private static final Logger logger = LoggerFactory.getLogger(EODService.class);
    private static final String EOD_STATUS = "EOD";

    @Autowired
    private DailyReportStatusRepository repository;

    @Transactional
    public void markEODForCurrentBusinessDate() {
        LocalDate currentBusinessDate = getCurrentBusinessDate();
        
        logger.info("Starting EOD process for business date: {}", currentBusinessDate);
        
        // Check if EOD is already marked for today (idempotency check)
        if (repository.existsByBusinessDateAndStatusFlagEOD(currentBusinessDate)) {
            logger.info("EOD already marked for business date: {}. Skipping update.", currentBusinessDate);
            return;
        }
        
        // Check if record exists for today
        DailyReportStatus existingRecord = repository.findByBusinessDate(currentBusinessDate)
                .orElse(null);
        
        if (existingRecord != null) {
            // Update existing record
            existingRecord.setStatusFlag(EOD_STATUS);
            existingRecord.setUpdatedAt(LocalDateTime.now());
            repository.save(existingRecord);
            logger.info("Updated existing record with EOD status for business date: {}", currentBusinessDate);
        } else {
            // Create new record
            DailyReportStatus newRecord = new DailyReportStatus(
                    currentBusinessDate,
                    EOD_STATUS,
                    LocalDateTime.now()
            );
            repository.save(newRecord);
            logger.info("Created new record with EOD status for business date: {}", currentBusinessDate);
        }
    }

    public boolean isEODMarkedForToday() {
        LocalDate currentBusinessDate = getCurrentBusinessDate();
        return repository.existsByBusinessDateAndStatusFlagEOD(currentBusinessDate);
    }

    public DailyReportStatus getTodayStatus() {
        LocalDate currentBusinessDate = getCurrentBusinessDate();
        return repository.findByBusinessDate(currentBusinessDate).orElse(null);
    }

    private LocalDate getCurrentBusinessDate() {
        // Get current date in EST timezone
        ZoneId estZone = ZoneId.of("America/New_York");
        ZonedDateTime nowInEST = ZonedDateTime.now(estZone);
        return nowInEST.toLocalDate();
    }
} 