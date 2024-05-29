package com.fpt.recruitmentsystem.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectedCandidateDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Email
    private String email;

    private Integer sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    private Set<EventFeaturesDTO> events;
}
