package com.degreemap.DegreeMap.dummy;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/dummy")
// This is so that only access tokens can be used to access this endpoint,
// not refresh tokens.
// If we ever decide to add roles, we would specify them here.
@PreAuthorize("hasAuthority('SCOPE_USER')")
public class DummyController {

    public record DummyResponse(String message) {}

    // How to instantiate a logger
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DummyController.class);

    @GetMapping("/hello")
    public DummyResponse hello(Principal principal) {
        log.info("User with username {} accessed /api/dummy/hello", principal.getName());

        return new DummyResponse("Hello, " + principal.getName() + "!");
    }
}
