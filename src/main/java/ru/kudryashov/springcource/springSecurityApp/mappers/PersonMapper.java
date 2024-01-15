package ru.kudryashov.springcource.springSecurityApp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import ru.kudryashov.springcource.springSecurityApp.dto.PersonDto;
import ru.kudryashov.springcource.springSecurityApp.models.Person;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
    Person personDtoToPerson(PersonDto personDto);
    PersonDto PersonToPersonDTO(Person person);
    List<PersonDto> personListToPersonDtoList(List<Person> people);
}