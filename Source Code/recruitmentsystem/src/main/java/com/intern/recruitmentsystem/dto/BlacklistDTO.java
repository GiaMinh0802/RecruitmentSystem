package com.fpt.recruitmentsystem.dto;

import java.sql.Date;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Future;
import lombok.Data;

@Data
public class BlacklistDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Start date must be in the past")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "End date must be a future date")
    private Date endDate;

    private String reason;
    private Integer candidateId;
}
