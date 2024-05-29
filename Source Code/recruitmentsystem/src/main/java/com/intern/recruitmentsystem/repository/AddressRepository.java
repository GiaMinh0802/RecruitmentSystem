package com.fpt.recruitmentsystem.repository;

import com.fpt.recruitmentsystem.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByCountryAndCityAndStreet(String country, String city, String street);
}
