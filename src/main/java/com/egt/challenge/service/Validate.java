package com.egt.challenge.service;

import com.egt.challenge.error.BadRequestException;
import com.egt.challenge.model.Address;
import com.egt.challenge.model.Person;
import com.egt.challenge.repo.AddressRepository;
import com.egt.challenge.repo.PersonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class Validate {

    @NonNull
    private final AddressRepository addressRepository;

    @NonNull
    private final PersonRepository personRepository;


    public Boolean stringEmptyOrBlank(String string) {
        if (string.isEmpty() && string.isBlank())
            return true;
        return false;
    }

    // If zip code or state is non-null, make sure they are also non-blank
    public void checkForNonBlankNonEmptyAddress(Address address) throws BadRequestException {
        List<String> errors = new ArrayList<>();

        if (Objects.nonNull(address.getZipCode()))
            if (stringEmptyOrBlank(address.getZipCode()))
                errors.add("Updated zip code cannot be blank");
        if (Objects.nonNull(address.getState()))
            if (stringEmptyOrBlank(address.getState()))
                errors.add("Updated state cannot be blank");

        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }

    public void checkForNonBlankNonEmptyPerson(Person person) throws BadRequestException {
        List<String> errors = new ArrayList<>();

        if (Objects.nonNull(person.getFirstName()))
            if (stringEmptyOrBlank(person.getFirstName()))
                errors.add("Updated first name cannot be blank");
        if (Objects.nonNull(person.getLastName()))
            if (stringEmptyOrBlank(person.getLastName()))
                errors.add("Updated last name cannot be blank");

        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }


    // Checks if an address already exists in the database and throws error if it does
    public void checkIfDuplicateAddress(Address address, List<Address> addressList) throws BadRequestException {
        for (Address foundAddress : addressList) {
            if (address.hasSameValues(foundAddress)) {
                throw new BadRequestException("Address given already exists");
            }
        }

    }

    // Same as previous except skips over address with same id
    public void checkIfDuplicateAddressSameId(Address address, List<Address> addressList) throws BadRequestException {
        for (Address foundAddress : addressList) {
            if (address.hasSameValues(foundAddress)) {
                if (address.getId() == foundAddress.getId())
                    continue;
                throw new BadRequestException("Address given already exists");
            }
        }

    }


    public void checkForRequiredAddressFields(Address address) throws BadRequestException {
        List<String> errors = new ArrayList<>();

        if (Objects.isNull(address.getZipCode()) || "".equalsIgnoreCase(address.getZipCode()))
            errors.add("Zip code cannot be null or blank");

        if (Objects.isNull(address.getState()) || "".equalsIgnoreCase(address.getState()))
            errors.add("State cannot be null or blank");

        // If an error message has been added, throw an error with all the messages
        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }

    public void checkIfValidAddress(Address address, List<Address> addressList) throws BadRequestException {
        checkForRequiredAddressFields(address);

        checkIfDuplicateAddress(address, addressList);
    }

    public void checkIfValidPerson(Person person) throws BadRequestException {
        List<String> errors = new ArrayList<>();
        Address personAddress = person.getMainAddress();

        if (Objects.isNull(personAddress))
            errors.add("Main address is null");

        if (Objects.isNull(person.getFirstName()) || "".equalsIgnoreCase(person.getFirstName()))
            errors.add("First name is null or blank");

        if (Objects.isNull(person.getLastName()) || "".equalsIgnoreCase(person.getLastName()))
            errors.add("Last name is null or blank");

        if (Objects.isNull(person.getBirthDate()))
            errors.add("Date is null");

        // If an error message has been added, throw an error with all the messages
        if (!errors.isEmpty())
            throw new BadRequestException(errors);
    }

    public void updateIfAddressInList(List<Address> addressList, Address address) throws BadRequestException {
        Boolean found = false;
        for (int i = 0; i < addressList.size(); i++) {
            if (addressList.get(i).getId() == address.getId()) {
                addressList.set(i, address);
                found = true;
                break;
            }
        }
    }

    public Boolean isMainAddress(Address address, Address mainAddress) {
        return address.getId() == mainAddress.getId();
    }


}
