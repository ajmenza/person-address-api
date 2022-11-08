package com.egt.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipCode;
    private List<AddressDto> additionalAddresses;
}
