package com.fpt.recruitmentsystem.dto;

import lombok.Data;

import java.sql.Date;

@Data

public class CollectedCandidateFeaturesDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private String email;

    private Integer sex;

    private Date birthday;
}
