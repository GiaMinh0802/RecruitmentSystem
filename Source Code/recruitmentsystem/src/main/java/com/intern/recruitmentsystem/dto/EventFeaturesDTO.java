package com.fpt.recruitmentsystem.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;

@Data

public class EventFeaturesDTO {
    private Integer id;

    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createAt = LocalDate.now();

    private Date startDate;

    private Date endDate;

    private String location;
    private String description;
    private String status;
    private String rule;
    private String benefit;
    private RecruiterDTO recruiter;
}
