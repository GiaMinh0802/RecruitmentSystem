package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.report.DateTimeDTO;
import com.fpt.recruitmentsystem.dto.report.ReportDataDTO;
import com.fpt.recruitmentsystem.dto.report.ReportDTO;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import java.util.List;

public interface IReportService {
    List<ReportDTO> getListReport();
    ReportDTO addReport(ReportDTO reportDTO);
    ReportDTO updateReport(int id, ReportDTO reportDTO);
    ResponseMessage deleteReport(int id);
    List<ReportDTO> findPaginated(int pageNo, int pageLimit);
    List<ReportDTO> getListByRecruiter(int recruiterId);
    ReportDTO getReportDetails(int id);
    ReportDataDTO reportData(DateTimeDTO dateTimeDTO);
}
