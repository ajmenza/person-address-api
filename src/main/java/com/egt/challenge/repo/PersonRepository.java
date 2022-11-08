package com.egt.challenge.repo;

import com.egt.challenge.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository {

    List<Person> findAll();

    Optional<Person> findById(Long id);

    Optional<Person> findByLastName(String lastName);

    Person save(Person person);

    void delete(Person person);
}
