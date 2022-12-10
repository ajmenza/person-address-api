package com.egt.challenge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    public Boolean hasSameValues(Person person) {
        if (!Objects.equals(this.getFirstName(), person.getFirstName()) ||
                !Objects.equals(this.getLastName(), person.getLastName()) ||
                !Objects.equals(this.getBirthDate(), person.getBirthDate()) ||
                !this.getMainAddress().hasSameValues(person.getMainAddress())) return false;

        if (this.additionalAddresses.size() != person.getAdditionalAddresses().size())
            return false;

        if (!this.additionalAddresses.isEmpty() && !person.getAdditionalAddresses().isEmpty()) {
            List<Address> compareList1 = person.getAdditionalAddresses().stream().toList();
            List<Address> compareList2 = this.getAdditionalAddresses().stream().toList();
            for (int i = 0; i < compareList1.size(); i++)
                if (!compareList1.get(i).hasSameValues(compareList2.get(i)))
                    return false;
        }
        return true;
    }

}
