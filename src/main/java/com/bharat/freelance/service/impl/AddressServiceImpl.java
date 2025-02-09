package com.bharat.freelance.service.impl;

import com.bharat.freelance.entity.Address;
import com.bharat.freelance.exception.AddressNotFoundException;
import com.bharat.freelance.repository.AddressRepository;
import com.bharat.freelance.service.AddressService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> getAllAddresses() {
        try {
            return addressRepository.findAll();
        } catch (Exception e) {
            logger.error("Error retrieving addresses", e);
            throw new RuntimeException("Error retrieving addresses", e);
        }
    }

    @Override
    @Transactional
    public String addAddress(Address address) {
        try {
            Address savedAddress = addressRepository.save(address);
            return "Address saved successfully with ID: " + savedAddress.getId();
        } catch (Exception e) {
            logger.error("Error saving address", e);
            throw new RuntimeException("Error saving address", e);
        }
    }

    @Override
    @Transactional
    public Address updateAddress(Long id, Address newAddress) {
        try {
            Optional<Address> optionalAddress = addressRepository.findById(id);
            if (optionalAddress.isPresent()) {
                Address existingAddress = optionalAddress.get();
                Address updatedAddress = Address.builder()
                        .id(existingAddress.getId())
                        .firstLine(newAddress.getFirstLine())
                        .secondLine(newAddress.getSecondLine())
                        .thirdLine(newAddress.getThirdLine())
                        .fourthLine(newAddress.getFourthLine())
                        .fifthLine(newAddress.getFifthLine())
                        .postCode(newAddress.getPostCode())
                        .dateMovedIn(newAddress.getDateMovedIn())
                        .preferNotToSay(newAddress.getPreferNotToSay())
                        .build();
                return addressRepository.save(updatedAddress);
            } else {
                throw new AddressNotFoundException("Address not found with ID: " + id);
            }
        } catch (AddressNotFoundException e) {
            logger.error("Address not found: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating address", e);
            throw new RuntimeException("Error updating address", e);
        }
    }

    @Override
    @Transactional
    public String deleteAddress(Long id) {
        try {
            if (addressRepository.existsById(id)) {
                addressRepository.deleteById(id);
                return "Address deleted successfully";
            } else {
                throw new AddressNotFoundException("Address not found with ID: " + id);
            }
        } catch (AddressNotFoundException e) {
            logger.error("Address not found with given id : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting address", e);
            throw new RuntimeException("Error deleting address", e);
        }
    }
}