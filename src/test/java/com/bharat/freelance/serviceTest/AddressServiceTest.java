package com.bharat.freelance.serviceTest;

import com.bharat.freelance.entity.Address;
import com.bharat.freelance.exception.AddressNotFoundException;
import com.bharat.freelance.repository.AddressRepository;
import com.bharat.freelance.service.impl.AddressServiceImpl;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Tag("unittest")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    void testAddAddress() {
        Address address = new Address();
        address.setId(1L);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        String result = addressService.addAddress(address);
        assertEquals("Address saved successfully with ID: 1", result);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testAddAddress_Error() {
        Address address = new Address();
        when(addressRepository.save(any(Address.class))).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> addressService.addAddress(address));
        assertEquals("Error saving address", exception.getMessage());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressRepository.existsById(1L)).thenReturn(false);
        Exception exception = assertThrows(AddressNotFoundException.class, () -> addressService.deleteAddress(1L));
        assertEquals("Address not found with ID: 1", exception.getMessage());
        verify(addressRepository, times(1)).existsById(1L);
    }

    @Test
    void testDeleteAddress_Success() {
        when(addressRepository.existsById(1L)).thenReturn(true);
        doNothing().when(addressRepository).deleteById(1L);
        assertDoesNotThrow(() -> addressService.deleteAddress(1L));
        verify(addressRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllAddresses() {
        when(addressRepository.findAll()).thenReturn(List.of(new Address()));
        List<Address> addresses = addressService.getAllAddresses();
        assertFalse(addresses.isEmpty());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void testUpdateAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(AddressNotFoundException.class, () -> addressService.updateAddress(1L, new Address()));
        assertEquals("Address not found with ID: 1", exception.getMessage());
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateAddress_Success() {
        Address existingAddress = new Address();
        existingAddress.setId(1L);
        Address newAddress = new Address();
        newAddress.setFirstLine("Updated Line");
        when(addressRepository.findById(1L)).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(newAddress);
        Address updatedAddress = addressService.updateAddress(1L, newAddress);
        assertNotNull(updatedAddress);
        assertEquals("Updated Line", updatedAddress.getFirstLine());
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testUpdateAddress_Error() {
        Address address = new Address();
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> addressService.updateAddress(1L, address));
        assertEquals("Error updating address", exception.getMessage());
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testGetAllAddresses_Error() {
        when(addressRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> addressService.getAllAddresses());
        assertEquals("Error retrieving addresses", exception.getMessage());
        verify(addressRepository, times(1)).findAll();
    }

}
