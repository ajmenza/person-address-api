package com.egt.challenge.controller;

import com.egt.challenge.dto.PersonDto;
import com.egt.challenge.dto.PersonMapper;
import com.egt.challenge.error.BadRequestException;
import com.egt.challenge.model.Person;
import com.egt.challenge.service.PersonService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping(PersonController.BASE_URL)
@RequiredArgsConstructor
public class PersonController {
    public static final String BASE_URL = "/api/persons";

    @NonNull
    private final PersonService personService;
    @NonNull
    private final PersonMapper personMapper;

    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @PostMapping
    public PersonDto savePerson(@RequestBody PersonDto personDto) throws BadRequestException {
        Person person = personMapper.toEntity(personDto);
        Person savedPerson = personService.savePerson(person);
        return personMapper.toDto(savedPerson);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDto> fetchPersonList() {
        List<Person> personList = personService.fetchPersonList();
        List<PersonDto> personDtoList = personList.stream()
                .map(personMapper::toDtoAll).collect(Collectors.toList());
        return personDtoList;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto getPersonById(@PathVariable("id") Long personId) throws BadRequestException {
        if (Objects.isNull(personId)) throw new BadRequestException("No id provided");
        Person person = personService.getPersonById(personId);
        return personMapper.toDto(person);
    }


    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PersonDto updatePerson(@RequestBody PersonDto personDto) throws BadRequestException {
        Person person = personMapper.toEntity(personDto);
        Person savedPerson = personService.updatePerson(person);
        return personMapper.toDto(savedPerson);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deletePersonById(@PathVariable("id") Long personId) throws BadRequestException {
        if (Objects.isNull(personId)) throw new BadRequestException("No id provided");
        personService.deletePersonById(personId);
        return "Person successfully deleted";
    }

    @PostMapping("/lastName")
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDto> findPersonByLastName(@RequestBody PersonDto personDto) throws BadRequestException {
        Person person = personMapper.toEntity(personDto);
        List<Person> personList = personService.getPersonByLastName(person);
        List<PersonDto> personDtoList = personList.stream()
                .map(personMapper::toDtoAll).collect(Collectors.toList());
        return personDtoList;
    }

}
