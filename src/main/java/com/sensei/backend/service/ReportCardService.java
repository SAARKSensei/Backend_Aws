package com.sensei.backend.service;

import com.sensei.backend.dto.report.MasterReportCardResponse;

import java.util.UUID;

public interface ReportCardService {
    MasterReportCardResponse generateMasterReportCard(UUID childId, int page, int size);
}
