package com.bharat.freelance.controllerTest;

import com.bharat.freelance.controller.AddressController;
import com.bharat.freelance.entity.Address;
import com.bharat.freelance.exception.AddressNotFoundException;
import com.bharat.freelance.service.AddressService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unittest")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    @Test
    void testAddAddress() {
        Address address = new Address();
        address.setId(1L);
        when(addressService.addAddress(any(Address.class))).thenReturn("Address saved successfully with ID: 1");
        ResponseEntity<String> response = addressController.addAddress(address);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Address saved successfully with ID: 1", response.getBody());
    }

    @Test
    void testAddAddress_Error() {
        Address address = new Address();
        when(addressService.addAddress(any(Address.class))).thenThrow(new RuntimeException("Error saving address"));
        Exception exception = assertThrows(RuntimeException.class, () -> addressController.addAddress(address));
        assertEquals("Error saving address", exception.getMessage());
    }

    @Test
    void testDeleteAddress_NotFound() {
        doThrow(new AddressNotFoundException("Address not found")).when(addressService).deleteAddress(1L);
        Exception exception = assertThrows(AddressNotFoundException.class, () -> addressController.deleteAddress(1L));
        assertEquals("Address not found", exception.getMessage());
    }

    @Test
    void testDeleteAddress_Success() {
        when(addressService.deleteAddress(1L)).thenReturn("Address deleted successfully");
        ResponseEntity<String> response = addressController.deleteAddress(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllAddresses() {
        when(addressService.getAllAddresses()).thenReturn(List.of(new Address()));
        ResponseEntity<List<Address>> response = addressController.getAllAddresses();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testUpdateAddress_NotFound() {
        when(addressService.updateAddress(eq(1L), any(Address.class)))
                .thenThrow(new AddressNotFoundException("Address not found with ID: 1"));
        Exception exception = assertThrows(AddressNotFoundException.class, () -> addressController.updateAddress(1L, new Address()));
        assertEquals("Address not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateAddress_Success() {
        Address newAddress = new Address();
        newAddress.setFirstLine("Updated Line");
        when(addressService.updateAddress(eq(1L), any(Address.class))).thenReturn(newAddress);
        ResponseEntity<Address> response = addressController.updateAddress(1L, newAddress);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Line", response.getBody().getFirstLine());
    }

    @Test
    void testUpdateAddress_Error() {
        Address address = new Address();
        when(addressService.updateAddress(eq(1L), any(Address.class))).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> addressController.updateAddress(1L, address));
        assertEquals("Database error", exception.getMessage());
    }
}
