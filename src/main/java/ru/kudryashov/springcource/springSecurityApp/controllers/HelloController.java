package ru.kudryashov.springcource.springSecurityApp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kudryashov.springcource.springSecurityApp.security.PersonDetails;
import ru.kudryashov.springcource.springSecurityApp.servicers.PreAuthService;

@Controller
@RequestMapping("/hello")
public class HelloController {

    private final PreAuthService preAuthService;

    public HelloController(PreAuthService preAuthService) {
        this.preAuthService = preAuthService;
    }

    @GetMapping()
    public String hello() {
        return "hello";
    }

    @GetMapping("/showUser")
    public String showUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());
        return "hello";
    }

    @GetMapping("/admin")
    public String adminPage() {
        preAuthService.doSomething();
        return "admin";
    }
}
