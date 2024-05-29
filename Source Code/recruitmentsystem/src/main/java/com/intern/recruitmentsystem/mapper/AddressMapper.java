package com.fpt.recruitmentsystem.mapper;

import com.fpt.recruitmentsystem.model.Address;
import com.fpt.recruitmentsystem.dto.AddressDTO;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressMapper {
    private final ModelMapper modelMapper;
    public AddressDTO mapToDTO(Address address) {
        return modelMapper.map(address, AddressDTO.class);
    }
    public Address mapToEntity(AddressDTO addressDTO) {
        return modelMapper.map(addressDTO, Address.class);
    }
}
