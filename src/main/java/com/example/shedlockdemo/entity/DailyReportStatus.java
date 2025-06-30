package com.example.shedlockdemo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_report_status")
public class DailyReportStatus {

    @Id
    @Column(name = "business_date")
    private LocalDate businessDate;

    @Column(name = "status_flag")
    private String statusFlag;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor
    public DailyReportStatus() {}

    // Constructor with all fields
    public DailyReportStatus(LocalDate businessDate, String statusFlag, LocalDateTime updatedAt) {
        this.businessDate = businessDate;
        this.statusFlag = statusFlag;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public LocalDate getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(LocalDate businessDate) {
        this.businessDate = businessDate;
    }

    public String getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DailyReportStatus{" +
                "businessDate=" + businessDate +
                ", statusFlag='" + statusFlag + '\'' +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 