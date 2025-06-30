package com.example.shedlockdemo.controller;

import com.example.shedlockdemo.entity.DailyReportStatus;
import com.example.shedlockdemo.service.EODService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/eod")
public class EODController {

    @Autowired
    private EODService eodService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getEODStatus() {
        Map<String, Object> response = new HashMap<>();
        
        // Get current business date in EST
        ZoneId estZone = ZoneId.of("America/New_York");
        ZonedDateTime nowInEST = ZonedDateTime.now(estZone);
        LocalDate currentBusinessDate = nowInEST.toLocalDate();
        
        // Check if EOD is marked for today
        boolean isEODMarked = eodService.isEODMarkedForToday();
        DailyReportStatus todayStatus = eodService.getTodayStatus();
        
        response.put("businessDate", currentBusinessDate.toString());
        response.put("isEODMarked", isEODMarked);
        response.put("currentTimeEST", nowInEST.toString());
        
        if (todayStatus != null) {
            response.put("statusFlag", todayStatus.getStatusFlag());
            response.put("updatedAt", todayStatus.getUpdatedAt());
        } else {
            response.put("statusFlag", null);
            response.put("updatedAt", null);
        }
        
        return ResponseEntity.ok(response);
    }
} 