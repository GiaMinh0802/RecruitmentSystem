package com.fpt.recruitmentsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterDTO {
    private Integer id;
    private Date birthday;
    private String firstName;
    private String lastName;
    private String linkAvt;
    private String phoneNumber;
    private Integer sex;
    private String email;
    private AddressDTO address;
}
