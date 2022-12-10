package com.egt.challenge.repo;

import com.egt.challenge.model.Person;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class PersonRepositoryImpl implements PersonRepository {

    private final Map<Long, Person> repo = new HashMap<>();
    private Comparator<Person> compareById = Comparator.comparing(Person::getId);

    @Override
    public List<Person> findAll() {
        return new ArrayList<>(repo.values());
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(repo.get(id));
    }

    @Override
    public List<Person> findByLastName(String lastName) {
        return repo.values()
                .stream()
                .filter(p -> p.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public Person save(Person person) {
        if (person.getId() == null) {
            List<Person> personList = findAll();
            if (personList.isEmpty() || personList == null)
                person.setId(1L);
            else {
                // Sort by id number from low to high
                personList = personList.stream().sorted(compareById).collect(Collectors.toList());
                Person lastElement = personList.get(personList.size() - 1);
                // Set id to id of last element (highest id) plus 1
                person.setId(lastElement.getId() + 1L);
            }
        }
        repo.put(person.getId(), person);
        return person;
    }


    @Override
    public void delete(Person person) {
        repo.remove(person.getId());
    }
}
