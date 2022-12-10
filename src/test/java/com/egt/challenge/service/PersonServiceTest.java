package com.egt.challenge.service;

import com.egt.challenge.dto.AddressDto;
import com.egt.challenge.dto.PersonDto;
import com.egt.challenge.dto.PersonMapperImpl;
import com.egt.challenge.error.BadRequestException;
import com.egt.challenge.model.Address;
import com.egt.challenge.model.Person;
import com.egt.challenge.repo.PersonRepositoryImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PersonServiceTest {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    @Autowired
    private PersonServiceImpl personService;
    @Autowired
    private AddressServiceImpl addressService;
    @Autowired
    private PersonMapperImpl personMapper;
    @MockBean
    private PersonRepositoryImpl personRepository;

    private List<Person> buildPersonList(String firstName1, String lastName1,
                                         String firstName2, String lastName2) {


        AddressDto addlAddr1 = AddressDto.builder()
                .id(1L)
                .street1("66 English Club Drive")
                .city("Englishtown")
                .state("NJ")
                .zipCode("07726")
                .build();

        AddressDto addlAddr2 = AddressDto.builder()
                .id(2L)
                .street1("66 English Club Drive")
                .city("Englishtown")
                .state("NJ")
                .zipCode("07726")
                .build();

        AddressDto addlAddr3 = AddressDto.builder()
                .id(3L)
                .street1("66 English Club Drive")
                .city("Englishtown")
                .state("NJ")
                .zipCode("07726")
                .build();

        List<AddressDto> additionalAddresses1 = new ArrayList<>();
        additionalAddresses1.add(addlAddr1);
        additionalAddresses1.add(addlAddr2);

        List<AddressDto> additionalAddresses2 = new ArrayList<>();
        additionalAddresses2.add(addlAddr3);

        PersonDto personDto1 = PersonDto.builder()
                .id(1L)
                .firstName(firstName1)
                .lastName(lastName1)
                .street1("65 English Club Drive")
                .city("Englishtown")
                .state("NJ")
                .birthDate(LocalDate.parse("10-04-1993", formatter))
                .additionalAddresses(additionalAddresses1)
                .build();

        PersonDto personDto2 = PersonDto.builder()
                .id(1L)
                .firstName(firstName2)
                .lastName(lastName2)
                .street1("65 English Club Drive")
                .city("Englishtown")
                .state("NJ")
                .birthDate(LocalDate.parse("10-04-1993", formatter))
                .additionalAddresses(additionalAddresses2)
                .build();

        Person person1 = personMapper.toEntity(personDto1);
        person1.setId(1L);

        Person person2 = personMapper.toEntity(personDto2);
        person2.setId(2L);

        List<Person> personList = new ArrayList<>();
        personList.add(person1);
        personList.add(person2);

        return personList;
    }

    @BeforeEach
    void setUp() {

        List<Person> mockPersonList = buildPersonList("Tony", "Menza",
                "Denise", "Menza");

        Mockito.when(personRepository.findAll()).thenReturn(mockPersonList);
    }

    @Test
    void fetchPersonListTest() {
        List<Person> personUnsortedList = personService.fetchPersonList();

        List<Person> sortedPersonList = buildPersonList("Denise", "Menza",
                "Tony", "Menza");

        for (int i = 0; i < personUnsortedList.size(); i++) {
            Person unsortedPerson = personUnsortedList.get(i);
            Person sortedPerson = sortedPersonList.get(i);
            assertNotNull(unsortedPerson);
            assertEquals(unsortedPerson.getFirstName(), sortedPerson.getFirstName());
            assertEquals(unsortedPerson.getLastName(), sortedPerson.getLastName());
        }

        assertEquals(personUnsortedList.size(), 2);
    }

    @Test
    void whenValidPersonReturnPerson() throws BadRequestException {
        AddressDto addressDto = AddressDto.builder()
                .id(4L)
                .street1("66 English Club Drive")
                .city("Englishtown")
                .state("NJ")
                .zipCode("07726")
                .build();

        List<AddressDto> additionalAddresses = new ArrayList<>();
        additionalAddresses.add(addressDto);

        PersonDto personDto = PersonDto.builder()
                .firstName("Tony")
                .lastName("Menza")
                .street1("65 English Club Drive")
                .city("Englishtown")
                .state("NJ")
                .zipCode("07726")
                .birthDate(LocalDate.parse("10-04-1993", formatter))
                .additionalAddresses(additionalAddresses)
                .build();

        Person person = personMapper.toEntity(personDto);
        person.setId(3L);

        Mockito.when(personRepository.save(person)).thenReturn(person, person);

        Person savedPerson = personService.savePerson(person);

        assertTrue(savedPerson.hasSameValues(person));
    }

}
