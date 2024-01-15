package ru.kudryashov.springcource.springSecurityApp.servicers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class PreAuthService {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void doSomething() {
        System.out.println("admin is here!!!");
    }
}
