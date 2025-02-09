package com.bharat.freelance.service;

import com.bharat.freelance.entity.Address;
import com.bharat.freelance.exception.AddressNotFoundException;
import java.util.List;

public interface AddressService {
        List<Address> getAllAddresses() throws RuntimeException;
        String addAddress(Address address) throws RuntimeException;
        Address updateAddress(Long id, Address address) throws AddressNotFoundException, RuntimeException;
        String deleteAddress(Long id) throws AddressNotFoundException, RuntimeException;
}
