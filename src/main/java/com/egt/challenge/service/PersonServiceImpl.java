package com.egt.challenge.service;

import com.egt.challenge.repo.PersonRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    @NonNull
    private final PersonRepository personRepository;

    // TODO create methods to create, read, update, and delete Persons as outlined in the README

}
