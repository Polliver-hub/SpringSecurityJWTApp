package ru.kudryashov.springcource.springSecurityApp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kudryashov.springcource.springSecurityApp.dto.AuthDto;
import ru.kudryashov.springcource.springSecurityApp.dto.PersonDto;
import ru.kudryashov.springcource.springSecurityApp.mappers.PersonMapper;
import ru.kudryashov.springcource.springSecurityApp.models.Person;
import ru.kudryashov.springcource.springSecurityApp.security.JWTUtil;
import ru.kudryashov.springcource.springSecurityApp.security.RegistrationService;
import ru.kudryashov.springcource.springSecurityApp.util.AuthErrorResponse;
import ru.kudryashov.springcource.springSecurityApp.exceptions.AuthIncorrectException;
import ru.kudryashov.springcource.springSecurityApp.exceptions.AuthValidatorException;
import ru.kudryashov.springcource.springSecurityApp.util.PersonValidator;

import java.time.LocalDateTime;
import java.util.List;
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
    public ResponseEntity<Map<String, String>> performLogin(@RequestBody AuthDto authDto) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());
        try {
            daoAuthenticationProvider.authenticate(authInputToken);
        } catch (BadCredentialsException exception) {
            throw new AuthIncorrectException();
        }
        String token = jwtUtil.generateToken(authDto.getUsername());
        return new ResponseEntity<>(Map.of("jwt-token", token), HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<Map<String, String>> createNewRegPerson(@RequestBody @Valid PersonDto personDto, BindingResult bindingResult) {
        Person person = PersonMapper.INSTANCE.personDtoToPerson(personDto);

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new AuthValidatorException(bindingResult.getFieldErrors());
        }

        registrationService.register(person);
        String token = jwtUtil.generateToken(person.getUsername());
        return new ResponseEntity<>(Map.of("jwt-token", token), HttpStatus.CREATED);
    }

    @ExceptionHandler
    private ResponseEntity<AuthErrorResponse> handleException(AuthIncorrectException exception) {
        AuthErrorResponse response = new AuthErrorResponse(
                "Incorrect login or password", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    private ResponseEntity<List<FieldError>> handleException(AuthValidatorException exception) {
        return new ResponseEntity<>(exception.getListErrors(), HttpStatus.BAD_REQUEST);
    }

}
