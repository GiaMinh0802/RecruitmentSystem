package com.fpt.recruitmentsystem.dto;

import java.sql.Date;

import lombok.Data;
@Data
public class AdminDTO {
	private Integer id;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private Integer sex;
	private String linkAvt;
	private Date birthday;
	private String email;
	private AddressDTO address;
}
