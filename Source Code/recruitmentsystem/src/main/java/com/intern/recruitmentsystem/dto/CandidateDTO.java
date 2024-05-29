package com.fpt.recruitmentsystem.dto;

import java.sql.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO {
	private Integer id;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Integer sex;
    private Date birthday;
    private String linkAvt;
    private Set<BlacklistDTO> blacklists;
    private String email;
    private AddressDTO address;
}
