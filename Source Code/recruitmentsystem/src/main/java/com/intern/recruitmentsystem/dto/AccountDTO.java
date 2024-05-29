package com.fpt.recruitmentsystem.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
	private Integer id;
	private String email;
	@JsonManagedReference
	private RoleDTO role;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private Integer sex;
	private String linkAvt;
	private Date birthday;
	private AddressDTO address;
	private Boolean isActive;
	

}

