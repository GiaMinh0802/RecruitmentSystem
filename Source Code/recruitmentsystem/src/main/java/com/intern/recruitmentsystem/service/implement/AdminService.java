package com.fpt.recruitmentsystem.service.implement;

import com.fpt.recruitmentsystem.model.Address;
import com.fpt.recruitmentsystem.model.Admin;
import com.fpt.recruitmentsystem.dto.AddressDTO;
import com.fpt.recruitmentsystem.dto.AdminDTO;
import com.fpt.recruitmentsystem.mapper.AdminMapper;
import com.fpt.recruitmentsystem.repository.AdminRepository;
import com.fpt.recruitmentsystem.service.IAccountService;
import com.fpt.recruitmentsystem.service.IAddressService;
import com.fpt.recruitmentsystem.service.IAdminService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {
    private final IAddressService addressService;
    private final IAccountService accountService;
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminDTO update(AdminDTO adminDTO, String authorization) {
        Admin existingAdmin = accountService.getAdminByAuthorizationHeader(authorization);

		// Copy properties from adminDTO to existingAdmin EXCEPT for id
		BeanUtils.copyProperties(adminDTO, existingAdmin, "id");

        AddressDTO adminAddressdDto = adminDTO.getAddress();
        Address newAdminAddress = (adminAddressdDto != null) ? addressService.findOrInsert(adminAddressdDto) : null;

        existingAdmin.setAddress(newAdminAddress);
        Admin updatedAdmin = adminRepository.save(existingAdmin);

        return adminMapper.mapToDTO(updatedAdmin);
    }

    public AdminDTO getAdmin(String authorizationHeader) {
        Admin admin = accountService.getAdminByAuthorizationHeader(authorizationHeader);
        return adminMapper.mapToDTO(admin);
    }
}
