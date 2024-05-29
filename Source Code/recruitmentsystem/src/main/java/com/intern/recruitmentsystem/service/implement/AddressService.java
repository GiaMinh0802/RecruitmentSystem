package com.fpt.recruitmentsystem.service.implement;

import org.springframework.stereotype.Service;

import com.fpt.recruitmentsystem.model.Address;
import com.fpt.recruitmentsystem.service.IAddressService;
import com.fpt.recruitmentsystem.dto.AddressDTO;
import com.fpt.recruitmentsystem.mapper.AddressMapper;
import com.fpt.recruitmentsystem.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Address find(AddressDTO addressDTO) {
        return addressRepository.findByCountryAndCityAndStreet(addressDTO.getCountry(), 
                addressDTO.getCity(), addressDTO.getStreet());        
    }

    public Address findOrInsert(AddressDTO addressDTO) {
        Address existing = find(addressDTO);
        if (existing != null) {
            return existing;
        }

        Address newAddress = addressMapper.mapToEntity(addressDTO);
        newAddress.setId(null);
        return addressRepository.save(newAddress);
    }
}
