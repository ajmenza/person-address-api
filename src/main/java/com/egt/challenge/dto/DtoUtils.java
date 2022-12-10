package com.egt.challenge.dto;

import com.egt.challenge.model.Address;
import com.egt.challenge.model.Person;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class DtoUtils {

    private AddressDto buildAdditionalAddressDto(Address address) {
        AddressDto addressDto = AddressDto.builder()
                .id(address.getId())
                .street1(address.getStreet1())
                .street2(address.getStreet2())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .build();
        return addressDto;
    }

    public PersonDto buildFullPersonDto(Person person) {
        Address mainAddress = person.getMainAddress();
        PersonDto personDto = PersonDto.builder()
                .id(person.getId())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthDate(person.getBirthDate())
                .street1(mainAddress.getStreet1())
                .street2(mainAddress.getStreet2())
                .city(mainAddress.getCity())
                .state(mainAddress.getState())
                .zipCode(mainAddress.getZipCode())
                .build();
        return personDto;
    }

    public List<AddressDto> mapAddressSetToAddressDtoList(Set<Address> additionalAddresses) {
        List<AddressDto> addressList = additionalAddresses.stream()
                .map(this::buildAdditionalAddressDto)
                .collect(Collectors.toList());
        return addressList;
    }


}

