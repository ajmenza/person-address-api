package com.egt.challenge.dto;

import com.egt.challenge.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapperImpl implements PersonMapper {
    @Override
    public PersonDto toDto(Person entity) {
        // TODO implement method to transform Person object to PersonDto
        return null;
    }

    @Override
    public Person toEntity(PersonDto dto) {
        // TODO implement method to transform PersonDto object to Person
        return null;
    }
}
