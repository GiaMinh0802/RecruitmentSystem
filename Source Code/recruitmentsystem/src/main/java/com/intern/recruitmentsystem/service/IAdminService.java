package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.dto.AdminDTO;

public interface IAdminService {
    AdminDTO update(AdminDTO newProfile, String authorizationHeader);
    AdminDTO getAdmin(String authorizationHeader);
}
