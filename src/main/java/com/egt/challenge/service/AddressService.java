package com.egt.challenge.service;

import com.egt.challenge.error.BadRequestException;
import com.egt.challenge.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> fetchAddressList();

    Address saveAddress(Address address) throws BadRequestException;

    Address updateAddress(Address address) throws BadRequestException;

    void deleteAddressById(Long addressId) throws BadRequestException;

    Address findAddressById(Long addressId) throws BadRequestException;
}
