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
public class PersonServiceImpl implements PersonService {
    @NonNull
    private final PersonRepository personRepository;

    @NonNull
    private final AddressRepository addressRepository;

    @NonNull
    private final Validate validate;
    @NonNull
    private final ServiceUtils serviceUtils;

    private Comparator<Person> compareByFirstName = Comparator.comparing(Person::getFirstName);
    private Comparator<Person> compareByLastName = Comparator.comparing(Person::getLastName);
    private Comparator<Person> compareByFullName = compareByLastName.thenComparing(compareByFirstName);


    @Override
    public List<Person> fetchPersonList() {
        List<Person> personList = personRepository.findAll();
        personList.sort(compareByFullName);
        return personList;
    }

    @Override
    public Person savePerson(Person person) throws BadRequestException {
        // Check if required fields are filled in
        validate.checkIfValidPerson(person);

        List<Address> addressList = addressRepository.findAll();

        validate.checkIfValidAddress(person.getMainAddress(), addressList);


        if (Objects.nonNull(person.getAdditionalAddresses())) {
            // Validate addresses and check for duplicates
            for (Address address : person.getAdditionalAddresses())
                validate.checkIfValidAddress(address, addressList);
            // Get id
            person = personRepository.save(person);
            // Save all additional addresses
            for (Address address : person.getAdditionalAddresses()) {
                address.getPerson().setId(person.getId());
                addressRepository.save(address);
            }
        }

        // Save main address
        addressRepository.save(person.getMainAddress());

        return personRepository.save(person);
    }

    @Override
    public Person updatePerson(Person updatedPerson) throws BadRequestException {
        // Check if id given
        if (Objects.isNull(updatedPerson.getId()))
            throw new BadRequestException("Person id is null");

        // Validate birth date field
        if (Objects.nonNull(updatedPerson.getBirthDate()))
            throw new BadRequestException("Birth date cannot be changed");


        // Validate person fields
        validate.checkForNonBlankNonEmptyPerson(updatedPerson);

        // Get person to update and throw error if not found
        Person person = serviceUtils.findPersonAndThrowErrorIfNotFound(updatedPerson.getId());

        // Update person fields (first and last name)
        serviceUtils.updatePersonFields(person, updatedPerson);


        List<Address> addressList = null;
        Address updatedAddress = null;
        Address oldAddress = null;
        Set<Address> newAddlAddresses = null;
        Set<Address> oldAddlAddresses = null;


        Boolean updateAddress = false;

        // Validate main address if given in payload
        if (Objects.nonNull(updatedPerson.getMainAddress())) {
            addressList = addressRepository.findAll();
            if (Objects.isNull(addressList)) throw new BadRequestException("No addresses to update");

            updatedAddress = updatedPerson.getMainAddress();
            validate.checkForNonBlankNonEmptyAddress(updatedAddress);
            validate.checkIfDuplicateAddress(updatedAddress, addressList);

            // Set new address fields
            oldAddress = person.getMainAddress();
            serviceUtils.updateMainAddressFields(oldAddress, updatedAddress);

            // Update addressList
            validate.updateIfAddressInList(addressList, updatedAddress);

            // Set new main address in person
            person.setMainAddress(updatedAddress);

            updateAddress = true;
        }

        Boolean updateAddresses = false;

        // Validate additional addresses
        if (Objects.nonNull(updatedPerson.getAdditionalAddresses())) {
            // Get address list if not already gotten before
            if (Objects.isNull(addressList)) addressList = addressRepository.findAll();
            if (Objects.isNull(addressList)) throw new BadRequestException("No addresses to update");

            newAddlAddresses = updatedPerson.getAdditionalAddresses();
            oldAddlAddresses = person.getAdditionalAddresses();

            // Delete old addresses from copy db
            serviceUtils.deleteIfMatchingId(oldAddlAddresses, addressList);

            // Save new addresses to copy db
            for (Address address : newAddlAddresses) {
                validate.checkForRequiredAddressFields(address);
                addressList.add(address);
            }

            // Check for duplicates
            for (int i = 0; i < addressList.size(); i++) {
                for (int j = 0; j < addressList.size(); j++) {
                    if (i == j) continue;
                    if (addressList.get(i).hasSameValues(addressList.get(j)))
                        throw new BadRequestException("Cannot have duplicate addresses");
                }
            }
            updateAddresses = true;
        }

        if (updateAddress)
            addressRepository.save(updatedAddress);

        // Save new additional addresses in database and get new additional addresses list for updated person
        if (updateAddresses) {
            for (Address address : oldAddlAddresses) {
                Optional<Address> foundAddress = addressRepository.findById(address.getId());
                if (foundAddress.isPresent()) addressRepository.delete(address);
            }

            for (Address address : newAddlAddresses) {
                address.setPerson(updatedPerson);
                addressRepository.save(address);
            }

            person.setAdditionalAddresses(newAddlAddresses);
        }

        return personRepository.save(person);
    }


    @Override
    public void deletePersonById(Long personId) throws BadRequestException {
        Person person = serviceUtils.findPersonAndThrowErrorIfNotFound(personId);

        // Delete main address
        Optional<Address> mainAddress = addressRepository.findById(person.getMainAddress().getId());
        // MARK FOR DELETION
        if (mainAddress.isPresent()) addressRepository.delete(mainAddress.get());

        // Delete additional addresses
        if (Objects.nonNull(person.getAdditionalAddresses())) {
            for (Address address : person.getAdditionalAddresses())
                addressRepository.delete(address);
        }

        personRepository.delete(person);
    }

    @Override
    public Person getPersonById(Long personId) throws BadRequestException {
        Person person = serviceUtils.findPersonAndThrowErrorIfNotFound(personId);
        return person;
    }

    @Override
    public List<Person> getPersonByLastName(Person person) throws BadRequestException {
        if (Objects.isNull(person.getLastName()) || "".equalsIgnoreCase(person.getLastName()))
            throw new BadRequestException("No last name provided");
        return personRepository.findByLastName(person.getLastName());
    }

    // TODO create methods to create, read, update, and delete Persons as outlined in the README

}
