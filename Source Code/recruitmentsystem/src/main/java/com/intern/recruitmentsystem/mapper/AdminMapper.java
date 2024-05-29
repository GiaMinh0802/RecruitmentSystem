package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Account;
import com.fpt.recruitmentsystem.model.Admin;
import com.fpt.recruitmentsystem.dto.AdminDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminMapper {
    private final ModelMapper modelMapper;
    private final AddressMapper addressMapper;

    public AdminDTO mapToDTO(Admin admin) {
        AdminDTO adminDTO = modelMapper.map(admin, AdminDTO.class);
        adminDTO.setEmail(admin.getAccount().getEmail()); // Set the email from the Account entity
        if (admin.getAddress() != null)
            adminDTO.setAddress(addressMapper.mapToDTO(admin.getAddress()));
        return adminDTO;
    }

    public Admin mapToEntity(AdminDTO adminDTO) {
        Admin admin = modelMapper.map(adminDTO, Admin.class);
        // Set the Account and Address entities from the corresponding DTOs
        admin.setAccount(Account.builder().email(adminDTO.getEmail()).build());
        if (adminDTO.getAddress() != null)
            admin.setAddress(addressMapper.mapToEntity(adminDTO.getAddress()));
        return admin;
    }
}
