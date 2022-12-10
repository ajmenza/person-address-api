package com.egt.challenge.service;

import com.egt.challenge.error.BadRequestException;
import com.egt.challenge.model.Person;

import java.util.List;

public interface PersonService {
    List<Person> fetchPersonList();

    Person savePerson(Person person) throws BadRequestException;

    Person updatePerson(Person person) throws BadRequestException;

    void deletePersonById(Long personId) throws BadRequestException;

    Person getPersonById(Long personId) throws BadRequestException;

    List<Person> getPersonByLastName(Person person) throws BadRequestException;

}
