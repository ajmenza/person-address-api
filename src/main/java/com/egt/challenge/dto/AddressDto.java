package com.egt.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private Long id;
    private PersonDto person;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipCode;
}
