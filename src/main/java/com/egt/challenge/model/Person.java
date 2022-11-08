package com.egt.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private Address mainAddress;
    private Set<Address> additionalAddresses;
}
