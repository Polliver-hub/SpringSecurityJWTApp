package ru.kudryashov.springcource.springSecurityApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kudryashov.springcource.springSecurityApp.dto.AuthDto;
import ru.kudryashov.springcource.springSecurityApp.dto.PersonDto;
import ru.kudryashov.springcource.springSecurityApp.mappers.PersonMapper;
import ru.kudryashov.springcource.springSecurityApp.models.Person;
import ru.kudryashov.springcource.springSecurityApp.security.JWTUtil;
import ru.kudryashov.springcource.springSecurityApp.security.RegistrationService;
import ru.kudryashov.springcource.springSecurityApp.util.PersonValidator;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final JWTUtil jwtUtil;
    private final AuthenticationProvider daoAuthenticationProvider;

    @Autowired
    public AuthController(RegistrationService registrationService,
                          PersonValidator personValidator, JWTUtil jwtUtil, AuthenticationProvider daoAuthenticationProvider) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.jwtUtil = jwtUtil;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
        try {
            daoAuthenticationProvider.authenticate(authInputToken);
        } catch (BadCredentialsException exception) {
            return Map.of("message", "Incorrect credentials Error!");
        }
        String token = jwtUtil.generateToken(authDto.getUsername());
        return Map.of("jwt-token", token);
    }

    @PostMapping("/registration")
    public Map<String, String> createNewRegPerson(@RequestBody @Valid PersonDto personDto, BindingResult bindingResult) {
        Person person = PersonMapper.INSTANCE.personDtoToPerson(personDto);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("message", "Novalid Error!");
            // TODO create exception for this case.
        }

        registrationService.register(person);
        String token = jwtUtil.generateToken(person.getUsername());
        return Map.of("jwt-token", token); // TODO need return json
    }

}
