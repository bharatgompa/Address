package com.bharat.freelance.controller;

import com.bharat.freelance.entity.Address;
import com.bharat.freelance.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses/v1")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @GetMapping("/get")
    public ResponseEntity<List<Address>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAddress(@RequestBody Address address) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(address));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address address) {
        return ResponseEntity.ok(addressService.updateAddress(id, address));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }

}

