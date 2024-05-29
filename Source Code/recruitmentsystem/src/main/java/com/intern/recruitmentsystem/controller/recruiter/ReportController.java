package com.fpt.recruitmentsystem.controller.recruiter;

import com.fpt.recruitmentsystem.dto.report.DateTimeDTO;
import com.fpt.recruitmentsystem.dto.report.ReportDataDTO;
import com.fpt.recruitmentsystem.service.IReportService;
import com.fpt.recruitmentsystem.dto.report.ReportDTO;
import com.fpt.recruitmentsystem.util.ResponseMessage;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiters/reports")
@Tag(name = "Recruiter",description = "Recruiter Controller")
@SecurityRequirement(name="bearerAuth")
@RequiredArgsConstructor
public class ReportController {
    private final IReportService reportService;

    @GetMapping
    public ResponseEntity<List<ReportDTO>> getListReport(){
        return new ResponseEntity<>(reportService.getListReport(), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReportDetails(@PathVariable int id){
        return new ResponseEntity<>(reportService.getReportDetails(id), HttpStatus.OK);
    }
    @GetMapping("/individual")
    public ResponseEntity<List<ReportDTO>> getListReportByRecruiter(@RequestParam int recruiterId){
        return new ResponseEntity<>(reportService.getListByRecruiter(recruiterId), HttpStatus.OK);
    }
    @GetMapping("/paging")
    public ResponseEntity<List<ReportDTO>> findPaginated(@RequestParam int page, @RequestParam int limit){
        return new ResponseEntity<>(reportService.findPaginated(page, limit), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReportDTO> addReport(@RequestBody ReportDTO reportDTO){
        return new ResponseEntity<>(reportService.addReport(reportDTO), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ReportDTO> updateReport(@PathVariable int id, @RequestBody ReportDTO reportDTO){
        return new ResponseEntity<>(reportService.updateReport(id, reportDTO), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteReport(@PathVariable int id){
        return new ResponseEntity<>(reportService.deleteReport(id), HttpStatus.OK);
    }
    @GetMapping("/datas")
    public ResponseEntity<ReportDataDTO> getReportData(@RequestBody DateTimeDTO dateTimeDTO){
        return new ResponseEntity<>(reportService.reportData(dateTimeDTO), HttpStatus.OK);
    }

}
