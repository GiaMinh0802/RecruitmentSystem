package com.fpt.recruitmentsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fpt.recruitmentsystem.model.Recruiter;
import com.fpt.recruitmentsystem.model.Report;
import com.fpt.recruitmentsystem.dto.RecruiterDTO;
import com.fpt.recruitmentsystem.dto.report.ReportDTO;
import com.fpt.recruitmentsystem.exception.NotFoundException;
import com.fpt.recruitmentsystem.mapper.ReportMapper;
import com.fpt.recruitmentsystem.repository.RecruiterRepository;
import com.fpt.recruitmentsystem.repository.ReportRepository;
import com.fpt.recruitmentsystem.service.implement.ReportService;
import com.fpt.recruitmentsystem.util.ResponseMessage;

class ReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private RecruiterRepository recruiterRepository;

    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetListReport() {
        // Arrange
        List<Report> reports = new ArrayList<>();
        reports.add(Report.builder()
                .id(1)
                .title("Report 1")
                .description("05")
                .summary("03")
                .build());
        reports.add(Report.builder()
                .id(2)
                .title("Report 1")
                .description("05")
                .summary("03")
                .build());
        when(reportRepository.findAll()).thenReturn(reports);

        List<ReportDTO> reportDTOs = new ArrayList<>();
        reportDTOs.add(new ReportDTO());
        reportDTOs.get(0).setId(1);
        reportDTOs.get(0).setTitle("Report 1");
        reportDTOs.get(0).setDescription("05");
        reportDTOs.get(0).setSummary("05");
        reportDTOs.add(new ReportDTO());
        reportDTOs.get(1).setId(2);
        reportDTOs.get(1).setTitle("Report 2");
        reportDTOs.get(1).setDescription("2");
        reportDTOs.get(1).setSummary("2");
        when(reportMapper.mapToDTO(any(Report.class))).thenReturn(reportDTOs.get(0), reportDTOs.get(1));

        // Act
        List<ReportDTO> result = reportService.getListReport();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Report 1", result.get(0).getTitle());
        assertEquals("Report 2", result.get(1).getTitle());

    }

    @Test
    void testGetReportDetails() {
        // Arrange
        int reportId = 1;
        Report report = Report.builder()
                .id(reportId)
                .title("Report 1")
                .description("05")
                .summary("03")
                .build();
        when(reportRepository.findReportById(reportId)).thenReturn(report);

        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(reportId);
        reportDTO.setTitle("Report 1");
        reportDTO.setDescription("01");
        reportDTO.setSummary("01");
        when(reportMapper.mapToDTO(report)).thenReturn(reportDTO);

        // Act
        ReportDTO result = reportService.getReportDetails(reportId);

        // Assert
        assertEquals(reportId, result.getId());
        assertEquals("Report 1", result.getTitle());
        assertEquals("01", result.getDescription());
    }

    @Test
    void testGetListByRecruiter() {
        // Arrange
        int recruiterId = 1;
        Recruiter recruiter = Recruiter.builder()
                .id(recruiterId)
                .firstName("John")
                .build();
        when(recruiterRepository.findRecruiterById(recruiterId)).thenReturn(recruiter);

        List<Report> reportList = new ArrayList<>();
        reportList.add(Report.builder()
                .id(1)
                .title("Report 1")
                .description("05")
                .summary("03")
                .recruiter(recruiter)
                .build());
        reportList.add(Report.builder()
                .id(2)
                .title("Report 2")
                .description("02")
                .summary("02")
                .recruiter(recruiter)
                .build());
        when(reportRepository.findAllReportByRecruiterId(recruiterId)).thenReturn(reportList);

        List<ReportDTO> reportDTOs = new ArrayList<>();
        reportDTOs.add(new ReportDTO());
        reportDTOs.get(0).setId(1);
        reportDTOs.get(0).setTitle("Report 1");
        reportDTOs.get(0).setDescription("01");
        reportDTOs.get(0).setSummary("01");
        reportDTOs.get(0).setRecruiter(new RecruiterDTO());
        reportDTOs.get(0).getRecruiter().setId(recruiterId);
        reportDTOs.get(0).getRecruiter().setFirstName("John");
        reportDTOs.add(new ReportDTO());
        reportDTOs.get(1).setId(2);
        reportDTOs.get(1).setTitle("Report 2");
        reportDTOs.get(1).setDescription("02");
        reportDTOs.get(1).setSummary("02");
        reportDTOs.get(1).setRecruiter(new RecruiterDTO());
        reportDTOs.get(1).getRecruiter().setId(recruiterId);
        reportDTOs.get(1).getRecruiter().setFirstName("John");
        when(reportMapper.mapToDTO(any(Report.class))).thenReturn(reportDTOs.get(0), reportDTOs.get(1));

        // Act
        List<ReportDTO> result = reportService.getListByRecruiter(recruiterId);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Report 1", result.get(0).getTitle());
        assertEquals("Report 2", result.get(1).getTitle());
        assertEquals(recruiterId, result.get(0).getRecruiter().getId());
        assertEquals(recruiterId, result.get(1).getRecruiter().getId());
    }

    @Test
    void testFindPaginated() {
        // Arrange
        int pageNo = 1;
        int pageLimit = 5;
        List<Report> reportList = new ArrayList<>();
        reportList.add(Report.builder()
                .id(1)
                .title("Report 1")
                .description("01")
                .summary("01")
                .build());
        reportList.add(Report.builder()
                .id(2)
                .title("Report 2")
                .description("02")
                .summary("02")
                .build());

        Page<Report> page = new PageImpl<>(reportList);
        Pageable pageable = PageRequest.of(pageNo - 1, pageLimit);

        when(reportRepository.findAll(pageable)).thenReturn(page);

        List<ReportDTO> reportDTOs = new ArrayList<>();
        reportDTOs.add(new ReportDTO());
        reportDTOs.get(0).setId(1);
        reportDTOs.get(0).setTitle("Report 1");
        reportDTOs.get(0).setDescription("01");
        reportDTOs.get(0).setSummary("01");
        reportDTOs.add(new ReportDTO());
        reportDTOs.get(1).setId(2);
        reportDTOs.get(1).setTitle("Report 2");
        reportDTOs.get(1).setDescription("02");
        reportDTOs.get(1).setSummary("02");
        when(reportMapper.mapToDTO(any(Report.class))).thenReturn(reportDTOs.get(0), reportDTOs.get(1));

        // Act
        List<ReportDTO> result = reportService.findPaginated(pageNo, pageLimit);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Report 1", result.get(0).getTitle());
        assertEquals("Report 2", result.get(1).getTitle());
    }

    @Test
    void testAddReport() {
        // Arrange
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setTitle("New Report");
        reportDTO.setDescription("new");
        reportDTO.setSummary("new");
        Report reportToSave = Report.builder()
                .title("Report 1")
                .description("01")
                .summary("01")
                .build();
        Report savedReport = Report.builder()
                .id(1)
                .title("Report N")
                .description("N")
                .summary("N")
                .build();
        when(reportMapper.mapToEntity(reportDTO)).thenReturn(reportToSave);
        when(reportRepository.save(reportToSave)).thenReturn(savedReport);
        ReportDTO savedReportDTO = new ReportDTO();
        savedReportDTO.setId(1);
        savedReportDTO.setTitle("New Report");
        savedReportDTO.setDescription("new");
        savedReportDTO.setSummary("new");
        when(reportMapper.mapToDTO(savedReport)).thenReturn(savedReportDTO);

        // Act
        ReportDTO result = reportService.addReport(reportDTO);

        // Assert
        assertEquals("New Report", result.getTitle());
        assertEquals("new", result.getDescription());
    }

    @Test
    void testUpdateReport() {
        // Arrange
        int reportId = 1;
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(reportId);
        reportDTO.setTitle("Updated Report");
        reportDTO.setDescription("update");
        reportDTO.setSummary("update");
        Report existingReport = Report.builder()
                .id(reportId)
                .title("Old Report")
                .description("old")
                .summary("old")
                .build();
        when(reportRepository.findReportById(reportId)).thenReturn(existingReport);
        Report updatedReport = Report.builder()
                .id(reportId)
                .title("Update report")
                .description("update")
                .summary("update")
                .build();
        when(reportRepository.save(existingReport)).thenReturn(updatedReport);
        when(reportMapper.mapToEntity(reportDTO)).thenReturn(updatedReport);
        when(reportMapper.mapToDTO(updatedReport)).thenReturn(reportDTO);

        // Act
        ReportDTO result = reportService.updateReport(reportId, reportDTO);

        // Assert
        assertEquals("Updated Report", result.getTitle());
        assertEquals("update", result.getDescription());
    }

    @Test
    void testDeleteReport() {
        // Arrange
        int reportId = 1;
        Report reportToDelete = Report.builder()
                .id(reportId)
                .title("Report 1")
                .description("01")
                .summary("01")
                .build();
        when(reportRepository.findReportById(reportId)).thenReturn(reportToDelete);
        doNothing().when(reportRepository).delete(reportToDelete);

        // Act
        ResponseMessage result = reportService.deleteReport(reportId);

        // Assert
        assertEquals("Deleted report with Id: 1", result.getMessage());
    }

    @Test
    void testGetListReport_NoReportsFound() {
        // Arrange
        when(reportRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportService.getListReport());
    }

    @Test
    void testGetReportDetails_ReportNotFound() {
        // Arrange
        int reportId = 1;
        when(reportRepository.findReportById(reportId)).thenReturn(null);
    
        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportService.getReportDetails(reportId));
    }

    @Test
    void testGetListByRecruiter_RecruiterNotFound() {
        // Arrange
        int recruiterId = 1;
        when(recruiterRepository.findRecruiterById(recruiterId)).thenReturn(null);
    
        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportService.getListByRecruiter(recruiterId));
    }

    @Test
    void testFindPaginated_NoReportsFound() {
        // Arrange
        int pageNo = 1;
        int pageLimit = 5;
        Page<Report> page = new PageImpl<>(Collections.emptyList());
        Pageable pageable = PageRequest.of(pageNo - 1, pageLimit);
        when(reportRepository.findAll(pageable)).thenReturn(page);
    
        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportService.findPaginated(pageNo, pageLimit));
    }

    @Test
    void testUpdateReport_ReportNotFound() {
        // Arrange
        int reportId = 1;
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setId(reportId);
        reportDTO.setTitle("Updated Report");
        reportDTO.setDescription("update");
        reportDTO.setSummary("update");
        when(reportRepository.findReportById(reportId)).thenReturn(null);
    
        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportService.updateReport(reportId, reportDTO));
    }

    @Test
    void testDeleteReport_ReportNotFound() {
        // Arrange
        int reportId = 1;
        when(reportRepository.findReportById(reportId)).thenReturn(null);
    
        // Act & Assert
        assertThrows(NotFoundException.class, () -> reportService.deleteReport(reportId));
    }    
}
