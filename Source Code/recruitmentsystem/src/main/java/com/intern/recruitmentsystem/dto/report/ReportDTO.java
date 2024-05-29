package com.fpt.recruitmentsystem.dto.report;

import com.fpt.recruitmentsystem.dto.RecruiterDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ReportDTO {
    private Integer id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createAt = LocalDate.now();

    private String description;
    private String summary;

    private RecruiterDTO recruiter;
}
