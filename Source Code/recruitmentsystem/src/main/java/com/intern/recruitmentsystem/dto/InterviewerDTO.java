package com.fpt.recruitmentsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.sql.Date;

@Data
public class InterviewerDTO {
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private Date birthday;
    private Integer sex;
    private String linkAvt;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer accountId;
    private String email;
    private AddressDTO address;
}
