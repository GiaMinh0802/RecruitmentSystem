package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.dto.report.DateTimeDTO;
import com.fpt.recruitmentsystem.dto.report.ReportDataDTO;
import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.model.Report;
import com.fpt.recruitmentsystem.service.IReportService;
import com.fpt.recruitmentsystem.dto.report.ReportDTO;
import com.fpt.recruitmentsystem.exception.BadRequestException;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.ReportMapper;
import com.fpt.recruitmentsystem.repository.RecruiterRepository;
import com.fpt.recruitmentsystem.repository.ReportRepository;
import com.fpt.recruitmentsystem.util.Message;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;
    private final RecruiterRepository recruiterRepository;

    public List<ReportDTO> getListReport(){
        List<Report> reports = reportRepository.findAll();
        if (reports.isEmpty()){
            throw new NotFoundException(Message.REPORT_NOT_FOUND);
        }
        return reports.stream().
                map(reportMapper::mapToDTO).
                toList();
    }
    public ReportDTO getReportDetails(int id){
        Report report = reportRepository.findReportById(id);
        if (report == null){
            throw new NotFoundException(Message.REPORT_NOT_FOUND);
        }
        return reportMapper.mapToDTO(report);
    }
    public List<ReportDTO> getListByRecruiter(int recruiterId){
        Recruiter recruiter = recruiterRepository.findRecruiterById(recruiterId);
        if(recruiter == null){
            throw new NotFoundException(Message.RECRUITER_NOT_FOUND);
        }
        List<Report> reportList = reportRepository.findAllReportByRecruiterId(recruiterId);
        if(reportList.isEmpty()){
            throw new NotFoundException(Message.REPORT_NOT_FOUND);
        }
        return reportList.stream().
                map(reportMapper::mapToDTO).
                toList();
    }
    public List<ReportDTO> findPaginated(int pageNo, int pageLimit){
        Pageable pageable = PageRequest.of(pageNo-1, pageLimit);
        Page<Report> page = reportRepository.findAll(pageable);
        List<Report> reportList = page.getContent();
        if(reportList.isEmpty()){
            throw new NotFoundException(Message.REPORT_NOT_FOUND);
        }
        return reportList.stream().
                map(reportMapper::mapToDTO).
                toList();
    }

    public ReportDTO addReport(ReportDTO reportDTO){
        try{
            Report reportToEntity = reportMapper.mapToEntity(reportDTO);
            Report reportNew = reportRepository.save(reportToEntity);
            return reportMapper.mapToDTO(reportNew);
        }
        catch (Exception e){
            throw new BadRequestException("Can't create report: " + e.getMessage());
        }
    }

    public ReportDTO updateReport(int id, ReportDTO reportDTO){
        Report reportUpdate = reportRepository.findReportById(id);
        if (reportUpdate == null){
            throw new NotFoundException(Message.REPORT_NOT_FOUND);
        }
        try {
            Report reportToEntity = reportMapper.mapToEntity(reportDTO);
            reportUpdate.setTitle(reportToEntity.getTitle());
            reportUpdate.setDescription(reportToEntity.getDescription());
            reportUpdate.setSummary(reportToEntity.getSummary());
            Report reportNew = reportRepository.save(reportUpdate);
            return reportMapper.mapToDTO(reportNew);
        } catch (Exception e){
            throw new BadRequestException("Can't update report: " + e.getMessage());
        }
    }
    public ResponseMessage deleteReport(int id){
        Report report = reportRepository.findReportById(id);
        if (report == null) {
            throw new NotFoundException(Message.REPORT_NOT_FOUND);
        }
        try {
            reportRepository.delete(report);
            return new ResponseMessage("Deleted report with Id: " + id);

        } catch (Exception e){
            throw new BadRequestException("Can't delete report: " + e.getMessage());
        }
    }

    public ReportDataDTO reportData(DateTimeDTO dateTimeDTO){
        ReportDataDTO reportDataDTO = new ReportDataDTO();
        reportDataDTO.setTotalCandidate(reportRepository.totalCandidate());
        reportDataDTO.setTotalInterviewer(reportRepository.totalInterviewer());
        reportDataDTO.setTotalRecruiter(reportRepository.totalRecruiter());
        reportDataDTO.setTotalCandidateInBlacklist(reportRepository.totalCandidateInBlacklistByDate(dateTimeDTO.getStartDate(), dateTimeDTO.getEndDate()));
        reportDataDTO.setTotalCandidateVacancy(reportRepository.totalCandidateVacancy());
        reportDataDTO.setQuestionByScore(reportRepository.questionByScore());
        reportDataDTO.setCCandidateByEvent(reportRepository.candidateByEvent());
        reportDataDTO.setTotalCandidateByStatus(reportRepository.candidateByStatus());
        reportDataDTO.setInterviewByMonthAndYear(reportRepository.interviewByMonthAndYear(dateTimeDTO.getMonth(), dateTimeDTO.getYear()));
        return reportDataDTO;
    }

}
