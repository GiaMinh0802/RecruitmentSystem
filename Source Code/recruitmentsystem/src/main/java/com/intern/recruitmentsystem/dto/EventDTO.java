package com.fpt.recruitmentsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Integer id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate createAt = LocalDate.now();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String location;
    private String description;
    private String status;
    private String rule;
    private String benefit;
    private RecruiterDTO recruiter;
    private Set<CollectedCandidateFeaturesDTO> eventCollectedCandidate;
}
