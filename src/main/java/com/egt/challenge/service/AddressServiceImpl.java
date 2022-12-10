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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    @NonNull
    private final AddressRepository addressRepository;
    @NonNull
    private final PersonRepository personRepository;
    @NonNull
    private final Validate validate;
    @NonNull
    private final PersonServiceImpl personService;
    @NonNull
    private final ServiceUtils serviceUtils;

    private Comparator<Address> compareByStreet1 = Comparator.comparing(Address::getStreet1);
    private Comparator<Address> compareByState = Comparator.comparing(Address::getState);
    private Comparator<Address> compareByStreet1ThenState = compareByStreet1.thenComparing(compareByState);

    @Override
    public List<Address> fetchAddressList() {
        List<Address> addressList = addressRepository.findAll();
        // Remove null values to prepare for sorting
        addressList.stream().forEach(address -> {
            if (Objects.isNull(address.getStreet1())) address.setStreet1("");
            if (Objects.isNull(address.getState())) address.setState("");
        });
        addressList = addressList.stream().sorted(compareByStreet1ThenState).collect(Collectors.toList());
        return addressList;
    }

    @Override
    public Address saveAddress(Address address) throws BadRequestException {
        // Check if a person id was given
        if (Objects.isNull(address.getPerson().getId()))
            throw new BadRequestException("Person id is null");

        List<Address> addressList = addressRepository.findAll();

        // Throw error if invalid address fields or already exists
        validate.checkIfValidAddress(address, addressList);

        // Check if person in database
        Person foundPerson = serviceUtils.findPersonAndThrowErrorIfNotFound(address.getPerson().getId());

        Person updatedPerson = serviceUtils.addAddresstoAdditionalAddresses(foundPerson, address);
        address.setPerson(updatedPerson);

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Address newAddress) throws BadRequestException {
        // Make sure an id is attached
        if (Objects.isNull(newAddress.getId())) throw new BadRequestException("Address id cannot be null");

        // Validate new address fields
        validate.checkForNonBlankNonEmptyAddress(newAddress);


        // Get address and throw error if not found
        Address oldAddress = serviceUtils.findAddressAndThrowErrorIfNotFound(newAddress.getId());

        // Get person associated with old address
        Person oldPerson = serviceUtils.findPersonAndThrowErrorIfNotFound(oldAddress.getPerson().getId());

        if (Objects.isNull(newAddress.getPerson()))
            newAddress.setPerson(oldAddress.getPerson());

        // Update new address with old addresses fields if any fields are null
        serviceUtils.updateMainAddressFields(oldAddress, newAddress);

        // Check for new address already exists
        List<Address> addressList = addressRepository.findAll();

        // Check if person id changed and switch owners if it did
        if (oldPerson.getId() != newAddress.getPerson().getId()) {
            // If trying to change owner of main address, throw error
            if (oldAddress.hasSameValues(oldPerson.getMainAddress()))
                throw new BadRequestException("Cannot change owner of main address");

            // Make sure no duplicate address
            validate.checkIfDuplicateAddressSameId(newAddress, addressList);

            // Throw error if new person doesn't exist
            Person newPerson = serviceUtils.findPersonAndThrowErrorIfNotFound(newAddress.getPerson().getId());

            // Delete address from old person
            Set<Address> oldAddlAddresses = oldPerson.getAdditionalAddresses();
            oldAddlAddresses = serviceUtils.deleteIfAddressInAddlAddresses(oldAddlAddresses, oldAddress);
            oldPerson.setAdditionalAddresses(oldAddlAddresses);

            // Add address to new person
            newPerson = serviceUtils.addAddresstoAdditionalAddresses(newPerson, newAddress);
            newAddress.setPerson(newPerson);

            // If the address in question is a main address
        } else if (validate.isMainAddress(newAddress, oldPerson.getMainAddress())) {
            validate.checkIfDuplicateAddress(newAddress, addressList);
            oldPerson.setMainAddress(newAddress);
            newAddress.setPerson(oldPerson);
            newAddress = addressRepository.save(newAddress);

            // If the address in question is an additional address
        } else {
            validate.checkIfDuplicateAddress(newAddress, addressList);

            // Update address in additional addresses
            List<Address> oldAddlAddresses = new ArrayList<>(oldPerson.getAdditionalAddresses().stream().toList());
            newAddress = addressRepository.save(newAddress);
            validate.updateIfAddressInList(oldAddlAddresses, newAddress);

            // Convert back to set and update person with updated additional addresses
            Set<Address> addressSet = oldAddlAddresses.stream().collect(Collectors.toSet());
            oldPerson.setAdditionalAddresses(addressSet);
            newAddress.setPerson(oldPerson);
            newAddress = addressRepository.save(newAddress);
        }

        personRepository.save(oldPerson);

        return newAddress;
    }

    @Override
    public void deleteAddressById(Long addressId) throws BadRequestException {
        if (Objects.isNull(addressId)) throw new BadRequestException("Address id cannot be null");

        Address address = serviceUtils.findAddressAndThrowErrorIfNotFound(addressId);

        // Get person
        Person person = serviceUtils.findPersonAndThrowErrorIfNotFound(address.getPerson().getId());

        // Check if main address
        if (address.hasSameValues(person.getMainAddress()))
            throw new BadRequestException("Cannot delete main address");


        // Remove from additional addresses and update person
        Set<Address> addressSet = person.getAdditionalAddresses();
        addressSet = serviceUtils.deleteIfAddressInAddlAddresses(addressSet, address);
        person.setAdditionalAddresses(addressSet);
        personRepository.save(person);

        // Delete Address
        addressRepository.delete(address);
    }

    @Override
    public Address findAddressById(Long addressId) throws BadRequestException {
        if (Objects.isNull(addressId)) throw new BadRequestException("Address id cannot be null");

        Address address = serviceUtils.findAddressAndThrowErrorIfNotFound(addressId);

        // Get person and attach it
        Person person = serviceUtils.findPersonAndThrowErrorIfNotFound(address.getPerson().getId());

        address.setPerson(person);

        return address;
    }

    // TODO create methods to create, read, update, and delete Persons as outlined in the README
}
