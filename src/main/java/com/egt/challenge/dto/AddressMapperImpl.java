package com.egt.challenge.dto;

import com.egt.challenge.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapperImpl implements AddressMapper{
    @Override
    public AddressDto toDto(Address entity) {
        // TODO implement method to transform Address object to AddressDto
        return null;
    }

    @Override
    public Address toEntity(AddressDto dto) {
        // TODO implement method to transform AddressDto object to Address
        return null;
    }
}
