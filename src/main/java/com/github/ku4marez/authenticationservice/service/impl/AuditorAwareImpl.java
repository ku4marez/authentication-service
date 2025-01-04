package com.github.ku4marez.authenticationservice.service.impl;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    //TODO
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("system");
    }
}
