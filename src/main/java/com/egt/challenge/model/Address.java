package com.egt.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    private Long id;
    private Person person;
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String zipCode;

    public Boolean hasSameValues(Address address) {
        if (Objects.equals(this.getStreet1(), address.getStreet1()) &&
                Objects.equals(this.getStreet2(), address.getStreet2()) &&
                Objects.equals(this.getCity(), address.getCity()) &&
                Objects.equals(this.getState(), address.getState()) &&
                Objects.equals(this.getZipCode(), address.getZipCode())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
