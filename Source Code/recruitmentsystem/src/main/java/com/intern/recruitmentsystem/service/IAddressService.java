package com.fpt.recruitmentsystem.service;

import com.fpt.recruitmentsystem.model.Address;
import com.fpt.recruitmentsystem.dto.AddressDTO;

public interface IAddressService {
    Address find(AddressDTO addressDTO);
    Address findOrInsert(AddressDTO addressDTO);
}
