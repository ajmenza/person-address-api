package com.egt.challenge.service;

import com.egt.challenge.error.BadRequestException;
import com.egt.challenge.model.Address;
import com.egt.challenge.model.Person;
import com.egt.challenge.repo.AddressRepository;
import com.egt.challenge.repo.PersonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServiceUtils {

    @NonNull
    private final PersonRepository personRepository;

    @NonNull
    private final AddressRepository addressRepository;

    public void deleteIfMatchingId(Set<Address> list, List<Address> listToDeleteFrom) {
        ArrayList<Integer> deleteIndexes = new ArrayList<Integer>();
        for (Address address : list) {
            for (int i = 0; i < listToDeleteFrom.size(); i++) {
                if (address.getId() == listToDeleteFrom.get(i).getId()) {
                    deleteIndexes.add(i);
                    break;
                }
            }
        }

        if (!deleteIndexes.isEmpty())
            for (int index : deleteIndexes)
                listToDeleteFrom.remove(index);
    }

    public void updateMainAddressFields(Address oldAddress, Address updatedAddress) {
        if (Objects.isNull(updatedAddress.getState()) && Objects.nonNull(oldAddress.getState()))
            updatedAddress.setState(oldAddress.getState());
        if (Objects.isNull(updatedAddress.getZipCode()) && Objects.nonNull(oldAddress.getZipCode()))
            updatedAddress.setZipCode(oldAddress.getZipCode());
        if (Objects.isNull(updatedAddress.getCity()) && Objects.nonNull(oldAddress.getCity()))
            updatedAddress.setCity(oldAddress.getCity());
        if (Objects.isNull(updatedAddress.getStreet1()) && Objects.nonNull(oldAddress.getStreet1()))
            updatedAddress.setStreet1(oldAddress.getStreet1());
        if (Objects.isNull(updatedAddress.getStreet2()) && Objects.nonNull(oldAddress.getStreet2()))
            updatedAddress.setStreet2(oldAddress.getStreet2());

        updatedAddress.setId(oldAddress.getId());

    }

    public void updatePersonFields(Person oldPerson, Person newPerson) {
        if (Objects.nonNull(newPerson.getFirstName()))
            oldPerson.setFirstName(newPerson.getFirstName());
        if (Objects.nonNull(newPerson.getLastName()))
            oldPerson.setLastName(newPerson.getLastName());
    }

    public Set<Address> deleteIfAddressInAddlAddresses(Set<Address> additionalAddresses, Address address) {
        List<Address> addressList = new ArrayList<>(additionalAddresses.stream().toList());
        Boolean deleteAddress = false;
        int deleteIndex = 0;
        for (Address entry : addressList) {
            if (entry.hasSameValues(address)) {
                deleteAddress = true;
                break;
            }
            deleteIndex++;
        }

        if (deleteAddress)
            addressList.remove(deleteIndex);

        return addressList.stream().collect(Collectors.toSet());
    }

    public Person addAddresstoAdditionalAddresses(Person person, Address address) {
        Set<Address> additionalAddresses = person.getAdditionalAddresses();
        address = addressRepository.save(address);
        additionalAddresses.add(address);
        person.setAdditionalAddresses(additionalAddresses);
        return personRepository.save(person);
    }

    public Person findPersonAndThrowErrorIfNotFound(Long id) throws BadRequestException {
        Optional<Person> foundPerson = personRepository.findById(id);
        if (foundPerson.isEmpty()) throw new BadRequestException("Given person not found");
        return foundPerson.get();
    }

    public Address findAddressAndThrowErrorIfNotFound(Long id) throws BadRequestException {
        Optional<Address> foundAddress = addressRepository.findById(id);
        if (foundAddress.isEmpty()) throw new BadRequestException("Given person not found");
        return foundAddress.get();
    }
}
